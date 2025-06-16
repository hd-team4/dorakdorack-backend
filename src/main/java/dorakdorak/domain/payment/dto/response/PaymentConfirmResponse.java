package dorakdorak.domain.payment.dto.response;

import dorakdorak.domain.order.dto.MyOrderItemDto;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class PaymentConfirmResponse {

  private String orderCode;
  private List<MyOrderItemDto> orders;
}
