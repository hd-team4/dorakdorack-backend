package dorakdorak.domain.dosirak.dto.response;

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
  Double salePercentage;
  Integer weight;
  String storageType;
  String thumbnailImageUrl;
  List<DosirakDetailImageResponseDto> detailImages;
  DosirakNutritionResponseDto nutrition;
}
