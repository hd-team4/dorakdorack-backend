package dorakdorak.domain.member.api;

import dorakdorak.domain.member.dto.request.MemberEmailVerificationRequest;
import dorakdorak.domain.member.dto.request.MemberSignupRequest;
import dorakdorak.domain.member.dto.response.MemberEmailVerificationResponse;
import dorakdorak.domain.member.dto.request.MemberGoogleSMTPRequest;
import dorakdorak.domain.member.dto.response.MemberGoogleSMTPResponse;
import dorakdorak.domain.member.dto.response.MemberSignupResponse;
import dorakdorak.domain.member.service.MailService;
import dorakdorak.domain.member.service.MemberService;
import jakarta.mail.MessagingException;
import java.util.HashMap;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/members")
public class MemberController {

  private final MailService mailService;
  private final MemberService memberService;

  // 인증코드 보내기
  @GetMapping("/api/email/auth/{email}")
  public ResponseEntity<MemberGoogleSMTPResponse> requestAuthcode(
      @ModelAttribute MemberGoogleSMTPRequest mgr)
      throws MessagingException {
    boolean isSend = mailService.sendSimpleMessage(mgr.getEmail());
    return isSend ? ResponseEntity.status(HttpStatus.OK)
        .body(new MemberGoogleSMTPResponse("success", "인증 코드가 전송되었습니다.")) :
        ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body(new MemberGoogleSMTPResponse("fail", "인증 코드 발급에 실패하였습니다."));
  }

  // 이메일 인증
  @PostMapping("/api/email/verify")
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
}
