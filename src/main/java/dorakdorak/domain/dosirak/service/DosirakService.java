package dorakdorak.domain.dosirak.service;

import dorakdorak.domain.dosirak.dto.CustomDosirakSaveDto;
import dorakdorak.domain.dosirak.dto.response.DosirakDetailResponse;
import dorakdorak.domain.dosirak.dto.response.DosirakFilterResponse;
import dorakdorak.domain.dosirak.dto.response.MyCustomDosirakResponse;
import dorakdorak.domain.dosirak.enums.DosirakType;
import dorakdorak.domain.dosirak.enums.FilterType;
import dorakdorak.domain.dosirak.enums.SortType;

public interface DosirakService {

  // 회원 ID로 해당 회원의 커스텀 도시락 내역 조회
  MyCustomDosirakResponse getCustomDosiraksByMemberId(Long memberId);

  // 회원 ID로 해당 회원의 커스텀 도시락 내역 미리보기 정보 조회
  MyCustomDosirakResponse getCustomDosiraksPreviewByMemberId(Long memberId);

  // 도시락 ID, 필터링 타입, 정렬 기준, 반환 개수로 도시락 정보 조회
  DosirakFilterResponse getDosiraks(Long dosirakId, FilterType filterType,
      SortType sortType, DosirakType dosirakType,
      Long count);

  // 도시락 ID로 상세 정보 조회
  DosirakDetailResponse getDosirakDetail(Long dosirakId);

  // 커스텀 도시락 등록
  void registerCustomDosirak(CustomDosirakSaveDto customDosirakSaveDto);

  // 커스텀 도시락 투표
  void customDosirakVote(Long dosirakId, Long memberId);
}
