package dorakdorak.global.config;

import java.util.List;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
public class CorsConfig {

  @Bean
  public CorsConfigurationSource corsConfigurationSource() {
    CorsConfiguration configuration = new CorsConfiguration();

    // 추후 도메인 추가
    configuration.setAllowedOriginPatterns(List.of("http://localhost:5173", ""));
    configuration.setAllowedMethods(List.of("GET", "POST", "PATCH", "DELETE"));
    configuration.setAllowedHeaders(List.of("*"));
//    configuration.addExposedHeader("Set-Cookie");
    configuration.addExposedHeader("Authorization");
    configuration.setAllowCredentials(true);

    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    source.registerCorsConfiguration("/**", configuration);
    return source;
  }

}
