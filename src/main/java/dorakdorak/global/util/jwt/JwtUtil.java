package dorakdorak.global.util.jwt;

import dorakdorak.global.error.ErrorCode;
import dorakdorak.global.error.exception.InvalidValueException;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import java.nio.charset.StandardCharsets;
import javax.crypto.SecretKey;

import javax.crypto.spec.SecretKeySpec;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.Date;
import java.util.Map;

@Component
public class JwtUtil {

  private static final String BEARER = "Bearer ";

  @Value("${spring.jwt.secret}")
  private String secretKey;

  public String generateToken(Map<String, Object> claims, String subject, Duration expiration) {
    long now = System.currentTimeMillis();

    return Jwts.builder()
        .claims(claims)
        .subject(subject)
        .issuedAt(new Date(now))
        .expiration(new Date(now + expiration.toMillis()))
        .signWith(getSigningKey())
        .compact();
  }

  public Claims parseToken(String token) {
    try {
      return Jwts.parser()
          .verifyWith(getSigningKey())
          .build()
          .parseSignedClaims(getRawToken(token))
          .getPayload();
    } catch (JwtException | IllegalArgumentException e) {
      throw new InvalidValueException(ErrorCode.INVALID_TOKEN.getMessage(), ErrorCode.INVALID_TOKEN);
    }
  }

  public void validateToken(String token) {
    parseToken(token); // 검증 실패 시 예외 발생
  }

  public String getSubject(String token) {
    return parseToken(token).getSubject();
  }

  public String getRawToken(String token) {
    if (token == null || token.isBlank()) {
      throw new InvalidValueException(ErrorCode.INVALID_TOKEN.getMessage(), ErrorCode.INVALID_TOKEN);
    }

    if (token.startsWith(BEARER)) {
      return token.substring(BEARER.length());
    }
    return token;
  }

  private SecretKey getSigningKey() {
    return new SecretKeySpec(secretKey.getBytes(StandardCharsets.UTF_8),
        Jwts.SIG.HS256.key().build().getAlgorithm());

    // TODO secret Key Decode
//    return Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretKey));
  }
}
