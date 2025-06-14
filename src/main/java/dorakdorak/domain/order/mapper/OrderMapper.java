package dorakdorak.domain.order.mapper;

import dorakdorak.domain.order.dto.AdminOrderDto;
import dorakdorak.domain.order.dto.GroupOrderDto;
import dorakdorak.domain.order.dto.OrderDto;
import dorakdorak.domain.order.dto.OrderItemDto;
import dorakdorak.domain.order.dto.response.MyOrderAmountResponseDto;
import dorakdorak.domain.order.dto.response.MyOrderItemResponseDto;
import dorakdorak.domain.order.dto.response.MyOrderPreviewResponseDto;
import dorakdorak.domain.order.dto.response.MyOrderResponseDto;
import java.util.List;
import java.time.LocalDateTime;
import java.util.Optional;
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

  void insertOrder(OrderDto orderDto);

  void insertOrderItem(OrderItemDto orderItemDto);

  Long getNextOrderId();

  Optional<OrderDto> findByMerchantOrderId(@Param("merchantOrderId") String merchantOrderId);

  List<Long> findItemIdsByOrderId(Long orderId);

  void updateStatus(@Param("id") Long id, @Param("orderStatus") String orderStatus);

  void updateOrderItemStatusAndQr(@Param("itemId") Long itemId,
                                  @Param("orderStatus") String orderStatus,
                                  @Param("qrImageUrl") String qrImageUrl,
                                  @Param("qrToken") String qrToken);

  Optional<OrderDto> findById(Long orderId);

  List<GroupOrderDto> findGroupOrdersAll(@Param("arrive") LocalDateTime arrive, @Param("universityId") Long universityId);

  List<GroupOrderDto> findGroupOrdersWithExtra(@Param("arrive") LocalDateTime arrive,
                                               @Param("universityId") Long universityId,
                                               @Param("dosirakId") Long dosirakId);

  List<AdminOrderDto> findAdminOrders(@Param("offset") int offset, @Param("size") int size);

  int countAdminOrders();

  Optional<OrderItemDto> findItemById(@Param("orderItemId") Long orderItemId);

  Optional<OrderItemDto> findItemByQrToken(@Param("qrcode") String qrcode);

  void clearQrToken(@Param("orderItemId") Long orderItemId);

}
