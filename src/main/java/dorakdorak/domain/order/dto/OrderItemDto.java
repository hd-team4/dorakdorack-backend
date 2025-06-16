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

  // TODO: itemStatus로 변경 및 관련 로직들 수정 (order_status from order_items 등)
  private Long id;
  private Long orderId;
  private Long dosirakId;
  private String name;
  private int price;
  private String imageUrl;
  private String orderStatus;
  private Long createdBy;
}
