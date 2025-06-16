package dorakdorak.global.scheduler;

import dorakdorak.domain.order.enums.OrderStatus;
import dorakdorak.domain.order.mapper.OrderMapper;
import dorakdorak.domain.order.service.OrderService;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class GonggooSchedulerService {

  private final OrderMapper orderMapper;

  private final OrderService orderService;

  private static final int DAYS_BEFORE_DEADLINE = 3; // 도착일 기준 며칠 전까지 공동구매를 마감할지 설정 (현재는 3일 전)
  public static final int MINIMUM_ORDER_QUANTITY = 20;  // 도시락 최소 주문 수량 (도시락별 최소 공구 조건)

  /**
   * 매일 자정 실행되는 스케줄러: 공동구매 주문을 마감 처리
   * <p>
   * 1. 현재 날짜 기준으로 3일 뒤에 도착하는 주문들을 조회 2. 각 주문에 대해 도시락별 주문 수량을 집계 3. 수량이 부족한 도시락(orderItem)은 상태를
   * '취소'로 처리 4. 주문 상태는 전체적으로 '공구 마감(GONGGOO_CONFIRMED)' 상태로 변경
   */
  @Transactional
  public void closeGonggooOrders() {
    // 마감 대상 도착일 (오늘 + 3일)
    LocalDate targetDate = LocalDate.now().plusDays(DAYS_BEFORE_DEADLINE);

    // 해당 도착일에 맞춰 생성된 공구 주문 ID 목록 조회
    List<Long> orderIds = orderMapper.findGroupOrderIdsByArrivalAt(targetDate);

    for (Long orderId : orderIds) {
      // 해당 회원에게 공구 마감에 알림 전송
      orderService.notifyOrderStatusChange(orderId, OrderStatus.GONGGOO_CONFIRMED);

      // 도시락별 주문 수량 조회 (도시락 ID → 주문 수량)
      Map<Long, Integer> dosirakCounts = orderMapper.countDosirakOrdersByOrderId(orderId);

      // 최소 주문 수량을 충족하지 못한 도시락 ID 목록 추출
      List<Long> insufficientDosirakIds = dosirakCounts.entrySet().stream()
          .filter(entry -> entry.getValue() < MINIMUM_ORDER_QUANTITY)
          .map(Map.Entry::getKey)
          .collect(Collectors.toList());

      // 부족한 도시락이 있다면 해당 도시락의 orderItem 상태를 '취소' 처리
      if (!insufficientDosirakIds.isEmpty()) {
        int cancelled = orderMapper.cancelOrderItemsByOrderIdAndDosirakIds(orderId,
            insufficientDosirakIds);

        // 해당 회원에게 결제 취소 알림 전송
        orderService.notifyOrderStatusChange(orderId, OrderStatus.PAYMENT_CANCELLED);

        log.info("[공구 취소] orderId={}, 취소된 도시락 수: {}", orderId, cancelled);
      }

      // 주문 상태 전체를 'GONGGOO_CONFIRMED(공구 마감)'으로 변경
      orderMapper.updateStatus(orderId, OrderStatus.GONGGOO_CONFIRMED.name());
      log.info("[공구 마감] orderId={} → GONGGOO_CONFIRMED", orderId);
    }
  }
}