package nextech.shoploc.services.order;

import nextech.shoploc.models.order.OrderRequestDTO;
import nextech.shoploc.models.order.OrderResponseDTO;

import java.util.List;

public interface OrderService {
    OrderResponseDTO createOrder(OrderRequestDTO orderRequestDTO);

    OrderResponseDTO getOrderById(Long id);

    List<OrderResponseDTO> getAllOrderOfUser(Long userId);

    List<OrderResponseDTO> getOrdersByMerchantId(Long merchantId);

    List<OrderResponseDTO> getAllOrders();

    void deleteOrder(Long id);

    OrderResponseDTO updateOrder(Long id, OrderRequestDTO orderRequestDTO);
}
