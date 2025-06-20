package dorakdorak.domain.auth.jwt;

import io.jsonwebtoken.Jwts;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class JWTUtil {


  private SecretKey secretKey;

  public JWTUtil(@Value("${spring.jwt.secret}") String secret) {

    secretKey = new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8),
        Jwts.SIG.HS256.key().build().getAlgorithm());
  }

  public String getEmail(String token) {

    return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload()
        .get("email", String.class);
  }

  public String getRole(String token) {

    return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload()
        .get("role", String.class);
  }

  public String getCategory(String token) {
    return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload()
        .get("category", String.class);
  }

  public long getId(String token) {
    Number idValue = Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token)
        .getPayload()
        .get("id", Number.class);
    return idValue.longValue();
  }

  public long getUid(String token) {
    Number idValue = Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token)
        .getPayload()
        .get("uid", Number.class);
    return idValue.longValue();
  }


  public Boolean isExpired(String token) {

    return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload()
        .getExpiration().before(new Date());
  }

  public String createJwt(String category, Long id, Long uid, String email, String role,
      Long expiredMs) {

    return Jwts.builder()
        .claim("category", category)
        .claim("email", email)
        .claim("id", id)
        .claim("uid", uid)
        .claim("role", role)
        .issuedAt(new Date(System.currentTimeMillis()))
        .expiration(new Date(System.currentTimeMillis() + expiredMs))
        .signWith(secretKey)
        .compact();
  }
}
