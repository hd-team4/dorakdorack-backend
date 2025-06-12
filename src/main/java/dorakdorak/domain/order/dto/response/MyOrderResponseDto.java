package dorakdorak.domain.order.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MyOrderResponseDto {
    Long orderId;
    String orderCode;
    LocalDateTime orderDate;
    List<MyOrderItemResponseDto> items;
}