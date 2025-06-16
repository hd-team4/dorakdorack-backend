package dorakdorak.domain.dosirak.dto;

import dorakdorak.domain.dosirak.dto.request.CustomDosirakRegisterRequest;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CustomDosirakSaveDto {

  private Long id;

  private Long memberId;

  private String name;

  private Long price;

  private Long weight;

  private final String isCustom = "CUSTOM";
  
  private String storageType;

  private String imageUrl;

  private List<String> categories;

  private NutritionDto nutrition;

  public CustomDosirakSaveDto(CustomDosirakRegisterRequest customDosirakRegisterRequest,
      String imageUrl, Long memberId) {
    this.memberId = memberId;
    this.name = customDosirakRegisterRequest.getName();
    this.imageUrl = imageUrl;
    this.price = customDosirakRegisterRequest.getPrice();
    this.weight = customDosirakRegisterRequest.getWeight();
    this.storageType = customDosirakRegisterRequest.getStorageType();
    this.nutrition = customDosirakRegisterRequest.getNutrition();
    this.categories = customDosirakRegisterRequest.getCategories();
  }
}
