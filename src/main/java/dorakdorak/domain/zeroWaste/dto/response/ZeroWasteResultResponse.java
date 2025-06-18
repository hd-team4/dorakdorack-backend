package dorakdorak.domain.zeroWaste.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ZeroWasteResultResponse {
  private String status;           // "ACCEPTED" or "REJECTED"
  private int remainPercentage;   // 0~100
  private String message;         // 사용자 메시지
}