package dorakdorak.domain.member.dto;

import dorakdorak.domain.member.dto.request.MemberSignupRequest;
import java.time.LocalDate;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MemberSignupDto {

  private long id;

  private long universityId;

  private String email;

  private String password;

  private final String role = "MEMBER";

  private String name;

  private String gender;

  private LocalDate birthdate;

  private String zipCode;

  private final long zeroWasteCount = 0;

  private String doromyungAddress;

  private String jibunAddress;

  private String detailAddress;

  private List<Long> allergyIds;

  public MemberSignupDto(MemberSignupRequest memberSignupRequest) {
    this.universityId = memberSignupRequest.getUniversityId();
    this.email = memberSignupRequest.getEmail();
    this.password = memberSignupRequest.getPassword();
    this.name = memberSignupRequest.getName();
    this.gender = memberSignupRequest.getGender();
    this.birthdate = memberSignupRequest.getBirthdate();
    this.zipCode = memberSignupRequest.getZipCode();
    this.doromyungAddress = memberSignupRequest.getDoromyungAddress();
    this.jibunAddress = memberSignupRequest.getJibunAddress();
    this.detailAddress = memberSignupRequest.getDetailAddress();
    this.allergyIds = memberSignupRequest.getAllergyIds();
  }
}
