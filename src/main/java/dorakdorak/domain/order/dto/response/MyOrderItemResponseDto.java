package dorakdorak.domain.order.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MyOrderItemResponseDto {

  String name;
  String imageUrl;
  int price;
  int amount;
  String orderStatus;
}