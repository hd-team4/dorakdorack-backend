package dorakdorak.domain.dosirak.dto;

import dorakdorak.domain.dosirak.enums.StorageType;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DosirakFilterDto {

  private Long dosirakId;
  private String name;
  private Long price;
  private double salesPercentage;
  private StorageType storageType;
  private String imageUrl;
  private LocalDateTime createdAt;
  private Long vote;
}
