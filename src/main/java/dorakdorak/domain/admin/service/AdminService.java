package dorakdorak.domain.admin.service;

import dorakdorak.domain.admin.dto.response.DosirakSearchResponse;

public interface AdminService {

  // 도시락 이름 검색 (관리자용)
  DosirakSearchResponse searchDosiraksByName(String name, String role);
}