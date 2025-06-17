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
@Schema(description = "회원가입 응답")
public class MemberSignupResponse {

  @Schema(description = "응답 상태", example = "success")
  private String status;
  
  @Schema(description = "응답 메시지", example = "회원가입 성공")
  private String message;

}
