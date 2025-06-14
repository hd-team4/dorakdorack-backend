package dorakdorak.infra.openai;


import static org.assertj.core.api.Assertions.assertThat;

import dorakdorak.domain.dosirak.dto.DosirakGenerationResultDto;
import dorakdorak.domain.dosirak.dto.response.CustomDosirakPreviewResponse;
import dorakdorak.domain.dosirak.service.DosirakPromptGenerator;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class DosirakPromptGeneratorTest {

  @Autowired
  private OpenAiVisionClient openAiVisionClient;

  @Autowired
  private DosirakPromptGenerator dosirakPromptGenerator;

  @Test
  @DisplayName("사용자 응답으로 도시락 정보 및 이미지 생성 - 성공")
  void generateCustomInfo_success() {
    List<String> answers = List.of(
        "고기 위주가 좋아요",
        "다양한 식감 (바삭, 쫄깃, 부드러움) 조합",
        "든든하게 배불리",
        "달콤한 양념"
    );

    CustomDosirakPreviewResponse result = dosirakPromptGenerator.generateCustomInfo(answers);

    assertThat(result.getName()).isNotBlank();
    assertThat(result.getImageUrl()).startsWith("http");
    System.out.println("생성된 도시락 이름: " + result.getName());
    System.out.println("생성된 이미지 URL: " + result.getImageUrl());
    assertThat(result.getCategories()).isNotEmpty();
    assertThat(result.getNutrition().getCalories()).isGreaterThan(0);
  }

  @Test
  @DisplayName("GPT 응답이 정상적으로 파싱되는지 확인")
  void generateDosirakText_parsesValidJsonSuccessfully() {
    List<String> answers = List.of("고기 위주가 좋아요", "달콤한 양념", "든든하게 배불리");

    DosirakGenerationResultDto result = dosirakPromptGenerator.generateDosirakText(answers);

    assertThat(result.getName()).isNotBlank();
    assertThat(result.getCategories()).isNotEmpty();
    assertThat(result.getNutrition()).isNotNull();
    assertThat(result.getPrompt()).contains(
        "The final image must contain nothing but the packed lunch and a matching good plain background color"); // 템플릿 확인
  }

  @Test
  @DisplayName("파싱된 categories가 List 형식인지 확인")
  void generateDosirakText_categoriesAreArray() {
    List<String> answers = List.of("밸런스 잡힌 영양", "다채로운 색감");

    DosirakGenerationResultDto result = dosirakPromptGenerator.generateDosirakText(answers);

    assertThat(result.getCategories()).isInstanceOf(List.class);
    assertThat(result.getCategories()).allMatch(cat -> cat instanceof String);
  }

  @Test
  @DisplayName("영양 정보가 정상 범위인지 확인")
  void generateDosirakText_nutritionInExpectedRange() {
    DosirakGenerationResultDto result = dosirakPromptGenerator.generateDosirakText(
        List.of("든든하게", "고단백"));

    var nutrition = result.getNutrition();

    assertThat(nutrition.getCalories()).isBetween(200.0, 1200.0);
    assertThat(nutrition.getProtein()).isGreaterThan(10.0);
    assertThat(nutrition.getCarbohydrates()).isGreaterThan(0.0);
  }


}
