package nextech.shoploc.services.orderLine;

import nextech.shoploc.domains.Order;
import nextech.shoploc.domains.OrderLine;
import nextech.shoploc.domains.Product;
import nextech.shoploc.models.orderLine.OrderLineRequestDTO;
import nextech.shoploc.models.orderLine.OrderLineResponseDTO;
import nextech.shoploc.repositories.OrderLineRepository;
import nextech.shoploc.repositories.OrderRepository;
import nextech.shoploc.repositories.ProductRepository;
import nextech.shoploc.utils.ModelMapperUtils;
import nextech.shoploc.utils.exceptions.NotFoundException;
import org.modelmapper.Converter;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderLineServiceImpl implements OrderLineService {

    private final OrderLineRepository orderLineRepository;
    private final OrderRepository orderRepository;

    private final ModelMapperUtils modelMapperUtils;

    public OrderLineServiceImpl(OrderLineRepository orderLineRepository,
                                OrderRepository orderRepository, ProductRepository productRepository, ModelMapperUtils modelMapperUtils) {
        this.orderLineRepository = orderLineRepository;
        this.orderRepository = orderRepository;
        this.modelMapperUtils = modelMapperUtils;
        // Mapper & Converter
        Converter<Long, Product> convertIdentifierToProduct = context -> productRepository.findById(context.getSource())
                .orElseThrow(() -> new NotFoundException("Product not found with ID: " + context.getSource()));
        Converter<Long, Order> convertIdentifierToOrder = context -> this.orderRepository.findById(context.getSource())
                .orElseThrow(() -> new NotFoundException("Order not found with ID: " + context.getSource()));


        // OrderLineRequestDTO -> OrderLine
        this.modelMapperUtils.getModelMapper().typeMap(OrderLineRequestDTO.class, OrderLine.class).addMappings(mapper -> {
            mapper.when(ctx -> ctx.getSource() != null)
                    .using(convertIdentifierToOrder)
                    .map(OrderLineRequestDTO::getOrderId, OrderLine::setOrder);
            mapper.when(ctx -> ctx.getSource() != null)
                    .using(convertIdentifierToProduct)
                    .map(OrderLineRequestDTO::getProductId, OrderLine::setProduct);
        });

        // OrderLine -> OrderLineResponseDTO
        this.modelMapperUtils.getModelMapper().typeMap(OrderLine.class, OrderLineResponseDTO.class).addMappings(mapper -> {
            mapper.map(src -> src.getProduct().getProductId(), OrderLineResponseDTO::setProductId);
            mapper.when(ctx -> ctx.getSource() != null)
                    .map(src -> src.getOrder().getOrderId(), OrderLineResponseDTO::setOrderId);
        });

    }

    @Override
    public OrderLineResponseDTO createOrderLine(
            final OrderLineRequestDTO orderLineRequestDTO) {
        OrderLine orderLine = modelMapperUtils.getModelMapper().map(orderLineRequestDTO, OrderLine.class);
        orderLine = orderLineRepository.save(orderLine);
        return modelMapperUtils.getModelMapper().map(orderLine, OrderLineResponseDTO.class);
    }

    @Override
    public OrderLineResponseDTO getOrderLineById(Long id) {
        OrderLine orderLine = orderLineRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("OrderLine not found with ID: " + id));

        return modelMapperUtils.getModelMapper().map(orderLine, OrderLineResponseDTO.class);
    }

    @Override
    public List<OrderLineResponseDTO> getAllOrderLines() {
        List<OrderLine> orderLines = orderLineRepository.findAll();
        return orderLines.stream()
                .map(orderLine -> modelMapperUtils.getModelMapper().map(orderLine, OrderLineResponseDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public void deleteOrderLine(Long id) {
        if (!orderLineRepository.existsById(id)) {
            throw new NotFoundException("OrderLine not found with ID: " + id);
        }
        orderLineRepository.deleteById(id);
    }

    @Override
    public OrderLineResponseDTO updateOrderLine(Long id, OrderLineRequestDTO orderLineRequestDTO) {
        OrderLine orderLine = orderLineRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("OrderLine not found with ID: " + id));

        modelMapperUtils.getModelMapper().map(orderLineRequestDTO, orderLine);
        orderLine = orderLineRepository.save(orderLine);

        return modelMapperUtils.getModelMapper().map(orderLine, OrderLineResponseDTO.class);
    }

}
