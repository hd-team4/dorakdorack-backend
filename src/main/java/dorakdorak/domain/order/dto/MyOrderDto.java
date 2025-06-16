package dorakdorak.domain.order.dto;

import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MyOrderDto {

  private Long orderId;
  private String orderCode;
  private LocalDateTime orderDate;
  private List<MyOrderItemDto> items;
}