package dorakdorak.domain.dosirak.dto.request;

import dorakdorak.domain.dosirak.dto.NutritionDto;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Schema(description = "커스텀 도시락 등록 Request")
public class CustomDosirakRegisterRequest {

  @Schema(description = "도시락 이름", example = "매콤한 허브 고기 도시락")
  private String name;

  @Schema(description = "도시락 이미지 URL", example = "https://cdn.example.com/custom/37-main.jpg")
  private String imageUrl;

  @Schema(description = "가격", example = "5000")
  private Long price;

  @Schema(description = "무게", example = "50")
  private Long weight;

  @Schema(description = "보관방법", example = "'R' = Refrigerated (냉장)" + "'F' = Frozen (냉동)"
      + "'RT' = Room Temperature (상온/실온)")
  private String storageType;

  @ArraySchema(
      schema = @Schema(description = "카테고리", example = "단백질 식단"),
      arraySchema = @Schema(description = "도시락 카테고리 리스트", example = "[\"가성비 식단\", \"고혈압 식단\"]")
  )
  private List<String> categories;

  @Schema(description = "도시락 영양 정보")
  private NutritionDto nutrition;
}
