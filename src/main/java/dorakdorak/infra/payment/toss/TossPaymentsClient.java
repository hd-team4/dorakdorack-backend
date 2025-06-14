package dorakdorak.infra.payment.toss;

import dorakdorak.global.error.ErrorCode;
import dorakdorak.global.error.exception.BusinessException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Slf4j
@RequiredArgsConstructor
@Component
public class TossPaymentsClient {

    private static final String BASE_URL = "https://api.tosspayments.com/v1";
    private static final String CONFIRM_PATH = "/payments/confirm";
    private static final String AUTH_PREFIX = "Basic ";
    private static final int CLIENT_ERROR_MIN = 400;
    private static final int CLIENT_ERROR_MAX = 499;
    private static final int SERVER_ERROR_MIN = 500;

    @Value("${toss.secret-key}")
    private String secretKey;

    // WebClient 초기화 (기본 헤더 포함)
    private final WebClient webClient = WebClient.builder()
            .baseUrl(BASE_URL)
            .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
            .build();

    /**
     * 결제 승인 요청
     * @param paymentKey 결제 키
     * @param orderId 주문 ID
     * @param amount 결제 금액
     * @return TossPaymentResponse
     */
    public TossPaymentsResponse confirm(String paymentKey, String orderId, int amount) {
        return webClient.post()
                .uri(CONFIRM_PATH)
                .header(HttpHeaders.AUTHORIZATION, AUTH_PREFIX + encodeSecretKey())
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(Map.of(
                        "paymentKey", paymentKey,
                        "orderId", orderId,
                        "amount", amount
                ))
                .retrieve()
                .onStatus(status -> isClientError(status.value()), this::handleError)
                .onStatus(status -> isServerError(status.value()), this::handleError)
                .bodyToMono(TossPaymentsResponse.class)
                .block();
    }

    // 토스 Secret Key를 Base64로 인코딩
    private String encodeSecretKey() {
        return Base64.getEncoder()
                .encodeToString((secretKey + ":").getBytes(StandardCharsets.UTF_8));
    }

    private boolean isClientError(int status) {
        return status >= CLIENT_ERROR_MIN && status <= CLIENT_ERROR_MAX;
    }

    private boolean isServerError(int status) {
        return status >= SERVER_ERROR_MIN;
    }

    // 오류 처리
    private Mono<Throwable> handleError(ClientResponse response) {
        return response.bodyToMono(TossErrorResponse.class)
                .flatMap(error -> {
                    int status = response.statusCode().value();
                    log.warn("[TOSS ERROR] HTTP {} - {}: {}", status, error.getCode(), error.getMessage());

                    ErrorCode errorCode = isClientError(status)
                            ? ErrorCode.TOSS_CLIENT_ERROR
                            : ErrorCode.TOSS_SERVER_ERROR;
                    return Mono.error(new BusinessException(error.getMessage(), errorCode));
                });
    }
}