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
public class MyOrderPreviewDto {

  private String name;
  private String imageUrl;
  private Long price;
  private Long amount;
  private LocalDateTime orderDate;
  private String orderStatus;
}
