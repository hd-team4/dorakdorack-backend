package dorakdorak.domain.zeroWaste.api;

import dorakdorak.domain.zeroWaste.dto.response.UniversityRankingResponse;
import dorakdorak.domain.zeroWaste.dto.response.ZeroWasteJoinResponse;
import dorakdorak.domain.zeroWaste.dto.response.ZeroWasteResultResponse;
import dorakdorak.domain.zeroWaste.service.ZeroWasteService;
import dorakdorak.global.util.jwt.QrTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RequiredArgsConstructor
@RequestMapping("/api/zero-waste")
@RestController
public class ZeroWasteController {

  private final QrTokenProvider qrTokenProvider;
  private final ZeroWasteService zeroWasteService;

  @GetMapping("/{qrcode}")
  public ResponseEntity<ZeroWasteJoinResponse> getZeroWasteInfo(@PathVariable String qrcode) {
    ZeroWasteJoinResponse response = zeroWasteService.getJoinInfo(qrcode);
    return ResponseEntity.ok(response);
  }

  @PostMapping(value = "/{qrcode}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  public ResponseEntity<ZeroWasteResultResponse> verifyDosirakEmpty(
      @PathVariable String qrcode,
      @RequestPart("image") MultipartFile imageFile) {

    ZeroWasteResultResponse response = zeroWasteService.verifyEmptyDosirak(qrcode, imageFile);
    return ResponseEntity.ok(response);
  }

  @GetMapping("/university")
  public ResponseEntity<UniversityRankingResponse> getUniversityRankings() {
    UniversityRankingResponse response = zeroWasteService.getUniversityRankings();

    return ResponseEntity.ok(response);
  }
}