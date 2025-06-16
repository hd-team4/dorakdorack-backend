package dorakdorak.domain.dosirak.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DosirakNutritionDto {

  Double calories;
  Double carbohydrates;
  Double sugars;
  Double protein;
  Double cholesterol;
  Double fat;
  Double transFat;
}
