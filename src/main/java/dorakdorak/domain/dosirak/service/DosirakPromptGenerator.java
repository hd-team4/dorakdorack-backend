package dorakdorak.domain.dosirak.service;

import dorakdorak.domain.dosirak.dto.DosirakGenerationResultDto;
import dorakdorak.domain.dosirak.dto.response.CustomDosirakPreviewResponse;
import dorakdorak.infra.openai.OpenAiVisionClient;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DosirakPromptGenerator {

  private static final Logger log = LoggerFactory.getLogger(DosirakPromptGenerator.class);
  private final OpenAiVisionClient openAiVisionClient;

  public CustomDosirakPreviewResponse generateCustomInfo(List<String> answers) {

    DosirakGenerationResultDto dosirakGenerationResultDto = generateDosirakText(answers);

    log.info("DosirakGenerationResultDto () : {}", dosirakGenerationResultDto.toString());

    String prompt = dosirakGenerationResultDto.getPrompt();
    String imageUrl = openAiVisionClient.generateCustomImage("/images/generations", prompt);

    return new CustomDosirakPreviewResponse(dosirakGenerationResultDto, imageUrl);
  }

  public DosirakGenerationResultDto generateDosirakText(List<String> answers) {
    String joined = String.join("\n", answers);
    String prompt = """
        다음은 사용자의 도시락 선호 답변입니다:
        %s

        1. 이 선호에 따라 어울릴 수 있는 한식, 양식, 일식, 중식 중 **'풍성한 메인 메뉴 중심'**의 도시락을 상상해서 메뉴를 구성해주세요. \s
           - 메인 메뉴는 사용자 도시락 선호 기준으로 두 가지이며, **중량감 있고 시각적으로도 **가득 차 보이게** 구성합니다. \s
           - 반찬은 반드시 **1~2개 이하**로만 구성해야 하며, **절대 메인 메뉴보다 수량이 많거나 도시락 내에서 차지하는 면적이 커서는 안 됩니다.**
           - 사용자의 도시락 선호 답변에 "현재 사용자가 가지고 있는 알러지"가 포함되어 있으면 알러지를 고려한 메뉴를 구성해주셔야 합니다.
           - 반찬은 **작고 소량**이어야 하며, **메인 메뉴의 색감과 풍미를 보완하는 역할**만 수행합니다.
           - 밥은 무조건 흰쌀밥으로 1개 넣어주셔야 합니다.
           - 도시락 통은 무조건 검은색입니다.
           - 메인 메뉴는 매번 새로운 조합이어야 하며, 절대 같은 재료를 반복하거나 예시를 그대로 복붙하면 안 됩니다.
           - Included are ~~ 예시는 제거하고, “imagine based on the user’s preferences”와 같은 문장으로 대체하세요.
           - 사용자 응답 내용을 실제 조합에 반영하라는 가이드를 명시하세요.
           - 도시락 이름은 매번 새롭게 만들어야 하며, 이전에 등장한 이름과 중복되지 않아야 합니다.
           - 단순한 재료 나열은 피하고, MZ세대가 좋아할 트렌디하고 위트 있는 이름을 만들어 주세요.

        2. 수많은 메뉴 후보 중 하나를 선택해, 다음 JSON 형식으로 도시락 구성과 영양 정보를 작성하세요.

        3. 카테고리는 고혈압 식단, 칼로리 식단, 스페셜 식단, 단백질 식단, 당뇨 식단, 가성비 식단 중에서 도시락에 맞는 항목을 2개 이상 작성하세요. 식단 이외에 아무것도 들어가면 안됩니다.
           - 2개 이상이므로 항상 2개만 주면 안됩니다. 메뉴 구성에 맞게 카테고리를 잘 선정해주세요.  
           - "categories"는 반드시 배열(JSON array) 형식으로 작성하세요. 예: ["단백질 식단", "스페셜 식단"]
           
        4. storageType은 보관 방법으로 'R' = Refrigerated (냉장), 'F' = Frozen (냉동), 'RT' = Room Temperature (상온/실온) 을 의미해. 보관 방법도 메뉴 구성에 맞게 잘 선정해주세요.
                

        5. 아래 JSON 구조 외에 어떤 텍스트도 출력하지 마세요. 설명, 안내 문구, 형식 표시 없이 순수 JSON만 반환하세요.

        ※ 주의: 메뉴 구성은 반드시 너가 상상해서 만든 조합이어야 하며, 그 구성 요소가 이미지로 그려질 수 있도록 prompt에 전부 포함되어야 합니다.

        {
          "name": "도시락 이름 (한글, MZ세대 감성)",
          "price": 5000,
          "weight": 400,
          "storageType" : 'R',
          "categories": ["단백질 식단", "스페셜 식단"],
          "nutrition": {
            "calories": 612.4,
            "carbohydrates": 40.0,
            "sugars": 5.2,
            "protein": 45.6,
            "cholesterol": 72.0,
            "fat": 20.3,
            "transFat": 0.3
          },
          "prompt":"Look at the text and draw only a packed lunch that consists of a menu, as shown in the front. The packed lunch ratio is 6 x 4, the lunch box is only black, and the packed lunch is centrally located.
                    
                    Included are **soy sauce beef ribs and charcoal-grilled chicken breasts** as the main dishes, with **stir-fried garlic leeks and sesame spinach** as side dishes, and **pumpkin seed black rice**.
                    
                    The texture should be matte, with no gloss or glossy highlights, and soft, diffuse natural daylight should be applied. \s
                    **The visual style must be hyper-realistic, resembling an actual photo of a real lunch box.** \s
                    **Avoid any illustration, cartoon, anime, 3D render, or painting-like style. This image must look exactly like it was taken with a real camera in a real-life setting.**
                    
                    **The lunch box must be shot from a top-down (bird's eye) angle, as if taken directly from above. It should be clearly framed and centered, ideal for use as a clean thumbnail.**
                    
                    The food should look like it was photographed with a regular camera during the day, without artificial lighting or unrealistic reflections. \s
                    The image must not contain any text, brand names, logos, or writing on or around the lunch box. \s
                    The background must be only a single soft solid color that complements the lunch box. \s
                    The final image must contain nothing but the packed lunch and a matching good plain background color.
                    "
        }
        """.replace("\n", "\\n").formatted(joined);

    log.info("generateDosirakText () :: {}", prompt);
    return openAiVisionClient.chatWithTextToJson(prompt);
  }


}
