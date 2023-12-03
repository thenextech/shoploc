package nextech.shoploc.services.order;

import nextech.shoploc.domains.Order;
import nextech.shoploc.domains.User;
import nextech.shoploc.models.order.OrderRequestDTO;
import nextech.shoploc.models.order.OrderResponseDTO;
import nextech.shoploc.repositories.OrderRepository;
import nextech.shoploc.repositories.UserRepository;
import nextech.shoploc.utils.ModelMapperUtils;
import nextech.shoploc.utils.exceptions.NotFoundException;
import org.modelmapper.Converter;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final ModelMapperUtils modelMapperUtils;

    public OrderServiceImpl(OrderRepository orderRepository, ModelMapperUtils modelMapperUtils, UserRepository userRepository) {
        this.orderRepository = orderRepository;
        this.modelMapperUtils = modelMapperUtils;

        // Mapper & Converter
        Converter<Long, User> convertIdentifierToUser = context -> userRepository.findById(context.getSource())
                .orElseThrow(() -> new NotFoundException("User not found with ID: " + context.getSource()));

        this.modelMapperUtils.getModelMapper().typeMap(OrderRequestDTO.class, Order.class)
                .addMappings(mapper -> mapper.when(ctx -> ctx.getSource() != null)
                        .using(convertIdentifierToUser)
                        .map(OrderRequestDTO::getUserId, Order::setUser));

        this.modelMapperUtils.getModelMapper().typeMap(Order.class, OrderResponseDTO.class)
                .addMappings(mapper -> mapper.map(src -> src.getUser().getUserId(), OrderResponseDTO::setUserId));
    }

    @Override
    public OrderResponseDTO createOrder(OrderRequestDTO orderRequestDTO) {
        Order order = modelMapperUtils.getModelMapper().map(orderRequestDTO, Order.class);
        order = orderRepository.save(order);
        return modelMapperUtils.getModelMapper().map(order, OrderResponseDTO.class);
    }

    @Override
    public OrderResponseDTO getOrderById(Long id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Order not found with ID: " + id));
        return modelMapperUtils.getModelMapper().map(order, OrderResponseDTO.class);
    }

    @Override
    public List<OrderResponseDTO> getAllOrders() {
        List<Order> orders = orderRepository.findAll();
        return orders.stream()
                .map(order -> modelMapperUtils.getModelMapper().map(order, OrderResponseDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public void deleteOrder(Long id) {
        if (!orderRepository.existsById(id)) {
            throw new NotFoundException("Order not found with ID: " + id);
        }
        orderRepository.deleteById(id);
    }

    @Override
    public OrderResponseDTO updateOrder(Long id, OrderRequestDTO orderRequestDTO) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Order not found with ID: " + id));

        modelMapperUtils.getModelMapper().map(orderRequestDTO, order);
        order = orderRepository.save(order);
        return modelMapperUtils.getModelMapper().map(order, OrderResponseDTO.class);
    }
}
