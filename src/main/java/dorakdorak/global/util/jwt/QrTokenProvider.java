package dorakdorak.global.util.jwt;

import io.jsonwebtoken.Claims;
import java.time.Duration;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class QrTokenProvider {

  private final JwtUtil jwtUtil;

  @Value("${jwt.qr.expiration}")
  private long qrTokenExpireTime;

  public String generateQrToken(Long orderId, Long orderItemId, Long memberId) {
    Map<String, Object> claims = Map.of(
        "orderId", orderId,
        "orderItemId", orderItemId,
        "memberId", memberId
    );

    return jwtUtil.generateToken(claims, "qr-token", Duration.ofMillis(qrTokenExpireTime));
  }

  public Claims validateQrToken(String token) {
    return jwtUtil.parseToken(token);
  }
}