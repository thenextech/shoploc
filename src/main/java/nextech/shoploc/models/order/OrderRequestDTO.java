package nextech.shoploc.models.order;
import java.time.LocalDateTime;
import java.util.List;

import lombok.Getter;
import lombok.Setter;
import nextech.shoploc.domains.enums.Status;

@Getter
@Setter
public class OrderRequestDTO {
    private boolean isClickAndCollect;
    private double totalPrice;
    private LocalDateTime creationDate;
    private Status status;
    private List<String> merchantAdresses;
    private Long userId;
}
