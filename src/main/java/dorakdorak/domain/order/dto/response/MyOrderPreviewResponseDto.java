package dorakdorak.domain.order.dto.response;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MyOrderPreviewResponseDto {

  String name;
  String imageUrl;
  Long price;
  Long amount;
  LocalDateTime orderDate;
  String orderStatus;
}
