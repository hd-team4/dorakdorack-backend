package dorakdorak.domain.admin.dto.response;

import dorakdorak.domain.admin.dto.DosirakSearchDto;
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

  List<DosirakSearchDto> dosiraks;
}
