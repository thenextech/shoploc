package nextech.shoploc.models.order_line;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderLineRequestDTO {
    private Long orderId;
    private Long productId;
    private String productName;
    private int quantity;
    private double unitPrice;
    private String clientName;
    private Long merchantId;
    private String orderStatus;
}
