package dorakdorak.domain.auth.api;

import dorakdorak.domain.auth.dto.response.CustomMemberDetails;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@Slf4j
public class AuthController {

  @GetMapping("/test")
  public String orders(@AuthenticationPrincipal CustomMemberDetails customMemberDetails) {
    return customMemberDetails.getUsername();
  }
}
