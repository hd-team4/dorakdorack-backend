package dorakdorak.domain.order.service;

import dorakdorak.domain.order.dto.response.MyOrderPreviewResponse;
import dorakdorak.domain.order.dto.response.MyOrderResponse;

public interface OrderService {

    // 회원 ID로 일반 주문 내역 조회
    MyOrderResponse getNormalOrdersByMemberId(Long memberId);

    // 회원 ID로 일반 주문 내역 미리보기 정보 조회
    MyOrderPreviewResponse getNormalOrdersPreviewByMemberId(Long memberId);

    // 회원 ID로 공동 주문 내역 조회
    MyOrderResponse getGroupOrdersByMemberId(Long memberId);

    // 회원 ID로 공동 주문 내역 미리보기 정보 조회
    MyOrderPreviewResponse getGroupOrdersPreviewByMemberId(Long memberId);

}
