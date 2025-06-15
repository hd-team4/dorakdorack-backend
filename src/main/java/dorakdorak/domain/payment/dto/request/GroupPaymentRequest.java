package dorakdorak.domain.payment.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class GroupPaymentRequest {

    @NotEmpty
    private List<@Valid OrderItemRequest> orderItems;

    @NotNull
    private LocalDate arriveAt;

    @Min(0) @Max(23)
    private int arriveTime;
}
