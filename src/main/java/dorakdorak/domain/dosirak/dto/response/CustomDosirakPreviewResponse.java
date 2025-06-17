package dorakdorak.domain.dosirak.dto.response;

import dorakdorak.domain.dosirak.dto.DosirakGenerationResultDto;
import dorakdorak.domain.dosirak.dto.NutritionDto;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "사용자 응답 기반 도시락 구성 Response")
public class CustomDosirakPreviewResponse {

  public static final String SUCCESS_MESSAGE = "커스텀 도시락 이미지가 성공적으로 생성되었습니다.";

  @Schema(description = "도시락 이름", example = "매콤한 허브 고기 도시락")
  private String name;

  @Schema(description = "도시락 이미지 URL", example = "https://cdn.example.com/custom/37-main.jpg")
  private String imageUrl;

  @Schema(description = "가격", example = "7000")
  private Long price;

  @Schema(description = "무게", example = "420")
  private Long weight;

  @Schema(
      description = "보관방법 ('R': 냉장, 'F': 냉동, 'RT': 상온/실온 중 하나 입력)",
      example = "R"
  )
  private String storageType;

  @Schema(
      description = "도시락 카테고리 리스트",
      example = "[\"가성비 식단\", \"고혈압 식단\"]"
  )
  private List<String> categories;

  @Schema(description = "도시락 영양 정보")
  private NutritionDto nutrition;

  @Schema(description = "요청 결과 메시지", example = "커스텀 도시락 이미지가 성공적으로 생성되었습니다.")
  private String message;


  public CustomDosirakPreviewResponse(DosirakGenerationResultDto dosirakGenerationResultDto,
      String imageUrl) {
    this.name = dosirakGenerationResultDto.getName();
    this.imageUrl = imageUrl;
    this.price = dosirakGenerationResultDto.getPrice();
    this.weight = dosirakGenerationResultDto.getWeight();
    this.storageType = dosirakGenerationResultDto.getStorageType();
    this.nutrition = dosirakGenerationResultDto.getNutrition();
    this.categories = dosirakGenerationResultDto.getCategories();
    this.message = SUCCESS_MESSAGE;
  }
}
