package dorakdorak.domain.order.dto.response;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MyOrderPreviewResponse {

  List<MyOrderPreviewResponseDto> orders;
}
