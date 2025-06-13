package dorakdorak.domain.auth.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@AllArgsConstructor
public class MemberAuthDto {

  private Long id;
  private Long universityId;
  private String email;
  private String password;
  private String role;

  protected MemberAuthDto() {
  }
}
