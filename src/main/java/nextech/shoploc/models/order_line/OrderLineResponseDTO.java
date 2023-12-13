package nextech.shoploc.models.order_line;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderLineResponseDTO extends OrderLineRequestDTO {
    private Long orderLineId;

}
