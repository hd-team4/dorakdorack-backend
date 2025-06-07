package dorakdorak.domain.member.dto.request;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@Getter
@AllArgsConstructor
@ToString
public class MemberGoogleSMTPRequest {

  private String email;

  protected MemberGoogleSMTPRequest() {
  }
}
