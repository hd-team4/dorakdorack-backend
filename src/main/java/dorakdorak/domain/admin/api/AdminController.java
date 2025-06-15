package dorakdorak.domain.admin.api;

import dorakdorak.domain.admin.dto.AdminCustomDosirakSaveDto;
import dorakdorak.domain.admin.dto.request.AdminCustomDosirakRegisterRequest;
import dorakdorak.domain.admin.dto.response.AdminCustomsDosiraksRegisterResponse;
import dorakdorak.domain.admin.dto.response.DosirakSearchResponse;
import dorakdorak.domain.admin.dto.response.StatisticsSalesResponse;
import dorakdorak.domain.admin.service.AdminService;
import dorakdorak.domain.auth.dto.response.CustomMemberDetails;
import dorakdorak.domain.order.dto.request.OrderStatusUpdateRequest;
import dorakdorak.domain.order.dto.response.AdminOrderListResponse;
import dorakdorak.domain.order.service.OrderService;
import jakarta.validation.Valid;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/api/admin")
@RestController
@Slf4j
public class AdminController {

  private final OrderService orderService;
  private final AdminService adminService;

  @PatchMapping("/orders/{orderId}/status")
  public ResponseEntity<Void> updateOrderStatus(@PathVariable Long orderId,
      @Valid @RequestBody OrderStatusUpdateRequest request) {
    orderService.updateOrderStatus(orderId, request);
    return ResponseEntity.noContent().build();
  }

  @GetMapping("/orders")
  public ResponseEntity<AdminOrderListResponse> getAdminOrders(
      @RequestParam(value = "page", required = false) Integer page,
      @RequestParam(value = "size", required = false) Integer size
  ) {
    AdminOrderListResponse response = orderService.getAdminOrders(page, size);
    return ResponseEntity.ok(response);
  }

  @GetMapping("/dosiraks")
  public ResponseEntity<DosirakSearchResponse> searchDosiraksByName(
      @RequestParam("name") String name,
      @AuthenticationPrincipal CustomMemberDetails memberDetails) {

    DosirakSearchResponse response = adminService.searchDosiraksByName(name,
        memberDetails.getRole());
    return ResponseEntity.ok(response);
  }

  @PostMapping("/custom-dosiraks")
  public ResponseEntity<AdminCustomsDosiraksRegisterResponse> registerCustomDosirak(
      @RequestBody
      AdminCustomDosirakRegisterRequest adminCustomDosirakRegisterRequest,
      @AuthenticationPrincipal CustomMemberDetails memberDetails) {

    String role = memberDetails.getRole();
    Long id = memberDetails.getId();

    adminService.approveOfficialDosirak(
        new AdminCustomDosirakSaveDto(adminCustomDosirakRegisterRequest, id), role);

    return ResponseEntity.status(HttpStatus.OK)
        .body(new AdminCustomsDosiraksRegisterResponse("success", "커스텀 도시락이 정식 메뉴로 등록되었습니다."));
  }

  @GetMapping("/statistics/sales")
  public ResponseEntity<StatisticsSalesResponse> getWeeklySales(
      @RequestParam(value = "dosirakId", required = false) Optional<Long> dosirakId) {
    StatisticsSalesResponse response = adminService.getWeeklySales(dosirakId.orElse(null));
    return ResponseEntity.ok(response);
  }

}
