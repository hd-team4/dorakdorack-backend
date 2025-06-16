package dorakdorak.domain.auth.jwt;

import dorakdorak.domain.auth.security.CustomMemberDetails;
import dorakdorak.domain.auth.dto.MemberAuthDto;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

@Slf4j
@RequiredArgsConstructor
public class JWTFilter extends OncePerRequestFilter {

  private final JWTUtil jwtUtil;

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
      FilterChain filterChain) throws ServletException, IOException {

    // 헤더에서 access키에 담긴 토큰을 꺼냄
    String accessToken = request.getHeader("Authorization");

    // 토큰이 없다면 다음 필터로 넘김
    if (accessToken == null) {

      filterChain.doFilter(request, response);

      return;
    }

    if (accessToken.startsWith("Bearer ")) {
      accessToken = accessToken.substring(7);
    }
    // 토큰 만료 여부 확인, 만료시 다음 필터로 넘기지 않음
    try {
      jwtUtil.isExpired(accessToken);
    } catch (ExpiredJwtException e) {

      //response body
      PrintWriter writer = response.getWriter();
      writer.print("access token expired");

      //response status code
      response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
      return;
    }

    // 토큰이 access인지 확인 (발급시 페이로드에 명시)
    String category = jwtUtil.getCategory(accessToken);

    if (!category.equals("Authorization")) {

      //response body
      PrintWriter writer = response.getWriter();
      writer.print("invalid access token");

      //response status code
      response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
      return;
    }

    // email, role 값을 획득
    String email = jwtUtil.getEmail(accessToken);
    String role = jwtUtil.getRole(accessToken);
    Long id = jwtUtil.getId(accessToken);
    Long uid = jwtUtil.getUid(accessToken);

    MemberAuthDto memberAuthData = new MemberAuthDto(id, uid, email, "dkanrkskfsdlk", role);

    CustomMemberDetails customUserDetails = new CustomMemberDetails(memberAuthData);

    Authentication authToken = new UsernamePasswordAuthenticationToken(customUserDetails, null,
        customUserDetails.getAuthorities());

    SecurityContextHolder.getContext().setAuthentication(authToken);

    filterChain.doFilter(request, response);
  }
}
