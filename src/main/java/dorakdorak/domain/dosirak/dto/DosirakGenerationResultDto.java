package dorakdorak.domain.dosirak.dto;

import java.util.ArrayList;
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
  private final List<String> categories = new ArrayList<>();
  private NutritionDto nutrition;
  private String prompt;

}
