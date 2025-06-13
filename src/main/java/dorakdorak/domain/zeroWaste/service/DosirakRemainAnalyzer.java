package dorakdorak.domain.zeroWaste.service;

import dorakdorak.global.error.ErrorCode;
import dorakdorak.global.error.exception.BusinessException;
import dorakdorak.infra.openai.OpenAiVisionClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class DosirakRemainAnalyzer {

  private final OpenAiVisionClient openAiVisionClient;

  private static final String PROMPT = "이 도시락 사진을 보고 남은 음식의 양을 백분율로 추정해줘. 숫자만 정확히 하나만, 예: '8' 같은 형식으로 응답해. 단위나 설명 없이 숫자만 줘.";

  public int analyze(String imageUrl) {
    try {
      String result = openAiVisionClient.chatWithImage(PROMPT, imageUrl);
      return Integer.parseInt(result.trim());
    } catch (NumberFormatException e) {
      log.error("잔반 비율 숫자 파싱 실패: {}", e.getMessage(), e);
      throw new BusinessException(ErrorCode.OPENAI_RESULT_PARSING_FAILED);
    }
  }
}
