package dorakdorak.domain.admin.service;

import dorakdorak.domain.admin.dto.AdminCustomDosirakSaveDto;
import dorakdorak.domain.admin.dto.StatisticsSalesResponseDto;
import dorakdorak.domain.admin.dto.response.DosirakSearchResponse;
import dorakdorak.domain.admin.dto.response.DosirakSearchResponseDto;
import dorakdorak.domain.admin.dto.response.StatisticsSalesResponse;
import dorakdorak.domain.admin.mapper.AdminMapper;
import dorakdorak.global.error.ErrorCode;
import dorakdorak.global.error.exception.BusinessException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {

  private final AdminMapper adminMapper;

  @Override
  public DosirakSearchResponse searchDosiraksByName(String name, String role) {
    if ("MEMBER".equals(role)) {
      throw new BusinessException(ErrorCode.FORBIDDEN);
    }

    List<DosirakSearchResponseDto> dosiraks = adminMapper.findDosiraksByName(name);

    if (dosiraks == null) {
      throw new BusinessException(ErrorCode.DOSIRAK_DATA_ACCESS_ERROR);
    }

    return new DosirakSearchResponse(dosiraks);
  }

  @Override
  public void approveOfficialDosirak(AdminCustomDosirakSaveDto adminCustomDosirakSaveDto,
      String role) {
    if ("MEMBER".equals(role)) {
      throw new BusinessException(ErrorCode.FORBIDDEN);
    }
    adminMapper.updateOfficialDosirak(adminCustomDosirakSaveDto);
  }

  @Transactional(readOnly = true)
  public StatisticsSalesResponse getWeeklySales(Long dosirakId) {
    List<StatisticsSalesResponseDto> data = adminMapper.getWeeklySales(dosirakId);
    return new StatisticsSalesResponse(data);
  }
}