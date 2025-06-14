package dorakdorak.domain.order.mapper;

import dorakdorak.domain.order.dto.OrderDto;
import dorakdorak.domain.order.dto.OrderItemDto;
import dorakdorak.domain.order.dto.response.MyOrderAmountResponseDto;
import dorakdorak.domain.order.dto.response.MyOrderResponseDto;
import dorakdorak.domain.order.dto.response.MyOrderItemResponseDto;
import dorakdorak.domain.order.dto.response.MyOrderPreviewResponseDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface OrderMapper {

    List<MyOrderResponseDto> findNormalOrdersByMemberId(@Param("memberId") Long memberId);

    List<MyOrderPreviewResponseDto> findNormalOrdersPreviewByMemberId(@Param("memberId") Long memberId);

    List<MyOrderResponseDto> findGroupOrdersByMemberId(@Param("memberId") Long memberId);

    List<MyOrderPreviewResponseDto> findGroupOrdersPreviewByMemberId(@Param("memberId") Long memberId);

    List<MyOrderItemResponseDto> findItemsByOrderId(@Param("orderId") Long orderId);

    MyOrderAmountResponseDto countNormalOrdersByMemberId(@Param("memberId") Long memberId);

    MyOrderAmountResponseDto countGroupOrdersByMemberId(@Param("memberId") Long memberId);

    void insertOrder(OrderDto orderDto);

    void insertOrderItem(OrderItemDto orderItemDto);

    Long getNextOrderId();
}
