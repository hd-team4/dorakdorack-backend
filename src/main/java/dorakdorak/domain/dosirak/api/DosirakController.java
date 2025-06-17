package dorakdorak.domain.dosirak.api;

import dorakdorak.domain.auth.security.CustomMemberDetails;
import dorakdorak.domain.dosirak.dto.response.DosirakDetailResponse;
import dorakdorak.domain.dosirak.dto.response.DosirakFilterResponse;
import dorakdorak.domain.dosirak.enums.DosirakType;
import dorakdorak.domain.dosirak.enums.FilterType;
import dorakdorak.domain.dosirak.enums.SortType;
import dorakdorak.domain.dosirak.service.DosirakService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/dosiraks")
@Slf4j
@RequiredArgsConstructor
public class DosirakController {

  private final DosirakService dosirakService;

  @GetMapping("")
  public ResponseEntity<DosirakFilterResponse> getDosiraks(
      @RequestParam(name = "dosirakId", required = false) Long dosirakId,
      @RequestParam(name = "filterType", required = false, defaultValue = "ALL") FilterType filterType,
      @RequestParam(name = "sortType", required = false, defaultValue = "LATEST") SortType sortType,
      @RequestParam(name = "dosirakType", required = false, defaultValue = "NORMAL") DosirakType dosirakType,
      @RequestParam(name = "count", required = false, defaultValue = "12") Long count,
      @AuthenticationPrincipal CustomMemberDetails customMemberDetails
  ) {
    DosirakFilterResponse response = dosirakService.getDosiraks(customMemberDetails.getId(), dosirakId, filterType, sortType,
        dosirakType,
        count);
    return ResponseEntity.ok(response);
  }

  @GetMapping("/{dosirakId}")
  public ResponseEntity<DosirakDetailResponse> getDosirakDetail(@PathVariable Long dosirakId) {
    return ResponseEntity.ok(dosirakService.getDosirakDetail(dosirakId));
  }
}
