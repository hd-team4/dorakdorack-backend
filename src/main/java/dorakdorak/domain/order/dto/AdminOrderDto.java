package dorakdorak.domain.order.dto;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class AdminOrderDto {

  private Long orderId;
  private String orderCode;
  private String type;         // "SINGLE" or "GROUP"
  private int price;
  private String arrivedAt;    // "2025년 2월 17일 14시"
  private String orderStatus;
}
