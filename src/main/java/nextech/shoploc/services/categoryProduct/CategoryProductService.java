package nextech.shoploc.services.categoryProduct;


import nextech.shoploc.models.category_product.CategoryProductRequestDTO;
import nextech.shoploc.models.category_product.CategoryProductResponseDTO;

import java.util.List;

public interface CategoryProductService {
    CategoryProductResponseDTO createCategoryProduct(CategoryProductRequestDTO categoryRequestDTO);

    CategoryProductResponseDTO getCategoryProductById(Long id);

    List<CategoryProductResponseDTO> getCategoryProductByMerchantId(Long merchantId);

    List<CategoryProductResponseDTO> getAllCategories();

    void deleteCategoryProduct(Long id);

    CategoryProductResponseDTO updateCategoryProduct(Long id, CategoryProductRequestDTO categoryRequestDTO);
}
