package dorakdorak.domain.admin.service;

import dorakdorak.domain.admin.dto.AdminCustomDosirakSaveDto;
import dorakdorak.domain.admin.dto.StatisticPopularResponseDto;
import dorakdorak.domain.admin.dto.StatisticsOrderResponseDto;
import dorakdorak.domain.admin.dto.StatisticsSalesResponseDto;
import dorakdorak.domain.admin.dto.response.DosirakSearchResponse;
import dorakdorak.domain.admin.dto.response.DosirakSearchResponseDto;
import dorakdorak.domain.admin.dto.response.StatisticPopularResponse;
import dorakdorak.domain.admin.dto.response.StatisticsOrderResponse;
import dorakdorak.domain.admin.dto.response.StatisticsSalesResponse;
import dorakdorak.domain.admin.mapper.AdminMapper;
import dorakdorak.global.error.ErrorCode;
import dorakdorak.global.error.exception.BusinessException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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

  @Override
  @Transactional(readOnly = true)
  public StatisticsSalesResponse getWeeklySales(Long dosirakId) {
    List<StatisticsSalesResponseDto> data = adminMapper.getWeeklySales(dosirakId);
    return new StatisticsSalesResponse(data);
  }

  @Override
  @Transactional(readOnly = true)
  public StatisticPopularResponse getPopularDosirakByAge(Integer age) {
    Map<String, Object> paramMap = new HashMap<>();
    if (age != null) {
      paramMap.put("ageMin", age);
      paramMap.put("ageMax", age + 9);
    }
    List<StatisticPopularResponseDto> items = adminMapper.getPopularDosirakByAge(paramMap);
    return new StatisticPopularResponse(items);
  }

  @Override
  @Transactional(readOnly = true)
  public StatisticsOrderResponse getOrderRatio(Long dosirakId) {
    List<StatisticsOrderResponseDto> list = adminMapper.getOrderTypeRatio(dosirakId);

    int single = 0;
    int group = 0;

    for (StatisticsOrderResponseDto dto : list) {
      if ("NORMAL".equalsIgnoreCase(dto.getOrderType())) {
        single = dto.getCount();
      } else if ("GONGGOO".equalsIgnoreCase(dto.getOrderType())) {
        group = dto.getCount();
      }
    }

    return new StatisticsOrderResponse(single, group);
  }
  
}