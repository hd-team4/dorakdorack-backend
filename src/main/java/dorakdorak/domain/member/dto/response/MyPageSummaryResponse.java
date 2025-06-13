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
    String name;
    String email;
    Long normalOrderAmount;
    Long groupOrderAmount;
    Long customDosirakAmount;
}
