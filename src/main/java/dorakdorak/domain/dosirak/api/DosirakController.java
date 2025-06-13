package dorakdorak.domain.dosirak.api;

import dorakdorak.domain.dosirak.dto.response.DosirakListResponse;
import dorakdorak.domain.dosirak.enums.FilterType;
import dorakdorak.domain.dosirak.enums.SortType;
import dorakdorak.domain.dosirak.service.DosirakService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@Slf4j
@RequiredArgsConstructor
public class DosirakController {

    private final DosirakService dosirakService;

    @GetMapping("/dosiraks")
    public ResponseEntity<DosirakListResponse> getDosiraks(
        @RequestParam(required = false) Long dosirakId,
        @RequestParam(name = "filterType", defaultValue = "ALL") FilterType filterType,
        @RequestParam(name = "sortType", defaultValue = "LATEST") SortType sortType
    ) {
        DosirakListResponse response = dosirakService.getDosiraks(dosirakId, filterType, sortType);
        return ResponseEntity.ok(response);
    }
}
