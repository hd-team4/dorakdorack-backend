package dorakdorak.infra.openai;

import dorakdorak.infra.openai.dto.OpenAiResponse;
import dorakdorak.infra.openai.exception.OpenAiVisionException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class OpenAiVisionClient {

    private static final String OPENAI_API_URL = "https://api.openai.com/v1/chat/completions";
    private static final String MODEL = "gpt-4o";
    private static final String PROMPT = "이 도시락 사진을 보고 남은 음식의 양을 백분율로 추정해줘. 숫자만 정확히 하나만, 예: '8' 같은 형식으로 응답해. 단위나 설명 없이 숫자만 줘.";
    private static final int MAX_TOKENS = 10;
    private static final double TEMPERATURE = 0.2;

    @Value("${openai.api-key}")
    private String apiKey;

    private final WebClient webClient = WebClient.builder()
            .baseUrl(OPENAI_API_URL)
            .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
            .build();

    public int analyzeDosirakRemainPercent(String imageUrl) {
        Map<String, Object> requestBody = buildRequestBody(imageUrl);

        try {
            OpenAiResponse response = webClient.post()
                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + apiKey)
                    .bodyValue(requestBody)
                    .retrieve()
                    .bodyToMono(OpenAiResponse.class)
                    .block();

            String content = response.getChoices().get(0).getMessage().getContent();
            return Integer.parseInt(content.trim());

        } catch (NumberFormatException e) {
            log.error("OpenAI 응답이 숫자가 아닙니다: {}", e.getMessage());
            throw new OpenAiVisionException("OpenAI가 유효한 숫자를 반환하지 않았습니다.", e);
        } catch (Exception e) {
            log.error("OpenAI Vision API 호출 실패", e);
            throw new OpenAiVisionException("OpenAI Vision 호출 중 오류 발생", e);
        }
    }

    private Map<String, Object> buildRequestBody(String imageUrl) {
        return Map.of(
                "model", MODEL,
                "messages", List.of(
                        Map.of("role", "user", "content", List.of(
                                Map.of("type", "text", "text", PROMPT),
                                Map.of("type", "image_url", "image_url", Map.of("url", imageUrl))
                        ))
                ),
                "max_tokens", MAX_TOKENS,
                "temperature", TEMPERATURE
        );
    }
}
