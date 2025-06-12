package dorakdorak.domain.order.service;

import dorakdorak.domain.order.dto.response.*;
import dorakdorak.domain.order.mapper.OrderMapper;
import dorakdorak.global.error.ErrorCode;
import dorakdorak.global.error.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class OrderServiceImpl implements OrderService{

    private final OrderMapper orderMapper;

    @Override
    public MyOrderResponse getNormalOrdersByMemberId(Long memberId) {
        List<MyOrderResponseDto> myOrders = orderMapper.findNormalOrdersByMemberId(memberId);
        if (myOrders == null) {
            throw new BusinessException(ErrorCode.ORDER_DATA_ACCESS_ERROR);
        }

        for(MyOrderResponseDto myOrder : myOrders){
            List<MyOrderItemResponseDto> items = orderMapper.findItemsByOrderId(myOrder.getOrderId());
            if (items == null) {
                throw new BusinessException(ErrorCode.ORDER_DATA_ACCESS_ERROR);
            }

            myOrder.setItems(items);
        }

        return new MyOrderResponse(myOrders);
    }

    @Override
    public MyOrderPreviewResponse getNormalOrdersPreviewByMemberId(Long memberId) {
        List<MyOrderPreviewResponseDto> myOrdersPreview = orderMapper.findNormalOrdersPreviewByMemberId(memberId);
        if (myOrdersPreview == null) {
            throw new BusinessException(ErrorCode.ORDER_DATA_ACCESS_ERROR);
        }

        return new MyOrderPreviewResponse(myOrdersPreview);
    }

    @Override
    public MyOrderResponse getGroupOrdersByMemberId(Long memberId) {
        List<MyOrderResponseDto> myOrders = orderMapper.findGroupOrdersByMemberId(memberId);
        if (myOrders == null) {
            throw new BusinessException(ErrorCode.ORDER_DATA_ACCESS_ERROR);
        }

        for(MyOrderResponseDto myOrder : myOrders){
            List<MyOrderItemResponseDto> items = orderMapper.findItemsByOrderId(myOrder.getOrderId());
            if (items == null) {
                throw new BusinessException(ErrorCode.ORDER_DATA_ACCESS_ERROR);
            }

            myOrder.setItems(items);
        }

        return new MyOrderResponse(myOrders);
    }

    @Override
    public MyOrderPreviewResponse getGroupOrdersPreviewByMemberId(Long memberId) {
        List<MyOrderPreviewResponseDto> myOrdersPreview = orderMapper.findGroupOrdersPreviewByMemberId(memberId);
        if (myOrdersPreview == null) {
            throw new BusinessException(ErrorCode.ORDER_DATA_ACCESS_ERROR);
        }

        return new MyOrderPreviewResponse(myOrdersPreview);
    }
}
