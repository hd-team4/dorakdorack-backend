package dorakdorak.domain.order.api;

import dorakdorak.domain.auth.dto.response.CustomMemberDetails;
import dorakdorak.domain.order.dto.response.GroupOrderListResponse;
import dorakdorak.domain.order.service.OrderService;
import java.time.LocalDate;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/api/orders")
@RestController
public class OrderController {

  private final OrderService orderService;

  @DeleteMapping("/{orderId}")
  public ResponseEntity<Void> cancelOrder(@PathVariable Long orderId) {
    orderService.cancelOrder(orderId);
    return ResponseEntity.noContent().build();
  }

  @GetMapping("/group")
  public GroupOrderListResponse getGroupOrders(
      @AuthenticationPrincipal CustomMemberDetails member,
      @RequestParam LocalDate arriveAt,
      @RequestParam int arriveTime,
      @RequestParam(required = false) Long dosirakId
  ) {
    return orderService.getGroupOrders(arriveAt, arriveTime, 1L, dosirakId);
  }
}