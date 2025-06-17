package dorakdorak.domain.order.dto;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderMailInfoDto implements Serializable {

  private String email;
  private String memberName;
  private String dosirakName;
  private Long quantity;
}
