package dorakdorak.domain.dosirak.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DosirakFilterResponse {
    List<DosirakFilterResponseDto> dosiraks;
}
