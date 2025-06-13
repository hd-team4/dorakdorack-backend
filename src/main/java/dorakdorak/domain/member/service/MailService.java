package dorakdorak.domain.member.service;

import dorakdorak.global.error.ErrorCode;
import dorakdorak.global.error.exception.BusinessException;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j // 이 한 줄이면 log.info(), log.error() 등 사용 가능!
public class MailService {

  private final JavaMailSender javaMailSender;
  private final StringRedisTemplate redisTemplate;
  private final MemberService memberService;

  @Value("${spring.mail.username}")
  private String senderEmail;

  public String createCode() {
    Random random = new Random();
    StringBuilder key = new StringBuilder();

    for (int i = 0; i < 6; i++) {
      int index = random.nextInt(2);
      switch (index) {
        case 0 -> key.append((char) (random.nextInt(26) + 65)); // 대문자
        case 1 -> key.append(random.nextInt(10)); // 숫자
      }
    }
    log.debug("인증 코드 생성됨: code={}", key);
    return key.toString();
  }

  public MimeMessage createMail(String mail, String authCode) throws MessagingException {
    MimeMessage message = javaMailSender.createMimeMessage();

    message.setFrom(senderEmail);
    message.setRecipients(MimeMessage.RecipientType.TO, mail);
    message.setSubject("이메일 인증키 발급");

    String body = """
        <div style="max-width:420px; margin:40px auto; font-family:'Pretendard','Noto Sans KR',Arial,sans-serif; color:#222;">
            <h2 style="text-align:center; font-weight:700; font-size:28px; margin-bottom:10px; letter-spacing:-1px;">이메일 인증키 발급</h2>
            <hr style="border:none; border-top:1px solid #ddd; margin:16px 0 24px 0;">
            <p style="text-align:center; font-size:15px; margin-bottom:32px;">
                요청하신 이메일 인증을 위한 인증키를 발급해드립니다.
            </p>
           <div style="font-family:'Pretendard','Noto Sans KR',Arial,sans-serif; color:#222; text-align:center; padding:40px 0;">
                  <span style="font-size:38px; font-weight:700; letter-spacing:4px;">%s</span>
            </div>
            <p style="text-align:center; font-size:13px; color:#666; margin-bottom:6px;">
                인증키를 인증화면에 직접 입력하시거나<br>
                인증키를 복사 후 해당기기에 붙여넣어 주세요.
            </p>
            <p style="text-align:center; font-size:11px; color:#aaa;">
                본 메일은 발신전용이며 문의사항은 홈페이지를 참고해 주세요.
            </p>
        </div>
        """.formatted(authCode);

    message.setText(body, "UTF-8", "html");
    log.debug("이메일 본문 생성됨: email={}, code={}", mail, authCode);
    return message;
  }

  // 메일 발송
  public boolean sendSimpleMessage(String sendEmail) throws MessagingException {
    long count = getEmailRequestCount(sendEmail);

    int checkCnt = memberService.findMemberByMemberEmail(sendEmail);
    if (checkCnt != 0) {
      throw new BusinessException(ErrorCode.UNABLE_TO_SEND_EMAIL);
    }

    if (count == 5) {
      log.warn("이메일 인증 요청 횟수 초과: email={}", sendEmail);
      throw new BusinessException(ErrorCode.TOO_MANY_EMAIL_VERIFICATION_REQUESTS);
    }
    String authCode = createCode();
    log.info("인증 코드 생성 및 저장: email={}", sendEmail);

    MimeMessage message = createMail(sendEmail, authCode);
    saveVerificationCode(sendEmail, authCode);
    increaseEmailRequestCount(sendEmail);

    try {
      javaMailSender.send(message);
      log.info("이메일 발송 성공: email={}", sendEmail);
      return true;
    } catch (MailException e) {
      log.error("이메일 발송 실패: email={}, error={}", sendEmail, e.getMessage());
      return false;
    }
  }

  // 이메일 인증
  public void verificationEmail(String code, String savedCode) {
    if (!code.equals(savedCode)) {
      log.info("이메일 인증 실패: 입력 코드={}, 저장 코드={}", code, savedCode);
      throw new BusinessException(ErrorCode.UNABLE_TO_SEND_EMAIL);
    }
    log.info("이메일 인증 성공");
  }

  // redis에서 인증코드 가져오기
  public String getVerificationCode(String email) {
    String code = redisTemplate.opsForValue().get(email);
    log.debug("redis에서 인증코드 조회: email={}, code={}", email, code);
    return code;
  }

  // redis에 인증코드 저장
  public void saveVerificationCode(String email, String code) {
    redisTemplate.opsForValue().set(email, code, 3, TimeUnit.MINUTES);
    log.debug("redis에 인증코드 저장: email={}, code={}", email, code);
  }

  // 이메일 요청 카운트 증가
  public void increaseEmailRequestCount(String email) {
    String key = "email_request_count:" + email;
    long count = redisTemplate.opsForValue().increment(key);
    log.debug("이메일 인증 요청 카운트 증가: email={}, count={}", email, count);

    if (count == 5) {
      redisTemplate.expire(key, 24, TimeUnit.HOURS);
      log.info("이메일 인증 요청 5회 도달, 24시간 제한 시작: email={}", email);
    }
  }

  // 이메일 요청 카운트 가져오기
  public long getEmailRequestCount(String email) {
    String key = "email_request_count:" + email;
    String value = redisTemplate.opsForValue().get(key);
    long count = value != null ? Long.parseLong(value) : 0;
    log.debug("이메일 인증 요청 카운트 조회: email={}, count={}", email, count);
    return count;
  }
}
