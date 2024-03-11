package nextech.shoploc.services.orderLine;

import nextech.shoploc.models.order_line.OrderLineRequestDTO;
import nextech.shoploc.models.order_line.OrderLineResponseDTO;

import java.util.List;

public interface OrderLineService {

    OrderLineResponseDTO createOrderLine(OrderLineRequestDTO orderLineRequestDTO);

    OrderLineResponseDTO getOrderLineById(Long id);
    
    List<OrderLineResponseDTO> getOrderLinesByMerchantId(Long merchantId);

    List<OrderLineResponseDTO> getOrderLinesByOrderId(Long orderId);

    List<OrderLineResponseDTO> getAllOrderLines();

    void deleteOrderLine(Long id);

    OrderLineResponseDTO updateOrderLine(Long id, OrderLineRequestDTO orderLineRequestDTO);

}
