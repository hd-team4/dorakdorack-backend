package dorakdorak.domain.admin.service;

import dorakdorak.domain.admin.dto.AdminCustomDosirakSaveDto;
import dorakdorak.domain.admin.dto.response.DosirakSearchResponse;
import dorakdorak.domain.admin.dto.response.StatisticPopularResponse;
import dorakdorak.domain.admin.dto.response.StatisticsOrderResponse;
import dorakdorak.domain.admin.dto.response.StatisticsSalesResponse;

public interface AdminService {

  // 도시락 이름 검색 (관리자용)
  DosirakSearchResponse searchDosiraksByName(String name);

  // 커스텀 도시락 정식 메뉴 등록
  void approveOfficialDosirak(AdminCustomDosirakSaveDto adminCustomDosirakSaveDto);

  // 최근 7일 판매량 조회 (관리자용)
  StatisticsSalesResponse getWeeklySales(Long dosirakId);

  // 연령대별 인기 도시락 조회 (관리자용)
  StatisticPopularResponse getPopularDosirakByAge(Integer integer);

  // 주문 비율 조회 (관리자용)
  StatisticsOrderResponse getOrderRatio(Long dosirakId);

}