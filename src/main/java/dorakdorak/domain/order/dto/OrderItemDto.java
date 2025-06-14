package dorakdorak.domain.order.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderItemDto {

  private Long id;
  private Long orderId;
  private Long dosirakId;
  private String name;
  private int price;
  private String imageUrl;
  private String orderStatus;
  private Long createdBy;
}
