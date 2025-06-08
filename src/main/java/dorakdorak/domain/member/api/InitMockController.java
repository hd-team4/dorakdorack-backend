package dorakdorak.domain.member.api;

import dorakdorak.domain.member.dto.response.InitDataResponse;
import dorakdorak.domain.member.service.InitService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api")
public class InitMockController {

  private final InitService initService;

  @GetMapping("/universities")
  public List<InitDataResponse> getUniversities() {
    log.info("getUniversities()");
    return initService.findUniversities();
  }

  @GetMapping("/allergies")
  public List<InitDataResponse> getAllergies() {
    log.info("getAllergies()");
    return initService.findAllergys();
  }
}
