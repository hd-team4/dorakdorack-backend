package dorakdorak.domain.payment.service;

import dorakdorak.domain.dosirak.dto.response.DosirakOrderDto;
import dorakdorak.domain.dosirak.mapper.DosirakMapper;
import dorakdorak.domain.order.dto.OrderDto;
import dorakdorak.domain.order.dto.OrderItemDto;
import dorakdorak.domain.order.dto.response.MyOrderItemResponseDto;
import dorakdorak.domain.order.enums.OrderStatus;
import dorakdorak.domain.order.enums.OrderType;
import dorakdorak.domain.order.mapper.OrderMapper;
import dorakdorak.domain.order.service.OrderService;
import dorakdorak.domain.payment.dto.request.GroupPaymentRequest;
import dorakdorak.domain.payment.dto.request.OrderItemRequest;
import dorakdorak.domain.payment.dto.request.PaymentConfirmRequest;
import dorakdorak.domain.payment.dto.request.SinglePaymentRequest;
import dorakdorak.domain.payment.dto.response.PaymentConfirmResponse;
import dorakdorak.domain.payment.dto.response.PaymentPrepareResponse;
import dorakdorak.global.error.ErrorCode;
import dorakdorak.global.error.exception.EntityNotFoundException;
import dorakdorak.global.error.exception.InvalidValueException;
import dorakdorak.global.util.jwt.QrTokenProvider;
import dorakdorak.infra.payment.toss.TossPaymentsClient;
import dorakdorak.infra.payment.toss.TossPaymentsResponse;
import dorakdorak.infra.qrcode.QrCodeUploader;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
@Service
public class PaymentServiceImpl implements PaymentService {

  private final TossPaymentsClient tossPaymentsClient;
  private final QrTokenProvider qrTokenProvider;
  private final QrCodeUploader qrCodeUploader;
  private final OrderMapper orderMapper;
  private final DosirakMapper dosirakMapper;

  private final OrderService orderService;

  private static final String ZERO_WASTE_URL_PREFIX = "https://dorakdorak.com/zero-waste/";

  @Override
  @Transactional
  public PaymentPrepareResponse prepareSinglePayment(Long memberId, SinglePaymentRequest request) {
    LocalDateTime arrivalAt = LocalDateTime.now().plusDays(3);
    return preparePaymentInternal(memberId, request.getOrderItems(), arrivalAt, false);
  }

  @Override
  @Transactional
  public PaymentPrepareResponse prepareGroupPayment(Long memberId, GroupPaymentRequest request) {
    LocalDateTime arrivalAt = request.getArriveAt().atTime(request.getArriveTime(), 0);
    return preparePaymentInternal(memberId, request.getOrderItems(), arrivalAt, true);
  }

  @Override
  @Transactional
  public PaymentConfirmResponse confirmPayment(PaymentConfirmRequest request) {
    TossPaymentsResponse response = tossPaymentsClient.confirm(
        request.getPaymentKey(),
        request.getOrderId(),
        request.getAmount()
    );
    log.info("주문번호: {}", request.getOrderId());

    // 주문 상태 변경
    OrderDto orderDto = orderMapper.findByMerchantOrderId(request.getOrderId())
        .orElseThrow(() -> new EntityNotFoundException(ErrorCode.ORDER_NOT_FOUND.getMessage(),
            ErrorCode.ORDER_NOT_FOUND));
    orderMapper.updateStatus(orderDto.getId(), OrderStatus.PAYMENT_COMPLETED.toString());

    // 주문 아이템의 상태 변경, QR코드 생성 및 삽입
    List<Long> itemIds = orderMapper.findItemIdsByOrderId(orderDto.getId());
    for (Long itemId : itemIds) {
      String token = qrTokenProvider.generateQrToken(orderDto.getId(), itemId,
          orderDto.getMemberId()); // 주문 ID + 주문 아이템 ID + 멤버 ID로 토큰 생성
      String qrImageUrl = qrCodeUploader.uploadQrCodeToS3(ZERO_WASTE_URL_PREFIX + token);
      orderMapper.updateOrderItemStatusAndQr(itemId, OrderStatus.PAYMENT_COMPLETED.toString(),
          qrImageUrl, token);
    }

    // 주문한 아이템 조회
    List<MyOrderItemResponseDto> itemDto = orderMapper.findItemsByOrderId(orderDto.getId());

    // 해당 회원에게 결제 완료 알림 전송
    orderService.notifyOrderStatusChange(orderDto.getId(), OrderStatus.PAYMENT_COMPLETED);

    return new PaymentConfirmResponse(orderDto.getOrderCode(), itemDto);
  }

  private PaymentPrepareResponse preparePaymentInternal(Long memberId, List<OrderItemRequest> items,
      LocalDateTime arrivalAt, boolean isGroupOrder) {
    items = validateItems(items);

    int totalAmount = calculateTotalAmount(items);
    String tossOrderId = generateTossOrderId();
    String orderName = generateOrderName(items);

    Long orderId = orderMapper.getNextOrderId();
    String orderCode = generateOrderCode(orderId);

    OrderDto orderDto = new OrderDto(orderId, tossOrderId, memberId, orderCode,
        OrderStatus.PAYMENT_PENDING.name(), totalAmount,
        isGroupOrder ? OrderType.GONGGOO.name() : OrderType.NORMAL.name(), arrivalAt);
    orderMapper.insertOrder(orderDto);
    insertOrderItems(memberId, orderId, items);

    return new PaymentPrepareResponse(tossOrderId, totalAmount, orderName);
  }

  private DosirakOrderDto getDosirakOrderInfo(Long dosirakId) {
    return dosirakMapper.findDosirakOrderDtoById(dosirakId)
        .orElseThrow(() -> new EntityNotFoundException(dosirakId + "이 존재하지 않습니다.",
            ErrorCode.DOSIRAK_NOT_FOUND));
  }

  private List<OrderItemRequest> validateItems(List<OrderItemRequest> items) {
    return Optional.ofNullable(items)
        .filter(list -> !list.isEmpty())
        .orElseThrow(() -> new InvalidValueException(ErrorCode.ORDER_ITEMS_EMPTY.getMessage(),
            ErrorCode.ORDER_ITEMS_EMPTY));
  }

  private void insertOrderItems(Long memberId, Long orderId, List<OrderItemRequest> items) {
    for (OrderItemRequest item : items) {
      DosirakOrderDto dosirak = getDosirakOrderInfo(item.getDosirakId());
      OrderItemDto orderItemDto = new OrderItemDto(null, orderId, item.getDosirakId(),
          dosirak.getName(), dosirak.getPrice(), dosirak.getImageUrl(),
          OrderStatus.PAYMENT_PENDING.name(), memberId);
      orderMapper.insertOrderItem(orderItemDto);
    }
  }

  private int calculateTotalAmount(List<OrderItemRequest> items) {
    return items.stream()
        .mapToInt(item -> {
          DosirakOrderDto dosirak = getDosirakOrderInfo(item.getDosirakId());
          return dosirak.getPrice() * item.getCount();
        })
        .sum();
  }

  private String generateOrderName(List<OrderItemRequest> items) {
    String orderName = getDosirakOrderInfo(items.get(0).getDosirakId()).getName();
    if (items.size() > 1) {
      orderName += " 외 " + (items.size() - 1) + "건";
    }
    return orderName;
  }

  private String generateTossOrderId() {
    return "order-" + UUID.randomUUID();
  }

  private String generateOrderCode(Long orderId) {
    String datePart = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
    String orderPart = String.format("%05d", orderId);
    return datePart + "_" + orderPart;
  }
}