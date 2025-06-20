package dorakdorak.domain.auth.filter;

import dorakdorak.domain.auth.jwt.JWTUtil;
import dorakdorak.domain.auth.service.RedisRefreshTokenService;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;

@Component
public class CustomLogoutFilter extends GenericFilterBean {

  private final JWTUtil jwtUtil;
  private final RedisRefreshTokenService refreshRepository;
  private final AntPathRequestMatcher requestMatcher;

  public CustomLogoutFilter(JWTUtil jwtUtil, RedisRefreshTokenService refreshRepository) {
    super();
    this.requestMatcher = new AntPathRequestMatcher("/api/auth/logout", "POST");
    this.jwtUtil = jwtUtil;
    this.refreshRepository = refreshRepository;
  }

  @Override
  public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
      throws IOException, ServletException {

    doFilter((HttpServletRequest) request, (HttpServletResponse) response, chain);
  }

  private void doFilter(HttpServletRequest request, HttpServletResponse response,
      FilterChain filterChain) throws IOException, ServletException {

    if (!requestMatcher.matches(request)) {
      filterChain.doFilter(request, response);
      return;
    }

    String requestMethod = request.getMethod();
    if (!requestMethod.equals("POST")) {

      filterChain.doFilter(request, response);
      return;
    }

    // get refresh token
    String refresh = null;
    Cookie[] cookies = request.getCookies();
    if (cookies != null) {
      for (Cookie cookie : cookies) {

        if (cookie.getName().equals("refresh")) {

          refresh = cookie.getValue();
        }
      }
    }

    // refresh null check
    if (refresh == null) {

      response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
      return;
    }

    // expired check
    try {
      jwtUtil.isExpired(refresh);
    } catch (ExpiredJwtException e) {

      // response status code
      response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
      return;
    }

    // 토큰이 refresh인지 확인 (발급시 페이로드에 명시)
    String category = jwtUtil.getCategory(refresh);
    if (!category.equals("refresh")) {

      //response status code
      response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
      return;
    }

    // DB에 저장되어 있는지 확인
    Boolean isExist = refreshRepository.existsByRefresh(refresh);
    if (!isExist) {

      // response status code
      response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
      return;
    }

    // 로그아웃 진행
    // Refresh 토큰 redis에서 제거
    refreshRepository.deleteByRefresh(refresh);

    // Refresh 토큰 Cookie 값 0
    Cookie cookie = new Cookie("refresh", null);
    cookie.setMaxAge(0);
    cookie.setPath("/");
    response.addCookie(cookie);
    response.setStatus(HttpServletResponse.SC_OK);
  }
}
