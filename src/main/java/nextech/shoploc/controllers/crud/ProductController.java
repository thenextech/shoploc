package nextech.shoploc.controllers.crud;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.v3.oas.annotations.Operation;
import nextech.shoploc.models.product.ProductRequestDTO;
import nextech.shoploc.models.product.ProductResponseDTO;
import nextech.shoploc.services.product.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/merchant/product")
@Api(tags = "Product")
public class ProductController {

    private final ProductService productService;

    @Autowired
    public ProductController(final ProductService productService) {
        this.productService = productService;
    }

    @PostMapping("/create")
    @ApiOperation(value = "Create a product product", notes = "Creates a new product product for a merchant")
    public ResponseEntity<ProductResponseDTO> createProduct(
            @RequestBody ProductRequestDTO productRequestDTO) {
        ProductResponseDTO createdProduct = productService.createProduct(productRequestDTO);
        if (createdProduct != null) {
            return new ResponseEntity<>(createdProduct, HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{id}")
    @ApiOperation(value = "Get product product by ID", notes = "Retrieve a product product by its ID")
    public ResponseEntity<ProductResponseDTO> getProductById(
            @ApiParam(value = "ID of the product product", required = true) @PathVariable Long id) {
        ProductResponseDTO product = productService.getProductById(id);
        if (product != null) {
            return new ResponseEntity<>(product, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/orderLine")
    @ApiOperation(value = "Get product by orderLine Id", notes = "Retrieve a product category by orderLine Id")
    public ResponseEntity<List<ProductResponseDTO>> getAllProductsByOrderLineId(
            @ApiParam(value = "ID of the orderLine ", required = true) @RequestParam Long orderLineId) {
        List<ProductResponseDTO> products = productService.getProductsByOrderLineId(orderLineId);
        if (products != null) {
            return new ResponseEntity<>(products, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/category")
    @ApiOperation(value = "Get product by category Id", notes = "Retrieve a product category by category Id")
    public ResponseEntity<List<ProductResponseDTO>> getAllProductsOfCategory(
            @ApiParam(value = "ID of the product ", required = true) @RequestParam Long categoryId) {
        List<ProductResponseDTO> products = productService.getAllProductsOfCategory(categoryId);
        if (products != null) {
            return new ResponseEntity<>(products, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/all")
    @Operation(summary = "Get all Product Categories")
    public ResponseEntity<List<ProductResponseDTO>> getAllProducts() {
        List<ProductResponseDTO> categories = productService.getAllProducts();
        return new ResponseEntity<>(categories, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    @ApiOperation(value = "Update product product", notes = "Update an existing product product by its ID")
    public ResponseEntity<ProductResponseDTO> updateProduct(
            @PathVariable Long id, @RequestBody ProductRequestDTO productRequestDTO) {
        Optional<ProductResponseDTO> updatedProduct =
                Optional.ofNullable(productService.updateProduct(id, productRequestDTO));
        return updatedProduct.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("{id}")
    @ApiOperation(value = "Delete a product", notes = "Delete a MerchantsCategoriesProducs by their ID")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }


}
