package dorakdorak.global.config;

import dorakdorak.domain.auth.filter.CustomLogoutFilter;
import dorakdorak.domain.auth.filter.LoginFilter;
import dorakdorak.domain.auth.jwt.JWTFilter;
import dorakdorak.domain.auth.jwt.JWTUtil;
import dorakdorak.domain.auth.service.RedisRefreshTokenService;
import dorakdorak.domain.member.service.MemberService;
import jakarta.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

  private final RedisRefreshTokenService redisRefreshTokenService;

  private final JWTUtil jwtUtil;

  private final MemberService memberService;

  // AuthenticationManager가 인자로 받을 AuthenticationConfiguraion 객체 생성자 주입
  private final AuthenticationConfiguration authenticationConfiguration;
  private static final String[] WHITE_LIST = {
      "/api/auth/**",
      "/api/dosiraks",
      "/api/dosiraks/**",
      "/",
      "/favicon.ico",
      "/css/**",
      "/js/**",
      "/images/**",
      "/swagger-ui/**",
      "/v3/api-docs/**"
  };


  // AuthenticationManager Bean 등록
  @Bean
  public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration)
      throws Exception {

    return configuration.getAuthenticationManager();
  }

  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    http
        .cors(corsCustomizer -> corsCustomizer.configurationSource(new CorsConfigurationSource() {

          @Override
          public CorsConfiguration getCorsConfiguration(HttpServletRequest request) {

            CorsConfiguration configuration = new CorsConfiguration();

            // domain 어플의 모든 요청 허용, 추후 도메인 추가
            List<String> allowedOrigins = Arrays.asList("http://localhost:5173",
                "https://dorakdorak.store");
            configuration.setAllowedOrigins(allowedOrigins);
            // get, post, put 모든 요청 허용
            configuration.setAllowedMethods(Collections.singletonList("*"));
            // 클라이언트로부터 쿠키 및 토큰 전송 허용
            configuration.setAllowCredentials(true);
            // 어떤 헤더도 다 받을 수 있다.
            configuration.setAllowedHeaders(Collections.singletonList("*"));
            // 요청에 대한 응답이 3600초 동안 캐시된다. 브라우저는 일정 시간 동안 동일한 요청에 대한 preflight 요청을 다시 보내지 않고 이전에 받은 응답을 사용할 수 있다.
            configuration.setMaxAge(3600L);

            // 클라이언트에 노출시킬 응답 헤더들
//            configuration.setExposedHeaders(Collections.singletonList("Set-Cookie"));
            configuration.setExposedHeaders(Collections.singletonList("Authorization"));

            return configuration;
          }
        }));

    // csrf disable
    http
        .csrf((auth) -> auth.disable());

    // Form 로그인 방식 disable
    http
        .formLogin((auth) -> auth.disable());

    // HTTP Basic 인증 방식 disable
    http
        .httpBasic((auth) -> auth.disable());

    // JWT 필터 추가

    // 경로별 인가 작업
    http
        .authorizeHttpRequests(auth -> auth
            .requestMatchers(WHITE_LIST).permitAll()
            .requestMatchers("/api/dosiraks/custom/preview").authenticated()
            .requestMatchers("/api/admin/**").hasRole("ADMIN")
            .anyRequest().authenticated());

    // JWT 검증 필터
    http.addFilterBefore(new JWTFilter(jwtUtil), LoginFilter.class);

    http.addFilterAt(new LoginFilter(authenticationManager(authenticationConfiguration), jwtUtil,
            redisRefreshTokenService, memberService),
        UsernamePasswordAuthenticationFilter.class);

    http
        .addFilterBefore(new CustomLogoutFilter(jwtUtil, redisRefreshTokenService),
            LogoutFilter.class);

    // 세션 설정 : STATELESS
    http
        .sessionManagement((session) -> session
            .sessionCreationPolicy(SessionCreationPolicy.STATELESS));

    return http.build();
  }

}
