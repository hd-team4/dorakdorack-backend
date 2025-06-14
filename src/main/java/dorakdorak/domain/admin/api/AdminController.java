package dorakdorak.domain.admin.api;

import dorakdorak.domain.admin.dto.response.DosirakSearchResponse;
import dorakdorak.domain.admin.service.AdminService;
import dorakdorak.domain.auth.dto.response.CustomMemberDetails;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin")
@Slf4j
@RequiredArgsConstructor
public class AdminController {

  private final AdminService adminService;

  @GetMapping("/dosiraks")
  public ResponseEntity<DosirakSearchResponse> searchDosiraksByName(
      @RequestParam("name") String name,
      @AuthenticationPrincipal CustomMemberDetails memberDetails) {

    DosirakSearchResponse response = adminService.searchDosiraksByName(name,
        memberDetails.getRole());
    return ResponseEntity.ok(response);
  }
}
