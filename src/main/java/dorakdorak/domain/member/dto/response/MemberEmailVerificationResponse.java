package dorakdorak.domain.member.dto.response;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "이메일 인증 결과 응답")
public class MemberEmailVerificationResponse {

  @Schema(description = "응답 상태", example = "success")
  private String status;

  @Schema(description = "응답 메시지", example = "이메일 인증 성공")
  private String message;

}
