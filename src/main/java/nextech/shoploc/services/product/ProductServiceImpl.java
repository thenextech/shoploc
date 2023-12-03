package nextech.shoploc.services.product;

import nextech.shoploc.domains.CategoryProduct;
import nextech.shoploc.domains.Product;
import nextech.shoploc.models.product.ProductRequestDTO;
import nextech.shoploc.models.product.ProductResponseDTO;
import nextech.shoploc.repositories.CategoryProductRepository;
import nextech.shoploc.repositories.ProductRepository;
import nextech.shoploc.utils.ModelMapperUtils;
import nextech.shoploc.utils.exceptions.NotFoundException;
import org.modelmapper.Converter;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final CategoryProductRepository categoryRepository;
    private final ModelMapperUtils modelMapperUtils;

    public ProductServiceImpl(ProductRepository productRepository, CategoryProductRepository categoryRepository, ModelMapperUtils modelMapperUtils) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
        this.modelMapperUtils = modelMapperUtils;

        // Définition du convertisseur pour CategoryProduct
        Converter<Long, CategoryProduct> convertIdentifierToCategory = context -> this.categoryRepository.findById(context.getSource())
                .orElseThrow(() -> new NotFoundException("Category not found with ID: " + context.getSource()));

        // Mappings pour Product -> ProductResponseDTO
        this.modelMapperUtils.getModelMapper().typeMap(Product.class, ProductResponseDTO.class)
                .addMappings(mapper -> mapper.map(src -> src.getCategoryProduct().getId(), ProductResponseDTO::setCategoryProduct));

        // Mappings pour ProductRequestDTO -> Product
        this.modelMapperUtils.getModelMapper().typeMap(ProductRequestDTO.class, Product.class)
                .addMappings(mapper -> mapper.using(convertIdentifierToCategory)
                        .map(ProductRequestDTO::getCategoryProduct, Product::setCategoryProduct));
    }

    @Override
    public ProductResponseDTO createProduct(ProductRequestDTO productRequestDTO) {
        CategoryProduct category = categoryRepository.findById(productRequestDTO.getCategoryProduct())
                .orElseThrow(() -> new NotFoundException("Category not found with ID: " + productRequestDTO.getCategoryProduct()));

        Product product = modelMapperUtils.getModelMapper().map(productRequestDTO, Product.class);
        product.setCategoryProduct(category);

        product = productRepository.save(product);
        return modelMapperUtils.getModelMapper().map(product, ProductResponseDTO.class);
    }

    @Override
    public ProductResponseDTO getProductById(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Product not found with ID: " + id));
        return modelMapperUtils.getModelMapper().map(product, ProductResponseDTO.class);
    }

    @Override
    public List<ProductResponseDTO> getAllProducts() {
        List<Product> products = productRepository.findAll();
        return products.stream()
                .map(product -> modelMapperUtils.getModelMapper().map(product, ProductResponseDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public void deleteProduct(Long id) {
        if (!productRepository.existsById(id)) {
            throw new NotFoundException("Product not found with ID: " + id);
        }
        productRepository.deleteById(id);
    }

    @Override
    public ProductResponseDTO updateProduct(Long id, ProductRequestDTO productRequestDTO) {
        CategoryProduct category = categoryRepository.findById(productRequestDTO.getCategoryProduct())
                .orElseThrow(() -> new NotFoundException("Category not found with ID: " + productRequestDTO.getCategoryProduct()));

        Product product = productRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Product not found with ID: " + id));

        modelMapperUtils.getModelMapper().map(productRequestDTO, product);
        product.setCategoryProduct(category);

        product = productRepository.save(product);
        return modelMapperUtils.getModelMapper().map(product, ProductResponseDTO.class);
    }
}
