package dorakdorak.domain.order.service;

import dorakdorak.domain.order.dto.OrderDto;
import dorakdorak.domain.order.dto.response.*;
import dorakdorak.domain.order.enums.OrderStatus;
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

    @Override
    @Transactional
    public void cancelOrder(Long orderId) {
        OrderDto order = orderMapper.findById(orderId)
            .orElseThrow(() -> new BusinessException(ErrorCode.ORDER_NOT_FOUND));

        if (order.getOrderStatus().equals(OrderStatus.PAYMENT_COMPLETED.name()) &&
            !order.getOrderStatus().equals(OrderStatus.GONGGU_OPEN.name())) {
            throw new BusinessException(ErrorCode.CANNOT_CANCEL_ORDER);
        }

        // 주문 아이템을 결제 취소 상태로 변경, QR 코드 무효화
        orderMapper.updateStatus(orderId, OrderStatus.PAYMENT_CANCELLED.name());
        List<Long> itemIds = orderMapper.findItemIdsByOrderId(orderId);
        for (Long itemId : itemIds) {
            orderMapper.updateOrderItemStatusAndQr(itemId, OrderStatus.PAYMENT_CANCELLED.name(), "", "");
        }
    }
}
