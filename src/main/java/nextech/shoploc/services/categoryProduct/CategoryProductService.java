package nextech.shoploc.services.categoryProduct;


import nextech.shoploc.models.categoryProduct.CategoryProductRequestDTO;
import nextech.shoploc.models.categoryProduct.CategoryProductResponseDTO;

import java.util.List;

public interface CategoryProductService {
    CategoryProductResponseDTO createCategoryProduct(CategoryProductRequestDTO categoryRequestDTO);

    CategoryProductResponseDTO getCategoryProductById(Long id);

    List<CategoryProductResponseDTO> getAllCategories();

    void deleteCategoryProduct(Long id);

    CategoryProductResponseDTO updateCategoryProduct(Long id, CategoryProductRequestDTO categoryRequestDTO);
}
