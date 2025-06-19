package dorakdorak.domain.payment.api;

import dorakdorak.domain.auth.security.CustomMemberDetails;
import dorakdorak.domain.payment.dto.request.GroupPaymentRequest;
import dorakdorak.domain.payment.dto.request.PaymentConfirmRequest;
import dorakdorak.domain.payment.dto.request.SinglePaymentRequest;
import dorakdorak.domain.payment.dto.response.PaymentConfirmResponse;
import dorakdorak.domain.payment.dto.response.PaymentPrepareResponse;
import dorakdorak.domain.payment.service.PaymentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/api/payments")
@RestController
public class PaymentController {

  private final PaymentService paymentService;

  @PostMapping("/request/single")
  public ResponseEntity<PaymentPrepareResponse> preparePayment(@AuthenticationPrincipal CustomMemberDetails member, @Valid @RequestBody SinglePaymentRequest request) {
    return ResponseEntity.ok(paymentService.prepareSinglePayment(member.getId(), request));
  }

  @PostMapping("/request/group")
  public ResponseEntity<PaymentPrepareResponse> preparePayment(@AuthenticationPrincipal CustomMemberDetails member, @Valid @RequestBody GroupPaymentRequest request) {
    return ResponseEntity.ok(paymentService.prepareGroupPayment(member.getId(), member.getUid(), request));
  }

  @PostMapping("/confirm")
  public ResponseEntity<PaymentConfirmResponse> confirmPayment(@Valid @RequestBody PaymentConfirmRequest request) {
    PaymentConfirmResponse response = paymentService.confirmPayment(request);
    return ResponseEntity.ok(response);
  }
}
