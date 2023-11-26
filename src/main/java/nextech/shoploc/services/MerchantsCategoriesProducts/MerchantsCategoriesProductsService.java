package nextech.shoploc.services.MerchantsCategoriesProducts;


import nextech.shoploc.models.MerchantsCategoriesProducts.MerchantsCategoriesProductsRequestDTO;
import nextech.shoploc.models.MerchantsCategoriesProducts.MerchantsCategoriesProductsResponseDTO;

import java.util.List;

public interface MerchantsCategoriesProductsService {
    MerchantsCategoriesProductsResponseDTO createCategory(MerchantsCategoriesProductsRequestDTO categoryRequestDTO);

    MerchantsCategoriesProductsResponseDTO getCategoryById(Long id);

    List<MerchantsCategoriesProductsResponseDTO> getAllCategories();

    void deleteCategory(Long id);

    MerchantsCategoriesProductsResponseDTO updateCategory(Long id, MerchantsCategoriesProductsRequestDTO categoryRequestDTO);
}
