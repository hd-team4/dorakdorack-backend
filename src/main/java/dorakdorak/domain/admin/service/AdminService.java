package dorakdorak.domain.admin.service;

import dorakdorak.domain.admin.dto.AdminCustomDosirakSaveDto;
import dorakdorak.domain.admin.dto.response.DosirakSearchResponse;

public interface AdminService {

  // 도시락 이름 검색 (관리자용)
  DosirakSearchResponse searchDosiraksByName(String name, String role);


  // 커스텀 도시락 정식 메뉴 등록
  void approveOfficialDosirak(AdminCustomDosirakSaveDto adminCustomDosirakSaveDto, String role);
}