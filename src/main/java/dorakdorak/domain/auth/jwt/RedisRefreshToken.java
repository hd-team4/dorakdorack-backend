package dorakdorak.domain.auth.jwt;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

@RedisHash(value = "refreshToken")
@Getter
@AllArgsConstructor
@Setter
public class RedisRefreshToken {

  private String email;
  @Indexed
  private String refresh;

  protected RedisRefreshToken() {
  }
}