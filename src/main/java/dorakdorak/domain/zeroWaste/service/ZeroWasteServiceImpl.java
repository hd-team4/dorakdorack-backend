package dorakdorak.domain.zeroWaste.service;

import dorakdorak.domain.order.dto.OrderDto;
import dorakdorak.domain.order.dto.OrderItemDto;
import dorakdorak.domain.order.dto.response.MyOrderItemResponseDto;
import dorakdorak.domain.order.mapper.OrderMapper;
import dorakdorak.domain.zeroWaste.dto.response.ZeroWasteJoinResponse;
import dorakdorak.global.error.ErrorCode;
import dorakdorak.global.error.exception.EntityNotFoundException;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class ZeroWasteServiceImpl implements ZeroWasteService {

  private final OrderMapper orderMapper;

  @Override
  public ZeroWasteJoinResponse getJoinInfo(Long orderId, Long orderItemId, Long memberId) {
    // 주문 정보 조회
    OrderDto order = orderMapper.findById(orderId)
        .orElseThrow(() -> new EntityNotFoundException(ErrorCode.ORDER_NOT_FOUND.getMessage(), ErrorCode.ORDER_NOT_FOUND));
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy년 MM월 dd일");
    String date = order.getArrivalAt().format(formatter);

    // 주문 아이템 정보 조회
    OrderItemDto item = orderMapper.findItemById(orderItemId)
        .orElseThrow(() -> new EntityNotFoundException(orderItemId + "이 존재하지 않습니다.", ErrorCode.ORDER_ITEM_NOT_FOUND));

    return new ZeroWasteJoinResponse(item.getName(), date, item.getImageUrl());
  }
}
