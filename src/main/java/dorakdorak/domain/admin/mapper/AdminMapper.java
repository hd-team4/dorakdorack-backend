package dorakdorak.domain.admin.mapper;

import dorakdorak.domain.admin.dto.AdminCustomDosirakSaveDto;
import dorakdorak.domain.admin.dto.DosirakSearchDto;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface AdminMapper {

  List<DosirakSearchDto> findDosiraksByName(String name);

  void updateOfficialDosirak(AdminCustomDosirakSaveDto adminCustomDosirakSaveDto);

}