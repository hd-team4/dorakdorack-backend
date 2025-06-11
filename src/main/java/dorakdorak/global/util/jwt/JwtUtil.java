package dorakdorak.global.util.jwt;

import dorakdorak.global.error.ErrorCode;
import dorakdorak.global.error.exception.InvalidValueException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import java.time.Duration;
import java.util.Date;
import java.util.Map;
import javax.crypto.SecretKey;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class JwtUtil {

  private static final String BEARER = "Bearer ";

  @Value("${jwt.secret-key}")
  private String secretKey;

  public String generateToken(Map<String, Object> claims, String subject, Duration expiration) {
    long now = System.currentTimeMillis();

    return Jwts.builder()
        .setClaims(claims)
        .setSubject(subject)
        .setIssuedAt(new Date(now))
        .setExpiration(new Date(now + expiration.toMillis()))
        .signWith(getSigningKey(), SignatureAlgorithm.HS256)
        .compact();
  }

  public Claims parseToken(String token) {
    try {
      return Jwts.parserBuilder()
          .setSigningKey(getSigningKey())
          .build()
          .parseClaimsJws(getRawToken(token))
          .getBody();
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
    return Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretKey));
  }
}
