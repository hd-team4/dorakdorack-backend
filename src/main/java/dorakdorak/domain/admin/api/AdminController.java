package dorakdorak.domain.admin.api;

import dorakdorak.domain.order.dto.request.OrderStatusUpdateRequest;
import dorakdorak.domain.order.service.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/api/admin")
@RestController
public class AdminController {

  private final OrderService orderService;

  @PatchMapping("/orders/{orderId}/status")
  public ResponseEntity<Void> updateOrderStatus(@PathVariable Long orderId, @Valid @RequestBody OrderStatusUpdateRequest request) {
    orderService.updateOrderStatus(orderId, request);
    return ResponseEntity.noContent().build();
  }
}