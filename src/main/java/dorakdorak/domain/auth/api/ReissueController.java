package dorakdorak.domain.auth.api;

import dorakdorak.domain.auth.jwt.JWTUtil;
import dorakdorak.domain.auth.jwt.RedisRefreshToken;
import dorakdorak.domain.auth.service.RedisRefreshTokenService;
import dorakdorak.domain.member.service.MemberService;
import dorakdorak.global.error.ErrorCode;
import dorakdorak.global.error.exception.BusinessException;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class ReissueController {

  private final JWTUtil jwtUtil;

  private final MemberService memberService;

  private final RedisRefreshTokenService redisRefreshTokenService;
  private final static String BEARER = "Bearer ";

  @PostMapping("/reissue")
  public ResponseEntity<?> reissue(HttpServletRequest request, HttpServletResponse response) {

    // get refresh token
    String refresh = null;
    Cookie[] cookies = request.getCookies();
    for (Cookie cookie : cookies) {

      if (cookie.getName().equals("refresh")) {
        refresh = cookie.getValue();
      }
    }

    if (refresh == null) {

      // response status code
//      return new ResponseEntity<>("refresh token null", HttpStatus.BAD_REQUEST);
      throw new BusinessException(ErrorCode.INVALID_TOKEN);
    }

    // expired check
    try {
      jwtUtil.isExpired(refresh);
    } catch (ExpiredJwtException e) {

      // response status code
//      return new ResponseEntity<>("refresh token expired", HttpStatus.BAD_REQUEST);
      throw new BusinessException(ErrorCode.INVALID_TOKEN);
    }

    // 토큰이 refresh인지 확인 (발급시 페이로드에 명시)
    String category = jwtUtil.getCategory(refresh);

    if (!category.equals("refresh")) {

      // response status code
//      return new ResponseEntity<>("invalid refresh token", HttpStatus.BAD_REQUEST);
      throw new BusinessException(ErrorCode.INVALID_TOKEN);
    }

    // DB에 저장되어 있는지 확인
    Boolean isExist = redisRefreshTokenService.existsByRefresh(refresh);
    if (!isExist) {
      // response body
//      return new ResponseEntity<>("invalid refresh token", HttpStatus.BAD_REQUEST);
      throw new BusinessException(ErrorCode.INVALID_TOKEN);
    }

    String email = jwtUtil.getEmail(refresh);
    String role = jwtUtil.getRole(refresh);
    long id = jwtUtil.getId(refresh);

    // make new JWT
    String newAccess = jwtUtil.createJwt("Authorization", id, email, role, 900000L);
    String newRefresh = jwtUtil.createJwt("refresh", id, email, role, 86400000L);

    // DB에 기존의 Refresh 토큰 삭제
    redisRefreshTokenService.deleteByRefresh(refresh);

    // response
    response.setHeader("Authorization", BEARER + newAccess);
    response.addCookie(createCookie(email, "refresh", newRefresh));
    return new ResponseEntity<>(HttpStatus.OK);
  }

  private Cookie createCookie(String email, String key, String value) {
    // 새 Refresh 토큰 저장
    redisRefreshTokenService.save(new RedisRefreshToken(email, value));
    // refresh 토큰 DB에 저장
    memberService.updateMemberRefreshToken(email, value);
    Cookie cookie = new Cookie(key, value);
    cookie.setMaxAge(24 * 60 * 60);
    cookie.setMaxAge(24 * 60 * 60);
    //cookie.setSecure(true);
    cookie.setPath("/");
    cookie.setHttpOnly(true);
    return cookie;
  }
}