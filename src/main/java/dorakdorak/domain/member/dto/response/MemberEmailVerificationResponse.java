package dorakdorak.domain.member.dto.response;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@Getter
@AllArgsConstructor
@ToString
public class MemberEmailVerificationResponse {

  private String status;
  private String message;

  protected MemberEmailVerificationResponse() {
  }

}
