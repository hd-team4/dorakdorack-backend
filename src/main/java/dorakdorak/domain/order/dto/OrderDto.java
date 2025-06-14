package dorakdorak.domain.order.dto;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderDto {

  private Long id;
  private String merchantOrderId;
  private Long memberId;
  private String orderCode;
  private String orderStatus;
  private long price;
  private String isGonggoo;
  private LocalDateTime arrivalAt;
}
