package dorakdorak.domain.member.api;

import dorakdorak.domain.auth.dto.response.CustomMemberDetails;
import dorakdorak.domain.dosirak.dto.response.MyCustomDosirakResponse;
import dorakdorak.domain.dosirak.service.DosirakService;
import dorakdorak.domain.member.dto.request.MemberEmailVerificationRequest;
import dorakdorak.domain.member.dto.request.MemberSignupRequest;
import dorakdorak.domain.member.dto.response.MemberEmailVerificationResponse;
import dorakdorak.domain.member.dto.request.MemberGoogleSMTPRequest;
import dorakdorak.domain.member.dto.response.MemberGoogleSMTPResponse;
import dorakdorak.domain.member.dto.response.MemberSignupResponse;
import dorakdorak.domain.member.dto.response.MyPageSummaryResponse;
import dorakdorak.domain.member.service.MailService;
import dorakdorak.domain.member.service.MemberService;
import dorakdorak.domain.order.dto.response.MyOrderPreviewResponse;
import dorakdorak.domain.order.dto.response.MyOrderResponse;
import dorakdorak.domain.order.service.OrderService;
import jakarta.mail.MessagingException;
import java.util.HashMap;
import java.util.Map;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class MemberController {

  private final MailService mailService;
  private final MemberService memberService;
  private final OrderService orderService;
  private final DosirakService dosirakService;

  // 인증코드 보내기
  @GetMapping("/members/{email}")
  public ResponseEntity<MemberGoogleSMTPResponse> requestAuthcode(
      @PathVariable("email") String email)
      throws MessagingException {
    MemberGoogleSMTPRequest mgr = new MemberGoogleSMTPRequest(email);
    boolean isSend = mailService.sendSimpleMessage(mgr.getEmail());
    return isSend ? ResponseEntity.status(HttpStatus.OK)
        .body(new MemberGoogleSMTPResponse("success", "인증 코드가 전송되었습니다.")) :
        ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body(new MemberGoogleSMTPResponse("fail", "인증 코드 발급에 실패하였습니다."));
  }

  // 이메일 인증
  @PostMapping("/members/email/verify")
  public ResponseEntity<MemberEmailVerificationResponse> verifyEmail(
      @RequestBody MemberEmailVerificationRequest memberEmailVerificationRequest) {
    String email = memberEmailVerificationRequest.getEmail();
    String code = memberEmailVerificationRequest.getCode(); //사용자가 입력한 코드
    String savedCode = mailService.getVerificationCode(email); //redis에 저장된 코드
    mailService.verificationEmail(code, savedCode);
    return ResponseEntity.status(HttpStatus.OK)
        .body(new MemberEmailVerificationResponse("success", "이메일 인증 성공"));
  }

  // 회원가입
  @PostMapping("/signup")
  public ResponseEntity<?> signup(
      @Validated @RequestBody MemberSignupRequest memberSignupRequest,
      BindingResult bindingResult) {

    log.info("회원가입 정보:{}", memberSignupRequest.toString());
    if (bindingResult.hasErrors()) {
      Map<String, String> errors = new HashMap<>();
      bindingResult.getFieldErrors().forEach(error ->
          errors.put(error.getField(), error.getDefaultMessage())
      );
      return ResponseEntity.status(HttpStatus.BAD_REQUEST)
          .body(Map.of("errors", errors, "message", "입력값을 확인해주세요."));
    }

    memberService.joinMember(memberSignupRequest);

    return ResponseEntity.status(HttpStatus.OK)
        .body(new MemberSignupResponse("success", "회원가입 성공"));
  }

  // 나의 일반 주문 내역 조회
  @GetMapping("/orders/normal")
  public ResponseEntity<MyOrderResponse> getMyNormalOrders(@AuthenticationPrincipal CustomMemberDetails memberDetails) {

    Long memberId = memberDetails.getId();
    MyOrderResponse response = orderService.getNormalOrdersByMemberId(memberId);
    return ResponseEntity.ok(response);
  }

  // 나의 일반 주문 내역 미리보기 정보 조회
  @GetMapping("/orders/normal/preview")
  public ResponseEntity<MyOrderPreviewResponse> getMyNormalOrdersPreview(@AuthenticationPrincipal CustomMemberDetails memberDetails) {

    Long memberId = memberDetails.getId();
    MyOrderPreviewResponse response = orderService.getNormalOrdersPreviewByMemberId(memberId);
    return ResponseEntity.ok(response);
  }

  // 나의 공동 주문 내역 조회
  @GetMapping("/orders/group")
  public ResponseEntity<MyOrderResponse> getMyGroupOrders(@AuthenticationPrincipal CustomMemberDetails memberDetails) {

    Long memberId = memberDetails.getId();
    MyOrderResponse response = orderService.getGroupOrdersByMemberId(memberId);
    return ResponseEntity.ok(response);
  }

  // 나의 공동 주문 내역 미리보기 정보 조회
  @GetMapping("/orders/group/preview")
  public ResponseEntity<MyOrderPreviewResponse> getMyGroupOrdersPreview(@AuthenticationPrincipal CustomMemberDetails memberDetails) {

    Long memberId = memberDetails.getId();
    MyOrderPreviewResponse response = orderService.getGroupOrdersPreviewByMemberId(memberId);
    return ResponseEntity.ok(response);
  }

  // 나의 커스텀 도시락 내역 조회
  @GetMapping("/custom-dosirak")
  public ResponseEntity<MyCustomDosirakResponse> getMyCustomDosiraks(@AuthenticationPrincipal CustomMemberDetails memberDetails) {

    Long memberId = memberDetails.getId();
    MyCustomDosirakResponse response = dosirakService.getCustomDosiraksByMemberId(memberId);
    return ResponseEntity.ok(response);
  }

  // 나의 커스텀 도시락 내역 미리보기 정보 조회
  @GetMapping("/custom-dosirak/preview")
  public ResponseEntity<MyCustomDosirakResponse> getMyCustomDosiraksPreview(@AuthenticationPrincipal CustomMemberDetails memberDetails) {

    Long memberId = memberDetails.getId();
    MyCustomDosirakResponse response = dosirakService.getCustomDosiraksPreviewByMemberId(memberId);
    return ResponseEntity.ok(response);
  }

  // 마이페이지 상단 요약 정보 조회
  @GetMapping("/orders/summary")
  public ResponseEntity<MyPageSummaryResponse> getMyPageSummary(@AuthenticationPrincipal CustomMemberDetails memberDetails) {

    Long memberId = memberDetails.getId();
    MyPageSummaryResponse response = memberService.getMyPageSummary(memberId);
    return ResponseEntity.ok(response);
  }

}
