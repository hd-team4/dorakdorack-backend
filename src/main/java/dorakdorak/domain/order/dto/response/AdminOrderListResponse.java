package dorakdorak.domain.order.dto.response;

import dorakdorak.domain.order.dto.AdminOrderDto;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@AllArgsConstructor
public class AdminOrderListResponse {
  private List<AdminOrderDto> orders;
  private int page;
  private int size;
  private int totalElements;

  public int getTotalPages() {
    return (int) Math.ceil((double) totalElements / size);
  }
}