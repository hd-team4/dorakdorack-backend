package dorakdorak.domain.admin.service;

import dorakdorak.domain.admin.dto.response.DosirakSearchResponse;

public interface AdminService {

  DosirakSearchResponse searchDosiraksByName(String name);
}