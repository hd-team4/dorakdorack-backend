package dorakdorak.global.util.jwt;

import io.jsonwebtoken.Claims;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class QrTokenProvider {

  private final JwtUtil jwtUtil;

  private static final Duration QR_TOKEN_EXPIRE_TIME = Duration.ofDays(3);

  public String generateQrToken(Long orderId, Long orderItemId, Long memberId) {
    Map<String, Object> claims = new HashMap<>();
    claims.put("orderId", orderId);
    claims.put("orderItemId", orderItemId);
    claims.put("memberId", memberId);
    claims.put("jti", UUID.randomUUID().toString());

    return jwtUtil.generateToken(claims, "qr-token", QR_TOKEN_EXPIRE_TIME);
  }

  public Claims validateQrToken(String token) {
    return jwtUtil.parseToken(token);
  }
}