package dorakdorak.domain.payment.api;

import dorakdorak.domain.auth.dto.response.CustomMemberDetails;
import dorakdorak.domain.payment.dto.request.GroupPaymentRequest;
import dorakdorak.domain.payment.dto.request.PaymentConfirmRequest;
import dorakdorak.domain.payment.dto.request.SinglePaymentRequest;
import dorakdorak.domain.payment.dto.response.PaymentPrepareResponse;
import dorakdorak.domain.payment.service.PaymentService;
import dorakdorak.infra.payment.toss.TossPaymentsResponse;
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
    return ResponseEntity.ok(paymentService.prepareSinglePayment(1L, request));
  }

  @PostMapping("/request/group")
  public ResponseEntity<PaymentPrepareResponse> preparePayment(@AuthenticationPrincipal CustomMemberDetails member, @Valid @RequestBody GroupPaymentRequest request) {
    return ResponseEntity.ok(paymentService.prepareGroupPayment(member.getId(), request));
  }

  @PostMapping("/confirm")
  public ResponseEntity<?> confirmPayment(@Valid @RequestBody PaymentConfirmRequest request) {
    TossPaymentsResponse response = paymentService.confirmPayment(request);
    return ResponseEntity.ok(response);
  }
}
