package dorakdorak.domain.admin.dto.response;

import dorakdorak.domain.admin.dto.StatisticsSalesResponseDto;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class StatisticsSalesResponse {

  private List<StatisticsSalesResponseDto> data;
}
