package dorakdorak.domain.dosirak.dto.response;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class DosirakOrderDto {

  private Long id;
  private String name;
  private int price;
  private double salesPercentage;
  private String isCustom;
  private String imageUrl;
}
