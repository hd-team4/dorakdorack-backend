package dorakdorak.domain.order.dto;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class GroupOrderDto {
  private Long dosirakId;
  private String name;
  private String category;
  private int price;
  private int count;
}
