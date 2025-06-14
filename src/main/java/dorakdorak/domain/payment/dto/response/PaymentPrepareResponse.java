package dorakdorak.domain.payment.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PaymentPrepareResponse {

    private String orderId;
    private int amount;
    private String orderName;
}
