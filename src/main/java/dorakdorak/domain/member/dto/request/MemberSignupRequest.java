package dorakdorak.domain.member.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import java.time.LocalDate;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class MemberSignupRequest {

  private long id;

  @NotNull(message = "대학교명은 필수 입력값입니다.")
  private long universityId;

  @NotBlank(message = "이메일은 필수 입력값입니다.")
  @Email(message = "유효한 이메일 형식이어야 합니다.")
  private String email;

  @NotBlank(message = "비밀번호는 필수 입력값입니다.")
  @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[~!@#$%^&*(),.?\":{}|<>]).{8,}$",
      message = "비밀번호는 영문 대소문자, 숫자, 특수문자를 포함한 8자 이상이어야 합니다.")
  private String password;

  private final String role = "ROLE_MEMBER";


  @NotBlank(message = "이름은 필수 입력값입니다.")
  private String name;

  @NotBlank(message = "성별은 필수 입력값입니다.")
  @Pattern(regexp = "^[MF]$", message = "성별은 'M' 또는 'F'여야 합니다.")
  private String gender;

  @NotNull(message = "생년월일은 필수 입력값입니다.")
  @JsonFormat(pattern = "yyyy-MM-dd")
  private LocalDate birthdate;

  @NotBlank(message = "우편번호는 필수 입력값입니다.")
  private String zipCode;

  private final long zeroWasteCount = 0;

  @NotBlank(message = "도로명 주소는 필수 입력값입니다.")
  private String doromyungAddress;

  @NotBlank(message = "지번 주소는 필수 입력값입니다.")
  private String jibunAddress;

  @NotBlank(message = "상세 주소는 필수 입력값입니다.")
  private String detailAddress;

  private List<Long> allergyIds;

}
