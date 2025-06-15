package dorakdorak.domain.dosirak.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CustomDosirakVoteResponse {

  @Schema(description = "응답 상태", example = "success")
  private String status;

  @Schema(description = "응답 메시지", example = "투표가 정상적으로 등록되었습니다.")
  private String message;
}
