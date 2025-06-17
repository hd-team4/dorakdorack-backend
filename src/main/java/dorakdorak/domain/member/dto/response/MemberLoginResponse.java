package dorakdorak.domain.member.dto.response;

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
@Schema(description = "로그인 응답")
public class MemberLoginResponse {

  @Schema(description = "요청 처리 결과 상태", example = "SUCCESS")
  private String status;

  @Schema(description = "상세 메시지", example = "로그인 성공")
  private String message;
}
