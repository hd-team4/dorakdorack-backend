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

  Long dosirakId;
  String name;
  Long price;
  double salesPercentage;
  StorageType storageType;
  String imageUrl;
  LocalDateTime createdAt;
  Long vote;
}
