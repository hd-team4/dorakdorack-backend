package dorakdorak.domain.admin.dto.response;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DosirakSearchResponse {

  List<DosirakSearchResponseDto> dosiraks;
}
