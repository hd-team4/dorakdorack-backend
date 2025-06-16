package dorakdorak.domain.admin.service;

import dorakdorak.domain.admin.dto.AdminCustomDosirakSaveDto;
import dorakdorak.domain.admin.dto.response.DosirakSearchResponse;
import dorakdorak.domain.admin.dto.DosirakSearchDto;
import dorakdorak.domain.admin.mapper.AdminMapper;
import dorakdorak.global.error.ErrorCode;
import dorakdorak.global.error.exception.BusinessException;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {

  private final AdminMapper adminMapper;

  @Override
  @Transactional(readOnly = true)
  public DosirakSearchResponse searchDosiraksByName(String name) {
    List<DosirakSearchDto> dosiraks = adminMapper.findDosiraksByName(name);

    if (dosiraks == null) {
      throw new BusinessException(ErrorCode.DOSIRAK_DATA_ACCESS_ERROR);
    }

    return new DosirakSearchResponse(dosiraks);
  }

  @Override
  @Transactional
  public void approveOfficialDosirak(AdminCustomDosirakSaveDto adminCustomDosirakSaveDto) {

    adminMapper.updateOfficialDosirak(adminCustomDosirakSaveDto);
  }
}