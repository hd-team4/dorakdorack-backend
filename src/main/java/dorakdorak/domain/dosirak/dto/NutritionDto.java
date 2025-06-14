package dorakdorak.domain.dosirak.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "도시락 영양 정보 DTO")
public class NutritionDto {

  @Schema(description = "칼로리 (kcal)", example = "650.0")
  private Double calories;

  @Schema(description = "탄수화물 (g)", example = "50.0")
  private Double carbohydrates;

  @Schema(description = "당류 (g)", example = "6.5")
  private Double sugars;

  @Schema(description = "단백질 (g)", example = "55.0")
  private Double protein;

  @Schema(description = "콜레스테롤 (mg)", example = "85.0")
  private Double cholesterol;

  @Schema(description = "지방 (g)", example = "22.0")
  private Double fat;

  @Schema(description = "트랜스지방 (g)", example = "0.5")
  private Double transFat;
}
