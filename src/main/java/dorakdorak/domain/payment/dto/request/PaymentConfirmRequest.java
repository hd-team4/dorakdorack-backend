package dorakdorak.domain.payment.dto.request;


import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class PaymentConfirmRequest {

    @NotNull
    private String orderId;

    @NotNull
    private String paymentKey;

    private int amount;
}
