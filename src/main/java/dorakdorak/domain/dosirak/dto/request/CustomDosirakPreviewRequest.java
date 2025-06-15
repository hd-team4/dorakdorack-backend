package dorakdorak.domain.dosirak.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Schema(description = "사용자의 도시락 메뉴 구성 선호 Request")
public class CustomDosirakPreviewRequest {

  @Schema(description = "고기 or 채소, 뭐가 더 끌리세요?", example = "고가 위주가 좋아요")
  private String mainPreference;

  @Schema(description = "먹을 때 가장 중요한 감각은?", example = "다양한 식감 (바삭, 쫄깃, 부드러움) 조합")
  private String importantSense;

  @Schema(description = "한 끼에 얼마나 든든하게 드실래요?", example = "든든하게 배불리")
  private String mealAmount;

  @Schema(description = "요즘 끌리는 맛은?", example = "매콤한 맛")
  private String cravingFlavor;

  @Schema(
      description = "memberId를 통해 찾은 알러지 리스트(request 필드값 아님)",
      example = "[\"새우\", \"땅콩\"]"
  )
  private List<String> allergies;

}
