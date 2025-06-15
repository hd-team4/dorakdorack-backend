package dorakdorak.domain.admin.mapper;

import dorakdorak.domain.admin.dto.AdminCustomDosirakSaveDto;
import dorakdorak.domain.admin.dto.StatisticPopularResponseDto;
import dorakdorak.domain.admin.dto.StatisticsSalesResponseDto;
import dorakdorak.domain.admin.dto.response.DosirakSearchResponseDto;
import java.util.List;
import java.util.Map;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface AdminMapper {

  List<DosirakSearchResponseDto> findDosiraksByName(String name);

  void updateOfficialDosirak(AdminCustomDosirakSaveDto adminCustomDosirakSaveDto);

  List<StatisticsSalesResponseDto> getWeeklySales(Long dosirakId);

  List<StatisticPopularResponseDto> getPopularDosirakByAge(Map<String, Object> paramMap);

}