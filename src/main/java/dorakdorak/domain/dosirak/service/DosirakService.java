package dorakdorak.domain.dosirak.service;

import dorakdorak.domain.dosirak.dto.response.DosirakDetailResponse;
import dorakdorak.domain.dosirak.dto.response.DosirakListResponse;
import dorakdorak.domain.dosirak.dto.response.MyCustomDosirakResponse;
import dorakdorak.domain.dosirak.enums.DosirakType;
import dorakdorak.domain.dosirak.enums.FilterType;
import dorakdorak.domain.dosirak.enums.SortType;

public interface DosirakService {

  // 회원 ID로 해당 회원의 커스텀 도시락 내역 조회
  MyCustomDosirakResponse getCustomDosiraksByMemberId(Long memberId);

  // 회원 ID로 해당 회원의 커스텀 도시락 내역 미리보기 정보 조회
  MyCustomDosirakResponse getCustomDosiraksPreviewByMemberId(Long memberId);

  DosirakListResponse getDosiraks(Long dosirakId, FilterType filterType,
      SortType sortType, DosirakType dosirakType,
      Long count);

  DosirakDetailResponse getDosirakDetail(Long dosirakId);
}
