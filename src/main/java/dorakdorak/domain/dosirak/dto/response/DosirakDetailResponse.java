package dorakdorak.domain.dosirak.dto.response;

import dorakdorak.domain.dosirak.dto.DosirakDetailImageDto;
import dorakdorak.domain.dosirak.dto.DosirakNutritionDto;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DosirakDetailResponse {

  Long dosirakId;
  String name;
  Integer price;
  Double salesPercentage;
  Integer weight;
  String storageType;
  String thumbnailImageUrl;
  List<DosirakDetailImageDto> detailImages;
  DosirakNutritionDto nutrition;
}
