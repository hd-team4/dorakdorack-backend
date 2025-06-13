package dorakdorak.domain.member.api;

import dorakdorak.domain.auth.dto.response.CustomMemberDetails;
import dorakdorak.domain.dosirak.dto.response.MyCustomDosirakResponse;
import dorakdorak.domain.dosirak.service.DosirakService;
import dorakdorak.domain.member.dto.response.MyPageSummaryResponse;
import dorakdorak.domain.member.service.MemberService;
import dorakdorak.domain.order.dto.response.MyOrderPreviewResponse;
import dorakdorak.domain.order.dto.response.MyOrderResponse;
import dorakdorak.domain.order.service.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/members")
public class MemberController {

  private final MemberService memberService;
  private final OrderService orderService;
  private final DosirakService dosirakService;

  // 나의 일반 주문 내역 조회
  @GetMapping("/orders/normal")
  public ResponseEntity<MyOrderResponse> getMyNormalOrders(
      @AuthenticationPrincipal CustomMemberDetails memberDetails) {

    Long memberId = memberDetails.getId();
    MyOrderResponse response = orderService.getNormalOrdersByMemberId(memberId);
    return ResponseEntity.ok(response);
  }

  // 나의 일반 주문 내역 미리보기 정보 조회
  @GetMapping("/orders/normal/preview")
  public ResponseEntity<MyOrderPreviewResponse> getMyNormalOrdersPreview(
      @AuthenticationPrincipal CustomMemberDetails memberDetails) {

    Long memberId = memberDetails.getId();
    MyOrderPreviewResponse response = orderService.getNormalOrdersPreviewByMemberId(memberId);
    return ResponseEntity.ok(response);
  }

  // 나의 공동 주문 내역 조회
  @GetMapping("/orders/group")
  public ResponseEntity<MyOrderResponse> getMyGroupOrders(
      @AuthenticationPrincipal CustomMemberDetails memberDetails) {

    Long memberId = memberDetails.getId();
    MyOrderResponse response = orderService.getGroupOrdersByMemberId(memberId);
    return ResponseEntity.ok(response);
  }

  // 나의 공동 주문 내역 미리보기 정보 조회
  @GetMapping("/orders/group/preview")
  public ResponseEntity<MyOrderPreviewResponse> getMyGroupOrdersPreview(
      @AuthenticationPrincipal CustomMemberDetails memberDetails) {

    Long memberId = memberDetails.getId();
    MyOrderPreviewResponse response = orderService.getGroupOrdersPreviewByMemberId(memberId);
    return ResponseEntity.ok(response);
  }

  // 나의 커스텀 도시락 내역 조회
  @GetMapping("/custom-dosirak")
  public ResponseEntity<MyCustomDosirakResponse> getMyCustomDosiraks(
      @AuthenticationPrincipal CustomMemberDetails memberDetails) {

    Long memberId = memberDetails.getId();
    MyCustomDosirakResponse response = dosirakService.getCustomDosiraksByMemberId(memberId);
    return ResponseEntity.ok(response);
  }

  // 나의 커스텀 도시락 내역 미리보기 정보 조회
  @GetMapping("/custom-dosirak/preview")
  public ResponseEntity<MyCustomDosirakResponse> getMyCustomDosiraksPreview(
      @AuthenticationPrincipal CustomMemberDetails memberDetails) {

    Long memberId = memberDetails.getId();
    MyCustomDosirakResponse response = dosirakService.getCustomDosiraksPreviewByMemberId(memberId);
    return ResponseEntity.ok(response);
  }

  // 마이페이지 상단 요약 정보 조회
  @GetMapping("/orders/summary")
  public ResponseEntity<MyPageSummaryResponse> getMyPageSummary(
      @AuthenticationPrincipal CustomMemberDetails memberDetails) {

    Long memberId = memberDetails.getId();
    MyPageSummaryResponse response = memberService.getMyPageSummary(memberId);
    return ResponseEntity.ok(response);
  }
}
