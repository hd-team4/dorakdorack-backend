package dorakdorak.domain.auth.api;

import dorakdorak.domain.auth.jwt.JWTUtil;
import dorakdorak.domain.auth.jwt.RedisRefreshToken;
import dorakdorak.domain.auth.service.RedisRefreshTokenService;
import dorakdorak.domain.member.service.MemberService;
import dorakdorak.global.error.ErrorCode;
import dorakdorak.global.error.exception.BusinessException;
import io.jsonwebtoken.ExpiredJwtException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@Tag(name = "reissue", description = "리프레시 토큰 재발급 컨트롤러")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class ReissueController {

  private final JWTUtil jwtUtil;

  private final MemberService memberService;

  private final RedisRefreshTokenService redisRefreshTokenService;
  private final static String BEARER = "Bearer ";

  @Operation(
      summary = "JWT 토큰 재발급",
      description = "Refresh 토큰을 기반으로 새로운 Access/Refresh 토큰을 재발급합니다. \n\n" +
          "- Refresh 토큰은 Cookie에 `refresh` 키로 포함되어 있어야 합니다.\n" +
          "- 성공 시 새로운 Access 토큰은 `Authorization` 헤더에 포함되며, Refresh 토큰은 다시 Cookie로 설정됩니다.",
      responses = {
          @ApiResponse(responseCode = "200", description = "재발급 성공"),
          @ApiResponse(responseCode = "401", description = "Refresh 토큰이 없거나 만료/유효하지 않음"),
          @ApiResponse(responseCode = "500", description = "서버 에러")
      }
  )
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
      throw new BusinessException(ErrorCode.REFRESH_TOKEN_MISSING);
    }

    // expired check
    try {
      jwtUtil.isExpired(refresh);
    } catch (ExpiredJwtException e) {

      // response status code
//      return new ResponseEntity<>("refresh token expired", HttpStatus.BAD_REQUEST);
      throw new BusinessException(ErrorCode.REFRESH_TOKEN_EXPIRED);
    }

    // 토큰이 refresh인지 확인 (발급시 페이로드에 명시)
    String category = jwtUtil.getCategory(refresh);

    if (!category.equals("refresh")) {

      // response status code
//      return new ResponseEntity<>("invalid refresh token", HttpStatus.BAD_REQUEST);
      throw new BusinessException(ErrorCode.REFRESH_TOKEN_INVALID_CATEGORY);
    }

    // DB에 저장되어 있는지 확인
    Boolean isExist = redisRefreshTokenService.existsByRefresh(refresh);
    if (!isExist) {
      // response body
//      return new ResponseEntity<>("invalid refresh token", HttpStatus.BAD_REQUEST);
      throw new BusinessException(ErrorCode.REFRESH_TOKEN_NOT_FOUND);
    }

    String email = jwtUtil.getEmail(refresh);
    String role = jwtUtil.getRole(refresh);
    long id = jwtUtil.getId(refresh);
    Long uid = jwtUtil.getUid(refresh);

    // make new JWT
    String newAccess = jwtUtil.createJwt("Authorization", id, uid, email, role, 900000L);
    String newRefresh = jwtUtil.createJwt("refresh", id, uid, email, role, 86400000L);

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
    cookie.setSecure(true);
    cookie.setPath("/");
    cookie.setHttpOnly(true);
    return cookie;
  }
}