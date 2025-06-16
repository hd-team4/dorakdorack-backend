package dorakdorak.domain.order.dto.response;

import dorakdorak.domain.order.dto.MyOrderDto;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MyOrderResponse {

  private List<MyOrderDto> orders;
}
