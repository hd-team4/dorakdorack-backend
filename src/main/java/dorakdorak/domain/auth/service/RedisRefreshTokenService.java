package dorakdorak.domain.auth.service;

import dorakdorak.domain.auth.jwt.RedisRefreshToken;
import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class RedisRefreshTokenService {

  private final RedisTemplate<String, Object> redisTemplate;
  private static final String KEY_PREFIX = "refreshToken:";

  public void save(RedisRefreshToken token) {
    String key = KEY_PREFIX + token.getRefresh();

    redisTemplate.opsForValue().set(key, token);
    redisTemplate.expire(key, 80400, TimeUnit.SECONDS);
  }

  public Boolean existsByRefresh(String refresh) {
    String key = KEY_PREFIX + refresh;
    return redisTemplate.hasKey(key);
  }

  public RedisRefreshToken findByRefresh(String refresh) {
    String key = KEY_PREFIX + refresh;
    return (RedisRefreshToken) redisTemplate.opsForValue().get(key);
  }

  public void deleteByRefresh(String refresh) {
    String key = KEY_PREFIX + refresh;
    redisTemplate.delete(key);
  }
}
