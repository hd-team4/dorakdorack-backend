package dorakdorak.domain.zeroWaste.api;

import dorakdorak.domain.zeroWaste.dto.response.ZeroWasteJoinResponse;
import dorakdorak.domain.zeroWaste.service.ZeroWasteService;
import dorakdorak.global.util.jwt.QrTokenProvider;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/api/zero-waste")
@RestController
public class ZeroWasteController {

  private final QrTokenProvider qrTokenProvider;
  private final ZeroWasteService zeroWasteService;

  @GetMapping("/{qrcode}")
  public ResponseEntity<ZeroWasteJoinResponse> getZeroWasteInfo(@PathVariable String qrcode) {
    Claims claims = qrTokenProvider.validateQrToken(qrcode);

    Long orderId = ((Number) claims.get("orderId")).longValue();
    Long orderItemId = ((Number) claims.get("orderItemId")).longValue();
    Long memberId = ((Number) claims.get("memberId")).longValue();

    ZeroWasteJoinResponse response = zeroWasteService.getJoinInfo(orderId, orderItemId, memberId);
    return ResponseEntity.ok(response);
  }
}