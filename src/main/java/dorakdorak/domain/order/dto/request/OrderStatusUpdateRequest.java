package dorakdorak.domain.order.dto.request;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class OrderStatusUpdateRequest {
    @NotEmpty
    private String orderStatus;
}
