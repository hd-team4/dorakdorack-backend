package dorakdorak.domain.admin.mapper;

import dorakdorak.domain.admin.dto.AdminCustomDosirakSaveDto;
import dorakdorak.domain.admin.dto.response.DosirakSearchResponseDto;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface AdminMapper {

  List<DosirakSearchResponseDto> findDosiraksByName(String name);

  void updateOfficialDosirak(AdminCustomDosirakSaveDto adminCustomDosirakSaveDto);

}