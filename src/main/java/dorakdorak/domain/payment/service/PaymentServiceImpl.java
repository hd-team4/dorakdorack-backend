package dorakdorak.domain.payment.service;

import dorakdorak.domain.dosirak.dto.response.DosirakOrderDto;
import dorakdorak.domain.dosirak.entity.Dosirak;
import dorakdorak.domain.dosirak.mapper.DosirakMapper;
import dorakdorak.domain.order.dto.OrderDto;
import dorakdorak.domain.order.dto.OrderItemDto;
import dorakdorak.domain.order.enums.OrderStatus;
import dorakdorak.domain.order.enums.OrderType;
import dorakdorak.domain.order.mapper.OrderMapper;
import dorakdorak.domain.payment.dto.request.GroupPaymentRequest;
import dorakdorak.domain.payment.dto.request.OrderItemRequest;
import dorakdorak.domain.payment.dto.request.SinglePaymentRequest;
import dorakdorak.domain.payment.dto.response.PaymentPrepareResponse;
import dorakdorak.global.error.ErrorCode;
import dorakdorak.global.error.exception.EntityNotFoundException;
import dorakdorak.global.error.exception.InvalidValueException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class PaymentServiceImpl implements PaymentService {

  private final OrderMapper orderMapper;
    private final DosirakMapper dosirakMapper;

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

  private PaymentPrepareResponse preparePaymentInternal(Long memberId, List<OrderItemRequest> items, LocalDateTime arrivalAt, boolean isGroupOrder) {
    items = validateItems(items);

    int totalAmount = calculateTotalAmount(items);
    String tossOrderId = generateTossOrderId();
    String orderName = generateOrderName(items);

    Long orderId = orderMapper.getNextOrderId();
    String orderCode = generateOrderCode(orderId);

    OrderDto orderDto = new OrderDto(orderId, tossOrderId, memberId, orderCode,
        OrderStatus.PAYMENT_PENDING.name(), totalAmount, isGroupOrder ? OrderType.GONGGOO.name() : OrderType.NORMAL.name(), arrivalAt);
    orderMapper.insertOrder(orderDto);
    insertOrderItems(memberId, orderId, items);

    return new PaymentPrepareResponse(tossOrderId, totalAmount, orderName);
  }

  private DosirakOrderDto getDosirakOrderInfo(Long dosirakId) {
    return dosirakMapper.findDosirakOrderDtoById(dosirakId)
        .orElseThrow(() -> new EntityNotFoundException(dosirakId + "이 존재하지 않습니다.", ErrorCode.DOSIRAK_NOT_FOUND));
  }

  private List<OrderItemRequest> validateItems(List<OrderItemRequest> items) {
    return Optional.ofNullable(items)
        .filter(list -> !list.isEmpty())
        .orElseThrow(() -> new InvalidValueException(ErrorCode.ORDER_ITEMS_EMPTY.getMessage(), ErrorCode.ORDER_ITEMS_EMPTY));
  }

  private void insertOrderItems(Long memberId, Long orderId, List<OrderItemRequest> items) {
    for (OrderItemRequest item : items) {
      DosirakOrderDto dosirak = getDosirakOrderInfo(item.getDosirakId());
      OrderItemDto orderItemDto = new OrderItemDto(null, orderId, item.getDosirakId(),
          dosirak.getName(), dosirak.getCategory(), dosirak.getPrice(), dosirak.getImageUrl(), OrderStatus.PAYMENT_PENDING.name(), memberId);
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