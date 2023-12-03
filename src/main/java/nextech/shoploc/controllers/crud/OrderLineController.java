package nextech.shoploc.controllers.crud;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.Operation;
import nextech.shoploc.models.orderLine.OrderLineRequestDTO;
import nextech.shoploc.models.orderLine.OrderLineResponseDTO;
import nextech.shoploc.services.orderLine.OrderLineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/orderlines")
@Api(tags = "OrderLine")
public class OrderLineController {

    private final OrderLineService orderLineService;

    @Autowired
    public OrderLineController(OrderLineService orderLineService) {
        this.orderLineService = orderLineService;
    }

    @PostMapping("/create")
    @ApiOperation(value = "Create an order line", notes = "Creates a new order line")
    public ResponseEntity<OrderLineResponseDTO> createOrderLine(@RequestBody OrderLineRequestDTO orderLineRequestDTO) {
        OrderLineResponseDTO createdOrderLine = orderLineService.createOrderLine(orderLineRequestDTO);
        if (createdOrderLine != null) {
            return new ResponseEntity<>(createdOrderLine, HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{id}")
    @ApiOperation(value = "Get order line by ID", notes = "Retrieve an order line by its ID")
    public ResponseEntity<OrderLineResponseDTO> getOrderLineById(@PathVariable Long id) {
        OrderLineResponseDTO orderLine = orderLineService.getOrderLineById(id);
        if (orderLine != null) {
            return new ResponseEntity<>(orderLine, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/all")
    @Operation(summary = "Get all Order Lines")
    public ResponseEntity<List<OrderLineResponseDTO>> getAllOrderLines() {
        List<OrderLineResponseDTO> orderLines = orderLineService.getAllOrderLines();
        return new ResponseEntity<>(orderLines, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    @ApiOperation(value = "Update an order line", notes = "Update an existing order line by its ID")
    public ResponseEntity<OrderLineResponseDTO> updateOrderLine(@PathVariable Long id, @RequestBody OrderLineRequestDTO orderLineRequestDTO) {
        Optional<OrderLineResponseDTO> updatedOrderLine = Optional.ofNullable(orderLineService.updateOrderLine(id, orderLineRequestDTO));
        return updatedOrderLine.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    @ApiOperation(value = "Delete an order line", notes = "Delete an order line by its ID")
    public ResponseEntity<Void> deleteOrderLine(@PathVariable Long id) {
        orderLineService.deleteOrderLine(id);
        return ResponseEntity.noContent().build();
    }
}
