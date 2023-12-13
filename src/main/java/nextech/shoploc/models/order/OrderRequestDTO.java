package nextech.shoploc.models.order;
import lombok.Getter;
import lombok.Setter;
import nextech.shoploc.domains.enums.Status;

import java.time.LocalDateTime;

@Getter
@Setter
public class OrderRequestDTO {
    private boolean isClickAndCollect;
    private double totalPrice;
    private LocalDateTime creationDate;
    private Status status;
    private Long userId;
}
