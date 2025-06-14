package dorakdorak.domain.zeroWaste.service;

import dorakdorak.domain.zeroWaste.dto.response.ZeroWasteJoinResponse;

public interface ZeroWasteService {

  ZeroWasteJoinResponse getJoinInfo(Long orderId, Long orderItemId, Long memberId);
}
