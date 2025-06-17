package dorakdorak.domain.order.mapper;

import dorakdorak.domain.order.dto.AdminOrderDto;
import dorakdorak.domain.order.dto.GroupOrderDto;
import dorakdorak.domain.order.dto.OrderDto;
import dorakdorak.domain.order.dto.OrderItemDto;
import dorakdorak.domain.order.dto.MyOrderAmountDto;
import dorakdorak.domain.order.dto.MyOrderItemDto;
import dorakdorak.domain.order.dto.MyOrderPreviewDto;
import dorakdorak.domain.order.dto.MyOrderDto;
import dorakdorak.domain.order.dto.OrderMailInfoDto;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface OrderMapper {

  List<MyOrderDto> findNormalOrdersByMemberId(@Param("memberId") Long memberId,
      @Param("orderId") Long orderId, @Param("count") Long count);

  List<MyOrderPreviewDto> findNormalOrdersPreviewByMemberId(
      @Param("memberId") Long memberId);

  List<MyOrderDto> findGroupOrdersByMemberId(@Param("memberId") Long memberId,
      @Param("orderId") Long orderId, @Param("count") Long count);

  List<MyOrderPreviewDto> findGroupOrdersPreviewByMemberId(
      @Param("memberId") Long memberId);

  List<MyOrderItemDto> findItemsByOrderId(@Param("orderId") Long orderId);

  Optional<MyOrderAmountDto> countNormalOrdersByMemberId(@Param("memberId") Long memberId);

  Optional<MyOrderAmountDto> countGroupOrdersByMemberId(@Param("memberId") Long memberId);

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

  List<GroupOrderDto> findGroupOrdersAll(@Param("arrive") LocalDateTime arrive,
      @Param("universityId") Long universityId);

  List<GroupOrderDto> findGroupOrdersWithExtra(@Param("arrive") LocalDateTime arrive,
      @Param("universityId") Long universityId,
      @Param("dosirakId") Long dosirakId);

  List<AdminOrderDto> findAdminOrders(@Param("orderId") Long orderId);

  int countAdminOrders();

  Optional<OrderItemDto> findItemById(@Param("orderItemId") Long orderItemId);

  Optional<OrderItemDto> findItemByQrToken(@Param("qrcode") String qrcode);

  void clearQrToken(@Param("orderItemId") Long orderItemId);

  List<OrderMailInfoDto> findOrderMailInfoByOrderId(@Param("orderId") Long orderId);

  List<Long> findGroupOrderIdsByArrivalAt(@Param("targetDate") LocalDate targetDate);

  // TODO: 여기 리턴타입을 DTO로 빼는게 좋아보여요. 혹은 맵키 써야한다고 합니다
  Map<Long, Integer> countDosirakOrdersByOrderId(@Param("orderId") Long orderId);

  int cancelOrderItemsByOrderIdAndDosirakIds(@Param("orderId") Long orderId,
      @Param("dosirakIds") List<Long> dosirakIds);
}
