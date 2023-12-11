package nextech.shoploc.controllers.crud;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.v3.oas.annotations.Operation;
import nextech.shoploc.models.categoryProduct.CategoryProductRequestDTO;
import nextech.shoploc.models.categoryProduct.CategoryProductResponseDTO;
import nextech.shoploc.services.categoryProduct.CategoryProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/merchant/category")
@Api(tags = "CategoryProduct")
public class CategoryProductController {

    private final CategoryProductService categoryService;

    @Autowired
    public CategoryProductController(CategoryProductService categoryService) {
        this.categoryService = categoryService;
    }

    @PostMapping("/create")
    @ApiOperation(value = "Create a product category", notes = "Creates a new product category for a merchant")
    public ResponseEntity<CategoryProductResponseDTO> createCategory(
            @RequestBody CategoryProductRequestDTO categoryRequestDTO) {
        CategoryProductResponseDTO createdCategory = categoryService.createCategoryProduct(categoryRequestDTO);
        if (createdCategory != null) {
            return new ResponseEntity<>(createdCategory, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{id}")
    @ApiOperation(value = "Get product category by ID", notes = "Retrieve a product category by its ID")
    public ResponseEntity<CategoryProductResponseDTO> getCategoryById(
            @ApiParam(value = "ID of the product category", required = true) @PathVariable Long id) {
        CategoryProductResponseDTO category = categoryService.getCategoryProductById(id);
        if (category != null) {
            return new ResponseEntity<>(category, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/all")
    @Operation(summary = "Get all Product Categories")
    public ResponseEntity<List<CategoryProductResponseDTO>> getAllCategories() {
        List<CategoryProductResponseDTO> categories = categoryService.getAllCategories();
        return new ResponseEntity<>(categories, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    @ApiOperation(value = "Update product category", notes = "Update an existing product category by its ID")
    public ResponseEntity<CategoryProductResponseDTO> updateCategory(
            @PathVariable Long id, @RequestBody CategoryProductRequestDTO categoryRequestDTO) {
        Optional<CategoryProductResponseDTO> updatedCategory =
                Optional.ofNullable(categoryService.updateCategoryProduct(id, categoryRequestDTO));
        return updatedCategory.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    @ApiOperation(value = "Delete a MerchantsCategoriesProducs", notes = "Delete a MerchantsCategoriesProducs by their ID")
    public ResponseEntity<Void> deleteMerchantsCategoriesProducs(@PathVariable Long id) {
        categoryService.deleteCategoryProduct(id);
        return ResponseEntity.noContent().build();
    }
}
