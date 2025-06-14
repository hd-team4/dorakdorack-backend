package dorakdorak.domain.auth.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import dorakdorak.domain.auth.dto.response.CustomMemberDetails;
import dorakdorak.domain.auth.dto.response.LoginDto;
import dorakdorak.domain.auth.jwt.JWTUtil;
import dorakdorak.domain.auth.jwt.RedisRefreshToken;
import dorakdorak.domain.auth.service.RedisRefreshTokenService;
import dorakdorak.domain.member.service.MemberService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletInputStream;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.Iterator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.util.StreamUtils;

@Slf4j
public class LoginFilter extends UsernamePasswordAuthenticationFilter {

  private final AuthenticationManager authenticationManager;
  private final JWTUtil jwtUtil;
  private final RedisRefreshTokenService redisRefreshTokenService;
  private final MemberService memberService;
  private final static String BEARER = "Bearer ";

  public LoginFilter(AuthenticationManager authenticationManager, JWTUtil jwtUtil,
      RedisRefreshTokenService redisRefreshTokenService, MemberService memberService) {
    this.authenticationManager = authenticationManager;
    this.jwtUtil = jwtUtil;
    this.redisRefreshTokenService = redisRefreshTokenService;
    this.memberService = memberService;
    setFilterProcessesUrl("/api/auth/login");
  }

  @Override
  public Authentication attemptAuthentication(HttpServletRequest request,
      HttpServletResponse response) throws AuthenticationException {

    LoginDto loginDto = new LoginDto();

    try {
      ObjectMapper objectMapper = new ObjectMapper();
      ServletInputStream inputStream = request.getInputStream();
      String messageBody = StreamUtils.copyToString(inputStream, StandardCharsets.UTF_8);
      loginDto = objectMapper.readValue(messageBody, LoginDto.class);

    } catch (IOException e) {
      throw new RuntimeException(e);
    }

    String username = loginDto.getEmail();
    String password = loginDto.getPassword();

    log.info(username);

    //스프링 시큐리티에서 username과 password를 검증하기 위해서는 token에 담아야 함
    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
        username, password, null);

    //token에 담은 검증을 위한 AuthenticationManager로 전달
    return authenticationManager.authenticate(authToken);
  }

  //로그인 성공시 실행하는 메소드 (여기서 JWT를 발급하면 됨)
  @Override
  protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response,
      FilterChain chain, Authentication authentication) {
    log.info("success");

    CustomMemberDetails customMemberDetails = (CustomMemberDetails) authentication.getPrincipal();

    String email = customMemberDetails.getUsername();

    Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
    Iterator<? extends GrantedAuthority> iterator = authorities.iterator();
    GrantedAuthority auth = iterator.next();
    String role = auth.getAuthority();
    Long id = customMemberDetails.getId();
    Long uid = customMemberDetails.getUid();

    // 토큰 생성 15분, 24시간
    String access = jwtUtil.createJwt("Authorization", id, uid, email, role, 900000L);
    String refresh = jwtUtil.createJwt("refresh", id, uid, email, role, 86400000L);

    //응답 설정
    response.setHeader("Authorization", BEARER + access);
    response.addCookie(createCookie(email, "refresh", refresh));
    response.setStatus(HttpStatus.OK.value());

  }

  //로그인 실패시 실행하는 메소드
  @Override
  protected void unsuccessfulAuthentication(HttpServletRequest request,
      HttpServletResponse response, AuthenticationException failed) {
    log.info("fail");
    response.setStatus(401);
  }

  private Cookie createCookie(String email, String key, String value) {

    // refresh 토근 저장
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
