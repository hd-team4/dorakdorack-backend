package dorakdorak.infra.openai;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import dorakdorak.domain.dosirak.dto.DosirakGenerationResultDto;
import dorakdorak.global.error.ErrorCode;
import dorakdorak.global.error.exception.BusinessException;
import dorakdorak.infra.openai.dto.OpenAiResponse;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;


@Slf4j
@Component
@RequiredArgsConstructor
public class OpenAiVisionClient {

  @Value("${openai.api-key}")
  private String apiKey;

  @Value("${openai.dalle-api-key}")
  private String dalleApiKey;

  private final WebClient webClient = WebClient.builder()
      .baseUrl("https://api.openai.com/v1")
      .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
      .build();

  /**
   * 이미지와 프롬프트를 기반으로 OpenAI GPT 모델에게 요청을 보내고, 응답 텍스트(예: 숫자 추정, 분석 결과 등)를 반환
   *
   * @param prompt   GPT 모델에게 전달할 프롬프트 텍스트
   * @param imageUrl 분석에 사용할 이미지의 URL
   * @return GPT 응답에서 추출한 텍스트 (예: 숫자, 분석 결과 등)
   * @throws BusinessException OpenAI 응답 오류 또는 파싱 실패 시 예외 발생
   *                           <p>
   *                           temperature: 응답의 창의성과 다양성을 조절하는 값. 0.0에 가까울수록 예측 가능한 응답, 1.0에 가까울수록
   *                           창의적인 응답이 생성 max_tokens: 응답 텍스트의 최대 길이 temperature와 maxTokens는 향후 필요에
   *                           따라 조절 가능
   */
  public String chatWithImage(String prompt, String imageUrl) {
    Map<String, Object> requestBody = createImageRequestBody(prompt, imageUrl, 0.2, 50);
    return getContent("/chat/completions", requestBody);
  }

  /**
   * 순수 텍스트 프롬프트를 기반으로 OpenAI GPT 모델에게 요청을 보내고, 생성된 응답 텍스트(예: 도시락 구성, 설명 등)를 반환
   *
   * @param prompt GPT 모델에게 전달할 텍스트 프롬프트
   * @return GPT 응답에서 추출한 JSON 결과
   * @throws BusinessException OpenAI 응답 오류 또는 파싱 실패 시 예외 발생
   *                           <p>
   *                           temperature: 응답의 창의성과 다양성을 조절하는 값. 0.0에 가까울수록 예측 가능한 응답, 1.0에 가까울수록
   *                           창의적인 응답이 생성 max_tokens: 응답 텍스트의 최대 길이 temperature와 maxTokens는 향후 필요에
   *                           따라 조절 가능
   */
  public DosirakGenerationResultDto chatWithTextToJson(String prompt) {
    Map<String, Object> requestBody = createTextRequestBody(prompt, 1.2, 900);
    return getJsonContent("/chat/completions", requestBody);
  }

  private DosirakGenerationResultDto getJsonContent(String uri, Map<String, Object> requestBody) {
    try {
      OpenAiResponse response = webClient.post()
          .uri(uri)
          .header(HttpHeaders.AUTHORIZATION, "Bearer " + apiKey)
          .bodyValue(requestBody)
          .retrieve()
          .bodyToMono(OpenAiResponse.class)
          .block();

      if (response == null || response.getChoices() == null || response.getChoices().isEmpty()) {
        log.error("OpenAI 응답이 null이거나 choices가 비어 있음: {}", response);
        throw new BusinessException(ErrorCode.OPENAI_RESPONSE_ERROR);
      }

      String content = response.getChoices().get(0).getMessage().getContent();
      content = content.replaceAll("(?i)```json", "").replaceAll("```", "").trim();

      if (content == null || content.isBlank()) {
        log.error("OpenAI 응답 content가 비어 있습니다: {}", response);
        throw new BusinessException(ErrorCode.OPENAI_RESPONSE_ERROR);
      }

      ObjectMapper objectMapper = new ObjectMapper();
      return objectMapper.readValue(content, DosirakGenerationResultDto.class);

    } catch (JsonProcessingException e) {
      log.error("JSON 파싱 오류: {}", e.getMessage(), e);
      throw new BusinessException(ErrorCode.JSON_PARSE_ERROR);
    } catch (WebClientResponseException e) {
      log.error("OpenAI API 호출 실패 - 상태 코드: {}, 응답 본문: {}", e.getRawStatusCode(),
          e.getResponseBodyAsString(), e);
      throw new BusinessException(ErrorCode.OPENAI_API_CALL_FAILED);
    } catch (Exception e) {
      log.error("OpenAI API 호출 중 예외 발생: {}", e.getMessage(), e);
      throw new BusinessException(ErrorCode.OPENAI_API_CALL_FAILED);
    }
  }


  public String generateCustomImage(String url, String prompt) {
    Map<String, Object> requestBody = createCustomImageRequestBody(prompt);
    try {
      Map<String, Object> response = webClient.post()
          .uri(url)
          .header(HttpHeaders.AUTHORIZATION, "Bearer " + dalleApiKey)
          .bodyValue(requestBody)
          .retrieve()
          .bodyToMono(Map.class)
          .block();

      if (response == null || !response.containsKey("data")) {
        log.error("OpenAI 응답이 null이거나 data가 없음: {}", response);
        throw new RuntimeException("OpenAI 이미지 응답 오류");
      }

      var data = (java.util.List<Map<String, String>>) response.get("data");
      return data.get(0).get("url");

    } catch (WebClientResponseException e) {
      log.error("OpenAI API 호출 실패 - 상태 코드: {}, 응답 본문: {}", e.getRawStatusCode(),
          e.getResponseBodyAsString(), e);
      throw new RuntimeException("OpenAI API 호출 실패");
    } catch (Exception e) {
      log.error("OpenAI 이미지 생성 중 예외 발생: {}", e.getMessage(), e);
      throw new RuntimeException("OpenAI 이미지 처리 실패");
    }
  }

  private String getContent(String uri, Map<String, Object> requestBody) {
    try {
      OpenAiResponse response = webClient.post()
          .uri(uri)
          .header(HttpHeaders.AUTHORIZATION, "Bearer " + apiKey)
          .bodyValue(requestBody)
          .retrieve()
          .bodyToMono(OpenAiResponse.class)
          .block();

      if (response == null) {
        log.error("OpenAI 응답이 null입니다. 요청 본문: {}", requestBody);
        throw new BusinessException(ErrorCode.OPENAI_RESPONSE_ERROR);
      }

      if (response.getChoices() == null || response.getChoices().isEmpty()) {
        log.error("OpenAI 응답 choices가 비어 있습니다. 전체 응답: {}", response);
        throw new BusinessException(ErrorCode.OPENAI_RESPONSE_ERROR);
      }

      String content = response.getChoices().get(0).getMessage().getContent();

      if (content == null || content.isBlank()) {
        log.error("OpenAI 응답 content가 비어 있습니다. 전체 응답: {}", response);
        throw new BusinessException(ErrorCode.OPENAI_RESPONSE_ERROR);
      }

      return content;

    } catch (WebClientResponseException e) {
      log.error("OpenAI API 호출 실패 - 상태 코드: {}, 응답 본문: {}", e.getRawStatusCode(),
          e.getResponseBodyAsString(), e);
      throw new BusinessException(ErrorCode.OPENAI_API_CALL_FAILED);
    } catch (Exception e) {
      log.error("OpenAI API 호출 중 예외 발생: {}", e.getMessage(), e);
      throw new BusinessException(ErrorCode.OPENAI_API_CALL_FAILED);
    }
  }

  private Map<String, Object> createImageRequestBody(String prompt, String imageUrl,
      double temperature, int maxTokens) {
    return Map.of(
        "model", "gpt-4o",
        "messages", List.of(
            Map.of("role", "user", "content", List.of(
                Map.of("type", "text", "text", prompt),
                Map.of("type", "image_url", "image_url", Map.of("url", imageUrl))
            ))
        ),
        "temperature", temperature,
        "max_tokens", maxTokens
    );
  }

  private Map<String, Object> createTextRequestBody(String prompt, double temperature,
      int maxTokens) {
    return Map.of(
        "model", "gpt-4o",
        "messages", List.of(
            Map.of("role", "user", "content", prompt)
        ),
        "temperature", temperature,
        "max_tokens", maxTokens
    );
  }

  private Map<String, Object> createCustomImageRequestBody(String prompt) {
    return Map.of(
        "model", "dall-e-3",
        "prompt", prompt,
        "n", 1,
        "size", "1024x1024"
    );
  }
}