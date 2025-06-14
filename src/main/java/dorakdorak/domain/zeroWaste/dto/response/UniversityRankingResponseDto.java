package dorakdorak.domain.zeroWaste.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UniversityRankingResponseDto {

  private int rank;
  private String name;
  private int count;
  private String logoUrl;
}
