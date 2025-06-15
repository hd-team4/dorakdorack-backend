package dorakdorak.domain.member.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
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

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "회원가입 요청")
public class MemberSignupRequest {

  @Schema(description = "회원 ID (요청 시 사용되지 않음)", example = "0")
  private long id;

  @NotNull(message = "대학교명은 필수 입력값입니다.")
  @Schema(description = "대학교 ID", example = "1")
  private long universityId;

  @NotBlank(message = "이메일은 필수 입력값입니다.")
  @Email(message = "유효한 이메일 형식이어야 합니다.")
  @Schema(description = "이메일", example = "dorak@naver.com", required = true)
  private String email;

  @NotBlank(message = "비밀번호는 필수 입력값입니다.")
  @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[~!@#$%^&*(),.?\":{}|<>]).{8,}$",
      message = "비밀번호는 영문 대소문자, 숫자, 특수문자를 포함한 8자 이상이어야 합니다.")
  @Schema(description = "비밀번호 (대소문자, 숫자, 특수문자 포함 8자 이상)", example = "P@ssw0rd!", required = true)
  private String password;

  @Schema(description = "역할 (기본값: MEMBER, 관리자: ADMIN)", example = "MEMBER", defaultValue = "MEMBER")
  private final String role = "MEMBER";


  @NotBlank(message = "이름은 필수 입력값입니다.")
  @Schema(description = "이름", example = "홍길동", required = true)
  private String name;

  @NotBlank(message = "성별은 필수 입력값입니다.")
  @Pattern(regexp = "^[MF]$", message = "성별은 'M' 또는 'F'여야 합니다.")
  @Schema(description = "성별 (M 또는 F)", example = "M", required = true)
  private String gender;

  @NotNull(message = "생년월일은 필수 입력값입니다.")
  @JsonFormat(pattern = "yyyy-MM-dd")
  @Schema(description = "생년월일", example = "1995-08-15", required = true)
  private LocalDate birthdate;

  @NotBlank(message = "우편번호는 필수 입력값입니다.")
  @Schema(description = "우편번호", example = "12345", required = true)
  private String zipCode;

  @Schema(description = "제로웨이스트 횟수 (기본값: 0)", example = "0", defaultValue = "0")
  private final long zeroWasteCount = 0;

  @NotBlank(message = "도로명 주소는 필수 입력값입니다.")
  @Schema(description = "도로명 주소", example = "서울시 성동구 왕십리로 222", required = true)
  private String doromyungAddress;

  @NotBlank(message = "지번 주소는 필수 입력값입니다.")
  @Schema(description = "지번 주소", example = "성수동1가 123-45", required = true)
  private String jibunAddress;

  @NotBlank(message = "상세 주소는 필수 입력값입니다.")
  @Schema(description = "상세 주소", example = "101동 202호", required = true)
  private String detailAddress;

  @Schema(description = "알레르기 ID 리스트", example = "[1, 3, 5]")
  private List<Long> allergyIds;

}
