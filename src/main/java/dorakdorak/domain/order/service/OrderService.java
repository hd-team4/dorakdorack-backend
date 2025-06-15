package dorakdorak.domain.order.service;

import dorakdorak.domain.order.dto.request.OrderStatusUpdateRequest;
import dorakdorak.domain.order.dto.response.AdminOrderListResponse;
import dorakdorak.domain.order.dto.response.GroupOrderListResponse;
import dorakdorak.domain.order.dto.response.MyOrderPreviewResponse;
import dorakdorak.domain.order.dto.response.MyOrderResponse;
import dorakdorak.domain.order.enums.OrderStatus;
import java.time.LocalDate;

public interface OrderService {

  // 회원 ID로 일반 주문 내역 조회
  MyOrderResponse getNormalOrdersByMemberId(Long memberId, Long orderId, Long count);

  // 회원 ID로 일반 주문 내역 미리보기 정보 조회
  MyOrderPreviewResponse getNormalOrdersPreviewByMemberId(Long memberId);

  // 회원 ID로 공동 주문 내역 조회
  MyOrderResponse getGroupOrdersByMemberId(Long memberId, Long orderId, Long count);

  // 회원 ID로 공동 주문 내역 미리보기 정보 조회
  MyOrderPreviewResponse getGroupOrdersPreviewByMemberId(Long memberId);

  void cancelOrder(Long orderId);

  void updateOrderStatus(Long orderId, OrderStatusUpdateRequest request);

  GroupOrderListResponse getGroupOrders(LocalDate arriveAt, int arriveTime, Long uid,
      Long dosirakId);

  AdminOrderListResponse getAdminOrders(Integer page, Integer size);

  void notifyOrderStatusChange(Long orderId, OrderStatus status);
}
