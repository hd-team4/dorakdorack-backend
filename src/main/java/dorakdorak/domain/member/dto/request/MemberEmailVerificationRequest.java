package dorakdorak.domain.member.dto.request;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "이메일 인증 요청")
public class MemberEmailVerificationRequest {

  @Schema(description = "사용자 이메일", example = "dorak@naver.com", required = true)
  private String email;

  @Schema(description = "인증 코드", example = "1H3V56", required = true)
  private String code;

}
