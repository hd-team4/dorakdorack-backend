package dorakdorak.domain.order.mapper;

import dorakdorak.domain.order.dto.response.MyOrderAmountResponseDto;
import dorakdorak.domain.order.dto.response.MyOrderItemResponseDto;
import dorakdorak.domain.order.dto.response.MyOrderPreviewResponseDto;
import dorakdorak.domain.order.dto.response.MyOrderResponseDto;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface OrderMapper {

  List<MyOrderResponseDto> findNormalOrdersByMemberId(@Param("memberId") Long memberId,
      @Param("orderId") Long orderId, @Param("count") Long count);

  List<MyOrderPreviewResponseDto> findNormalOrdersPreviewByMemberId(
      @Param("memberId") Long memberId);

  List<MyOrderResponseDto> findGroupOrdersByMemberId(@Param("memberId") Long memberId,
      @Param("orderId") Long orderId, @Param("count") Long count);

  List<MyOrderPreviewResponseDto> findGroupOrdersPreviewByMemberId(
      @Param("memberId") Long memberId);

  List<MyOrderItemResponseDto> findItemsByOrderId(@Param("orderId") Long orderId);

  MyOrderAmountResponseDto countNormalOrdersByMemberId(@Param("memberId") Long memberId);

  MyOrderAmountResponseDto countGroupOrdersByMemberId(@Param("memberId") Long memberId);
}
