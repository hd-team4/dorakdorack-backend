package dorakdorak.domain.order.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MyOrderItemDto {

  private String name;
  private String imageUrl;
  private int price;
  private int amount;
  private String orderStatus;
}