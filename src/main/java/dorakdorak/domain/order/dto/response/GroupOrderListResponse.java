package dorakdorak.domain.order.dto.response;

import dorakdorak.domain.order.dto.GroupOrderDto;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class GroupOrderListResponse {
  private List<GroupOrderDto> orders;
}