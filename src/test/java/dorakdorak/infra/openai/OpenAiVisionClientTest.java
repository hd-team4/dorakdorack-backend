package dorakdorak.infra.openai;

import dorakdorak.domain.zeroWaste.service.DosirakRemainAnalyzer;
import dorakdorak.global.error.ErrorCode;
import dorakdorak.global.error.exception.BusinessException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
class OpenAiVisionClientTest {

    @Autowired
    private OpenAiVisionClient openAiVisionClient;

    @Autowired
    private DosirakRemainAnalyzer dosirakRemainAnalyzer;

    @Test
    @DisplayName("잔반 비율이 10% 미만일 경우 - 인증 성공 가능")
    void analyzeRemainPercent_success_under10() {
        String imageUrl = "https://mblogthumb-phinf.pstatic.net/MjAyMjA1MTdfOSAg/MDAxNjUyNzY3NTE4MjE4.2MObntGr4CTFjKgaWIdcvt9v-5LrTGr3HA7ZNv4n3HYg.hbo3TQfuFNmPs_2bfwUwJQitnm5RXR6ITpOA4mf3TGEg.JPEG.arex_blog/SE-f8e6af66-336c-4f05-8898-fdfad91583dc.jpg?type=w800";

        int result = dosirakRemainAnalyzer.analyze(imageUrl);

        System.out.println("잔반 비율 (성공 예상): " + result + "%");
        assertThat(result).isLessThan(10);
    }

    @Test
    @DisplayName("잔반 비율이 10% 이상일 경우 - 인증 실패 기준")
    void analyzeRemainPercent_fail_over10() {
        String imageUrl = "https://the-edit.co.kr/wp-content/uploads/2024/06/DSC01953-1.jpg";

        int result = dosirakRemainAnalyzer.analyze(imageUrl);

        System.out.println("잔반 비율 (실패 예상): " + result + "%");
        assertThat(result).isGreaterThanOrEqualTo(10);
    }

    @Test
    @DisplayName("숫자가 아닌 응답이 올 경우 예외 발생")
    void analyzeRemainPercent_invalidFormat() {
        String imageUrl = "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQVf42uP2Ekie-cvONyaTOomc1QCQczzQ_M7Q&s";

        assertThatThrownBy(() -> dosirakRemainAnalyzer.analyze(imageUrl))
            .isInstanceOf(BusinessException.class)
            .satisfies(e -> {
                BusinessException be = (BusinessException) e;
                assertThat(be.getErrorCode()).isEqualTo(ErrorCode.OPENAI_RESULT_PARSING_FAILED);
            });
    }

    @Test
    @DisplayName("존재하지 않는 이미지일 경우 예외 발생")
    void analyzeRemainPercent_notFoundImage() {
        String imageUrl = "https://your-s3-bucket.s3.amazonaws.com/non-existent-image.jpg";

        assertThatThrownBy(() -> dosirakRemainAnalyzer.analyze(imageUrl))
            .isInstanceOf(BusinessException.class)
            .satisfies(e -> {
                BusinessException be = (BusinessException) e;
                assertThat(be.getErrorCode()).isEqualTo(ErrorCode.OPENAI_API_CALL_FAILED);
            });
    }
}
