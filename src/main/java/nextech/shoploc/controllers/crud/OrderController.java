package nextech.shoploc.controllers.crud;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.v3.oas.annotations.Operation;
import nextech.shoploc.models.order.OrderRequestDTO;
import nextech.shoploc.models.order.OrderResponseDTO;
import nextech.shoploc.services.order.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/orders")
@Api(tags = "Order")
public class OrderController {

    private final OrderService orderService;

    @Autowired
    public OrderController(final OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping("/create")
    @ApiOperation(value = "Create an order", notes = "Creates a new order")
    public ResponseEntity<OrderResponseDTO> createOrder(@RequestBody OrderRequestDTO orderRequestDTO) {
    	OrderResponseDTO createdOrder = orderService.createOrder(orderRequestDTO);
        if (createdOrder != null) {
            return new ResponseEntity<>(createdOrder, HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{id}")
    @ApiOperation(value = "Get order by ID", notes = "Retrieve an order by its ID")
    public ResponseEntity<OrderResponseDTO> getOrderById(@PathVariable Long id) {
        OrderResponseDTO order = orderService.getOrderById(id);
        if (order != null) {
            return new ResponseEntity<>(order, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/merchant")
    @ApiOperation(value = "Get orders of User Id", notes = "Retrieve a product category by Merchant Id")
    public ResponseEntity<List<OrderResponseDTO>> getAllOrderOfMerchantId(
            @ApiParam(value = "ID of the merchant ", required = true) @RequestParam Long userId) {
        List<OrderResponseDTO> orders = orderService.getOrdersByMerchantId(userId);
        if (orders != null) {
            return new ResponseEntity<>(orders, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/client")
    @ApiOperation(value = "Get orders of User Id", notes = "Retrieve a product category by user Id")
    public ResponseEntity<List<OrderResponseDTO>> getAllOrderOfClient(
            @ApiParam(value = "ID of the user ", required = true) @RequestParam Long clientId) {
        List<OrderResponseDTO> orders = orderService.getAllOrderOfUser(clientId);
        if (orders != null) {
            return new ResponseEntity<>(orders, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/all")
    @Operation(summary = "Get all Orders")
    public ResponseEntity<List<OrderResponseDTO>> getAllOrders() {
        List<OrderResponseDTO> orders = orderService.getAllOrders();
        return new ResponseEntity<>(orders, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    @ApiOperation(value = "Update an order", notes = "Update an existing order by its ID")
    public ResponseEntity<OrderResponseDTO> updateOrder(@PathVariable Long id, @RequestBody OrderRequestDTO orderRequestDTO) {
        Optional<OrderResponseDTO> updatedOrder = Optional.ofNullable(orderService.updateOrder(id, orderRequestDTO));
        return updatedOrder.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("{id}")
    @ApiOperation(value = "Delete an order", notes = "Delete an order by its ID")
    public ResponseEntity<Void> deleteOrder(@PathVariable Long id) {
        orderService.deleteOrder(id);
        return ResponseEntity.noContent().build();
    }

}
