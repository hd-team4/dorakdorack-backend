package dorakdorak.global.util.jwt;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import dorakdorak.global.error.exception.InvalidValueException;
import io.jsonwebtoken.Claims;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class QrTokenProviderTest {

  @Autowired
  private QrTokenProvider qrTokenProvider;

  @Test
  @DisplayName("QR 토큰 생성 및 검증 성공")
  void generateAndValidate_success() {
    // given
    Long orderId = 1L;
    Long orderItemId = 10L;
    Long memberId = 99L;

    // when
    String token = qrTokenProvider.generateQrToken(orderId, orderItemId, memberId);
    Claims claims = qrTokenProvider.validateQrToken(token);

    // then
    assertThat(claims.getSubject()).isEqualTo("qr-token");
    assertThat(claims.get("orderId", Long.class)).isEqualTo(orderId);
    assertThat(claims.get("orderItemId", Long.class)).isEqualTo(orderItemId);
    assertThat(claims.get("memberId", Long.class)).isEqualTo(memberId);
  }

  @Test
  @DisplayName("잘못된 형식의 토큰 검증 시 예외 발생")
  void invalidToken_fail() {
    // given
    String invalidToken = "this.is.not.valid.jwt";

    // then
    assertThatThrownBy(() -> qrTokenProvider.validateQrToken(invalidToken))
        .isInstanceOf(InvalidValueException.class);
  }
}