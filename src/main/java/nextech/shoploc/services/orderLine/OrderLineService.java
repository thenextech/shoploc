package nextech.shoploc.services.orderLine;

import nextech.shoploc.models.orderLine.OrderLineRequestDTO;
import nextech.shoploc.models.orderLine.OrderLineResponseDTO;

import java.util.List;

public interface OrderLineService {

    OrderLineResponseDTO createOrderLine(OrderLineRequestDTO orderLineRequestDTO);

    OrderLineResponseDTO getOrderLineById(Long id);

    List<OrderLineResponseDTO> getAllOrderLines();

    void deleteOrderLine(Long id);

    OrderLineResponseDTO updateOrderLine(Long id, OrderLineRequestDTO orderLineRequestDTO);

}
