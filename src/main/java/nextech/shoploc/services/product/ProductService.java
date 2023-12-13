package nextech.shoploc.services.product;


import nextech.shoploc.models.product.ProductRequestDTO;
import nextech.shoploc.models.product.ProductResponseDTO;

import java.util.List;

public interface ProductService {
    ProductResponseDTO createProduct(ProductRequestDTO categoryRequestDTO);

    ProductResponseDTO getProductById(Long id);

    List<ProductResponseDTO> getProductsByOrderLineId(Long categoryId);

    List<ProductResponseDTO> getAllProductsOfCategory(Long categoryId);

    List<ProductResponseDTO> getAllProducts();

    void deleteProduct(Long id);

    ProductResponseDTO updateProduct(Long id, ProductRequestDTO categoryRequestDTO);
}
