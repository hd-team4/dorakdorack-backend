package dorakdorak.domain.admin.dto;

import dorakdorak.domain.admin.dto.request.AdminCustomDosirakRegisterRequest;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AdminCustomDosirakSaveDto {

  private Long customDosirakId;
  private Long adminId;

  public AdminCustomDosirakSaveDto(
      AdminCustomDosirakRegisterRequest adminCustomDosirakRegisterRequest, Long adminId) {
    this.customDosirakId = adminCustomDosirakRegisterRequest.getCustomDosirakId();
    this.adminId = adminId;
  }
}
