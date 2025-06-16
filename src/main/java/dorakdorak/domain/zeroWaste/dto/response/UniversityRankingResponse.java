package dorakdorak.domain.zeroWaste.dto.response;

import dorakdorak.domain.zeroWaste.dto.UniversityRankingDto;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UniversityRankingResponse {

  private List<UniversityRankingDto> universities;
}
