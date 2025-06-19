package dorakdorak.domain.payment.service;

import dorakdorak.domain.payment.dto.request.GroupPaymentRequest;
import dorakdorak.domain.payment.dto.request.PaymentConfirmRequest;
import dorakdorak.domain.payment.dto.request.SinglePaymentRequest;
import dorakdorak.domain.payment.dto.response.PaymentConfirmResponse;
import dorakdorak.domain.payment.dto.response.PaymentPrepareResponse;
import dorakdorak.infra.payment.toss.TossPaymentsResponse;

public interface PaymentService {

  PaymentPrepareResponse prepareSinglePayment(Long memberId, SinglePaymentRequest request);

  PaymentPrepareResponse prepareGroupPayment(Long memberId, Long universityId, GroupPaymentRequest request);

  PaymentConfirmResponse confirmPayment(PaymentConfirmRequest request);
}
