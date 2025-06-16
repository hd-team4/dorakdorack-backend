package dorakdorak.domain.member.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "로그인 요청")
public class MemberLoginRequest {

  @Schema(description = "사용자 email", example = "kosa@kosa.com")
  private String email;

  @Schema(description = "비밀번호", example = "password123")
  private String password;
}
