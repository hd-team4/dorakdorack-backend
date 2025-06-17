package dorakdorak.infra.mail.dto;

import dorakdorak.domain.order.dto.OrderMailInfoDto;
import dorakdorak.domain.order.enums.OrderStatus;
import java.io.Serializable;
import java.util.List;

public record OrderMailMessage(List<OrderMailInfoDto> items, OrderStatus status) implements Serializable{

  public String email() {
    return items.get(0).getEmail();
  }

  public String memberName() {
    return items.get(0).getMemberName();
  }
}
