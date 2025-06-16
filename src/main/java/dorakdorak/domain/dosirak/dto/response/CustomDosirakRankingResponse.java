package dorakdorak.domain.dosirak.dto.response;

import dorakdorak.domain.dosirak.dto.CustomDosirakRankingDto;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CustomDosirakRankingResponse {
  private List<CustomDosirakRankingDto> dosiraks;
}
