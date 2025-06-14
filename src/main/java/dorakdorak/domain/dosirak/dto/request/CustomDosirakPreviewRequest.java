package dorakdorak.domain.dosirak.dto.request;

import io.swagger.v3.oas.annotations.media.ArraySchema;
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

  @Schema(description = "도시락 구성 시 가장 중요하게 생각하는 메인 요소", example = "생선이나 해산물도 좋아요")
  private String mainPreference;

  @Schema(description = "음식의 중요한 감각 요소", example = "다양한 식감 (바삭, 쫄깃, 부드러움) 조합")
  private String importantSense;

  @Schema(description = "식사 양", example = "가볍게 다이어트")
  private String mealAmount;

  @Schema(description = "당기는 맛", example = "짭짤한 간장/소금기반")
  private String cravingFlavor;
  @ArraySchema(
      schema = @Schema(description = "알레르기 정보", example = "새우"),
      arraySchema = @Schema(description = "알레르기 항목 리스트", example = "[\"새우\", \"땅콩\"]")
  )
  private List<String> allergies;

}
