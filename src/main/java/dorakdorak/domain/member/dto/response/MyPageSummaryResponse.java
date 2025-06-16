package dorakdorak.domain.member.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MyPageSummaryResponse {

  private String name;
  private String email;
  private Long normalOrderAmount;
  private Long groupOrderAmount;
  private Long customDosirakAmount;
}
