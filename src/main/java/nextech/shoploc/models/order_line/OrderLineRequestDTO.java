package nextech.shoploc.models.order_line;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderLineRequestDTO {
    private Long orderId;
    private Long productId;
    private int quantity;
    private double unitPrice;
}
