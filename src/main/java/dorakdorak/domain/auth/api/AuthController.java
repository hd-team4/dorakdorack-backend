package dorakdorak.domain.auth.api;

import dorakdorak.domain.auth.dto.response.CustomMemberDetails;
import dorakdorak.domain.member.dto.request.MemberEmailVerificationRequest;
import dorakdorak.domain.member.dto.request.MemberGoogleSMTPRequest;
import dorakdorak.domain.member.dto.request.MemberLoginRequest;
import dorakdorak.domain.member.dto.request.MemberSignupRequest;
import dorakdorak.domain.member.dto.response.MemberEmailVerificationResponse;
import dorakdorak.domain.member.dto.response.MemberGoogleSMTPResponse;
import dorakdorak.domain.member.dto.response.MemberLoginResponse;
import dorakdorak.domain.member.dto.response.MemberSignupResponse;
import dorakdorak.domain.member.service.MailService;
import dorakdorak.domain.member.service.MemberService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.mail.MessagingException;
import java.util.HashMap;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "auth", description = "인증 관련 API (회원가입, 로그인)")
@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {

  private final MailService mailService;
  private final MemberService memberService;

  @Operation(summary = "이메일 인증코드 전송", description = "입력된 이메일로 인증코드를 전송합니다.")
  @GetMapping("/members/{email}")
  public ResponseEntity<MemberGoogleSMTPResponse> requestAuthcode(
      @Parameter(description = "인증코드를 보낼 이메일", example = "test@example.com")
      @PathVariable("email") String email)
      throws MessagingException {

    MemberGoogleSMTPRequest mgr = new MemberGoogleSMTPRequest(email);

    boolean isSend = mailService.sendSimpleMessage(mgr.getEmail());

    return isSend ? ResponseEntity.status(HttpStatus.OK)
        .body(new MemberGoogleSMTPResponse("success", "인증 코드가 전송되었습니다.")) :
        ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body(new MemberGoogleSMTPResponse("fail", "인증 코드 발급에 실패하였습니다."));
  }

  @Operation(summary = "이메일 인증", description = "사용자가 입력한 인증 코드를 검증합니다.")
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


  @Operation(summary = "회원가입", description = "사용자 정보를 입력받아 회원가입 처리합니다.")
  @PostMapping("/signup")
  public ResponseEntity<?> signup(
      @Validated @RequestBody MemberSignupRequest memberSignupRequest,
      BindingResult bindingResult) {

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

  @Operation(summary = "로그인", description = "스프링 시큐리티 필터를 이용한 Swagger 테스트용 로그인 API입니다.")
  @PostMapping("/login")
  public ResponseEntity<MemberLoginResponse> login(
      @RequestBody MemberLoginRequest memberLoginRequest) {
    return ResponseEntity.status(HttpStatus.OK).
        body(new MemberLoginResponse("success", "로그인 성공"));
  }

  @Operation(summary = "인증 테스트", description = "JWT 인증된 사용자 정보 조회", security = {
      @SecurityRequirement(name = "BearerAuth")})
  @GetMapping("/test")
  public String test(@AuthenticationPrincipal CustomMemberDetails customMemberDetails) {
    log.info("test() :: {}", customMemberDetails);
    return customMemberDetails.getUsername();
  }
}
