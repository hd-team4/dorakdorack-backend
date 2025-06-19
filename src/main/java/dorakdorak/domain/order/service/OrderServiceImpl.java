package dorakdorak.domain.order.service;

import dorakdorak.infra.mail.service.OrderMailService;
import dorakdorak.domain.order.dto.AdminOrderDto;
import dorakdorak.domain.order.dto.GroupOrderDto;
import dorakdorak.domain.order.dto.OrderDto;
import dorakdorak.domain.order.dto.request.OrderStatusUpdateRequest;
import dorakdorak.domain.order.dto.response.AdminOrderListResponse;
import dorakdorak.domain.order.dto.response.GroupOrderListResponse;
import dorakdorak.domain.order.dto.MyOrderItemDto;
import dorakdorak.domain.order.dto.response.MyOrderPreviewResponse;
import dorakdorak.domain.order.dto.MyOrderPreviewDto;
import dorakdorak.domain.order.dto.response.MyOrderResponse;
import dorakdorak.domain.order.dto.MyOrderDto;
import dorakdorak.domain.order.dto.OrderMailInfoDto;
import dorakdorak.domain.order.enums.OrderStatus;
import dorakdorak.domain.order.mapper.OrderMapper;
import dorakdorak.global.error.ErrorCode;
import dorakdorak.global.error.exception.BusinessException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class OrderServiceImpl implements OrderService {

  private final OrderMapper orderMapper;
  private final OrderMailService orderMailService;

  @Override
  public MyOrderResponse getNormalOrdersByMemberId(Long memberId, Long orderId, Long count) {
    List<MyOrderDto> myOrders = orderMapper.findNormalOrdersByMemberId(memberId, orderId,
        count);
    if (myOrders == null) {
      throw new BusinessException(ErrorCode.ORDER_DATA_ACCESS_ERROR);
    }

    for (MyOrderDto myOrder : myOrders) {
      List<MyOrderItemDto> items = orderMapper.findItemsByOrderId(myOrder.getOrderId());
      if (items == null) {
        throw new BusinessException(ErrorCode.ORDER_DATA_ACCESS_ERROR);
      }

      myOrder.setItems(items);
    }

    return new MyOrderResponse(myOrders);
  }

  @Override
  public MyOrderPreviewResponse getNormalOrdersPreviewByMemberId(Long memberId) {
    List<MyOrderPreviewDto> myOrdersPreview = orderMapper.findNormalOrdersPreviewByMemberId(
        memberId);
    if (myOrdersPreview == null) {
      throw new BusinessException(ErrorCode.ORDER_DATA_ACCESS_ERROR);
    }

    return new MyOrderPreviewResponse(myOrdersPreview);
  }

  @Override
  public MyOrderResponse getGroupOrdersByMemberId(Long memberId, Long orderId, Long count) {
    List<MyOrderDto> myOrders = orderMapper.findGroupOrdersByMemberId(memberId, orderId,
        count);
    if (myOrders == null) {
      throw new BusinessException(ErrorCode.ORDER_DATA_ACCESS_ERROR);
    }

    for (MyOrderDto myOrder : myOrders) {
      List<MyOrderItemDto> items = orderMapper.findItemsByOrderId(myOrder.getOrderId());
      if (items == null) {
        throw new BusinessException(ErrorCode.ORDER_DATA_ACCESS_ERROR);
      }

      myOrder.setItems(items);
    }

    return new MyOrderResponse(myOrders);
  }

  @Override
  public MyOrderPreviewResponse getGroupOrdersPreviewByMemberId(Long memberId) {
    List<MyOrderPreviewDto> myOrdersPreview = orderMapper.findGroupOrdersPreviewByMemberId(
        memberId);
    if (myOrdersPreview == null) {
      throw new BusinessException(ErrorCode.ORDER_DATA_ACCESS_ERROR);
    }
    return new MyOrderPreviewResponse(myOrdersPreview);
  }

  @Override
  @Transactional
  public void cancelOrder(Long orderId) {
    OrderDto order = orderMapper.findById(orderId)
        .orElseThrow(() -> new BusinessException(ErrorCode.ORDER_NOT_FOUND));

    if (order.getOrderStatus().equals(OrderStatus.GONGGOO_CONFIRMED.name()) ||
        order.getOrderStatus().equals(OrderStatus.DELIVERY_READY.name()) ||
        order.getOrderStatus().equals(OrderStatus.DELIVERY_IN_PROGRESS.name()) ||
        order.getOrderStatus().equals(OrderStatus.DELIVERY_COMPLETED.name())) {
      throw new BusinessException(ErrorCode.CANNOT_CANCEL_ORDER);
    }

    // 주문 아이템을 결제 취소 상태로 변경, QR 코드 무효화
    orderMapper.updateStatus(orderId, OrderStatus.PAYMENT_CANCELLED.name());
    List<Long> itemIds = orderMapper.findItemIdsByOrderId(orderId);
    for (Long itemId : itemIds) {
      orderMapper.updateOrderItemStatusAndQr(itemId, OrderStatus.PAYMENT_CANCELLED.name(), "", "");
    }

    // 해당 회원에게 결제 취소 알림 전송
    notifyOrderStatusChange(orderId, OrderStatus.PAYMENT_CANCELLED);
  }

  @Override
  @Transactional
  public void updateOrderStatus(Long orderId, OrderStatusUpdateRequest request) {
    OrderDto order = orderMapper.findById(orderId)
        .orElseThrow(() -> new BusinessException(ErrorCode.ORDER_NOT_FOUND));

    orderMapper.updateStatus(orderId, request.getOrderStatus());

    // 해당 회원에게 주문 상태 알림 전송
    notifyOrderStatusChange(orderId, OrderStatus.valueOf(request.getOrderStatus()));
  }

  @Override
  public GroupOrderListResponse getGroupOrders(LocalDate arriveAt, int arriveTime,
      Long universityId, Long dosirakId) {
    LocalDateTime arrive = arriveAt.atTime(arriveTime, 0);

    List<GroupOrderDto> orders;
    if (dosirakId == null) {
      orders = orderMapper.findGroupOrdersAll(arrive, universityId);
    } else {
      orders = orderMapper.findGroupOrdersWithExtra(arrive, universityId, dosirakId);
    }
    return new GroupOrderListResponse(orders);
  }

  @Override
  public AdminOrderListResponse getAdminOrders(Long orderId) {

    List<AdminOrderDto> orders = orderMapper.findAdminOrders(orderId);
    int total = orderMapper.countAdminOrders();

    return new AdminOrderListResponse(orders);
  }

  @Override
  public void notifyOrderStatusChange(Long orderId, OrderStatus status) {
    List<OrderMailInfoDto> items = orderMapper.findOrderMailInfoByOrderId(orderId);

    if (items.isEmpty()) {
      log.warn("메일 발송 생략: 주문 아이템 없음 orderId={}", orderId);
      return;
    }

    // 결제 대기 상태는 메일 발송 생략
    if (status == OrderStatus.PAYMENT_PENDING) {
      return;
    }

    orderMailService.sendOrderMail(items, status);
  }


}
