package dorakdorak.domain.admin.service;

import dorakdorak.domain.admin.dto.response.DosirakSearchResponse;
import dorakdorak.domain.admin.dto.response.DosirakSearchResponseDto;
import dorakdorak.domain.admin.mapper.AdminMapper;
import dorakdorak.global.error.ErrorCode;
import dorakdorak.global.error.exception.BusinessException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {

  private final AdminMapper adminMapper;

  @Override
  public DosirakSearchResponse searchDosiraksByName(String name, String role) {
    if (role.equals("ADMIN")) {
      throw new BusinessException(ErrorCode.FORBIDDEN);
    }

    List<DosirakSearchResponseDto> dosiraks = adminMapper.findDosiraksByName(name);

    if (dosiraks == null) {
      throw new BusinessException(ErrorCode.DOSIRAK_DATA_ACCESS_ERROR);
    }

    return new DosirakSearchResponse(dosiraks);
  }
}