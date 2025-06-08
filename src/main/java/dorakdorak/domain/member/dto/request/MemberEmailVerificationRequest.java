package dorakdorak.domain.member.dto.request;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@Getter
@AllArgsConstructor
@ToString
public class MemberEmailVerificationRequest {

  private String email;
  private String code;

  protected MemberEmailVerificationRequest() {
  }

}
