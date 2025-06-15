package dorakdorak.domain.dosirak.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class DosirakGenerationResultDto {

  private String name;
  private Long price;
  private Long weight;
  private String storageType;
  private List<String> categories;
  private NutritionDto nutrition;
  private String prompt;

}
