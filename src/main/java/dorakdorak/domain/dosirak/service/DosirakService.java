package dorakdorak.domain.dosirak.service;

import dorakdorak.domain.dosirak.dto.response.MyCustomDosirakResponse;

public interface DosirakService {

    // 회원 ID로 해당 회원의 커스텀 도시락 내역 조회
    MyCustomDosirakResponse getCustomDosiraksByMemberId(Long memberId);

    // 회원 ID로 해당 회원의 커스텀 도시락 내역 미리보기 정보 조회
    MyCustomDosirakResponse getCustomDosiraksPreviewByMemberId(Long memberId);
}
