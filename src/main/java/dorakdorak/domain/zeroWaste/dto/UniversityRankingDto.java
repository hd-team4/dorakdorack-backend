package dorakdorak.domain.zeroWaste.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UniversityRankingDto {

  private int rank;
  private String name;
  private int count;
  private String logoUrl;
}
