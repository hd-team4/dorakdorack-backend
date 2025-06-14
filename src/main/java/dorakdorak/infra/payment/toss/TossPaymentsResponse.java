package dorakdorak.infra.payment.toss;

import lombok.Getter;

@Getter
public class TossPaymentsResponse {

    private String orderId; // 주문 ID
    private String approvedAt; // 승인 시간
    private String method; // 결제 수단 (카드, 가상계좌 등)
    private String paymentKey; // 결제 식별자
    private int totalAmount; // 결제 금액
}