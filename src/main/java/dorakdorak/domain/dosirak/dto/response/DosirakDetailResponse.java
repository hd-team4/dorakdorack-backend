package dorakdorak.domain.dosirak.dto.response;

import dorakdorak.domain.dosirak.dto.DosirakDetailImageDto;
import dorakdorak.domain.dosirak.dto.NutritionDto;
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

  private Long dosirakId;
  private String name;
  private Integer price;
  private Double salesPercentage;
  private Integer weight;
  private String storageType;
  private String thumbnailImageUrl;
  private List<DosirakDetailImageDto> detailImages;
  private NutritionDto nutrition;
}
