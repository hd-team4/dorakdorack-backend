package dorakdorak.domain.payment.service;

import dorakdorak.domain.payment.dto.request.GroupPaymentRequest;
import dorakdorak.domain.payment.dto.request.SinglePaymentRequest;
import dorakdorak.domain.payment.dto.response.PaymentPrepareResponse;

public interface PaymentService {

  PaymentPrepareResponse prepareSinglePayment(Long memberId, SinglePaymentRequest request);

  PaymentPrepareResponse prepareGroupPayment(Long memberId, GroupPaymentRequest request);
}
