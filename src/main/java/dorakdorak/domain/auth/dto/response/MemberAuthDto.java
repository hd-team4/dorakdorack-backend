package dorakdorak.domain.auth.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MemberAuthDto {

  private Long id;
  private Long universityId;
  private String email;
  private String password;
  private String role;
}
