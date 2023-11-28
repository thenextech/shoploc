package nextech.shoploc.services.categoryProduct;

import nextech.shoploc.domains.CategoryProduct;
import nextech.shoploc.domains.Merchant;
import nextech.shoploc.models.categoryProduct.CategoryProductRequestDTO;
import nextech.shoploc.models.categoryProduct.CategoryProductResponseDTO;
import nextech.shoploc.models.merchant.MerchantRequestDTO;
import nextech.shoploc.repositories.CategoryProductRepository;
import nextech.shoploc.repositories.MerchantRepository;
import nextech.shoploc.utils.ModelMapperUtils;
import nextech.shoploc.utils.exceptions.NotFoundException;
import org.modelmapper.Converter;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CategoryProductServiceImpl implements CategoryProductService {

    private final CategoryProductRepository categoryRepository;
    private final MerchantRepository merchantRepository;
    private final ModelMapperUtils modelMapperUtils;


    public CategoryProductServiceImpl(CategoryProductRepository categoryRepository, MerchantRepository merchantRepository, ModelMapperUtils modelMapperUtils) {
        this.categoryRepository = categoryRepository;
        this.merchantRepository = merchantRepository;
        this.modelMapperUtils = modelMapperUtils;

        Converter<Long, Merchant> convertIdentifierToMerchant = context -> this.merchantRepository.findById(context.getSource())
                .orElseThrow(() -> new NotFoundException("Merchant not found with ID: " + context.getSource()));

        //Mapping MerchantRequestDTO -> Merchant
        this.modelMapperUtils.getModelMapper().typeMap(MerchantRequestDTO.class, Merchant.class)
                .addMappings(mapper -> mapper.when(ctx -> ctx.getSource() != null)
                        .using(convertIdentifierToMerchant)
                        .map(MerchantRequestDTO::getUserId, Merchant::setUserId));

        //Mapping CategoryProduct -> CategoryProductResponseDTO
        this.modelMapperUtils.getModelMapper().typeMap(CategoryProduct.class, CategoryProductResponseDTO.class).addMappings(mapper -> mapper.map(src -> src.getMerchant().getUserId(), CategoryProductResponseDTO::setUserId));
        //Mapping CategoryProductRequestDTO -> CategoryProduct
        this.modelMapperUtils.getModelMapper().typeMap(CategoryProductRequestDTO.class, CategoryProduct.class).addMappings(mapper -> mapper.map(src -> src.getUserId(), CategoryProduct::setMerchant));
    }

    @Override
    public CategoryProductResponseDTO createCategoryProduct(CategoryProductRequestDTO categoryRequestDTO) {
        CategoryProduct category = modelMapperUtils.getModelMapper().map(categoryRequestDTO, CategoryProduct.class);
        category.setMerchant(merchantRepository.findMerchantByUserId(categoryRequestDTO.getUserId()).get());
        category = categoryRepository.save(category);
        return modelMapperUtils.getModelMapper().map(category, CategoryProductResponseDTO.class);
    }

    @Override
    public CategoryProductResponseDTO getCategoryProductById(Long id) {
        CategoryProduct category = categoryRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Category not found with ID: " + id));
        return modelMapperUtils.getModelMapper().map(category, CategoryProductResponseDTO.class);
    }


    @Override
    public List<CategoryProductResponseDTO> getAllCategories() {
        List<CategoryProduct> categories = categoryRepository.findAll();
        return categories.stream()
                .map(category -> modelMapperUtils.getModelMapper().map(category, CategoryProductResponseDTO.class))
                .collect(Collectors.toList());
    }
    
    @Override
    public void deleteCategoryProduct(Long id) {
        if (!categoryRepository.existsById(id)) {
            throw new NotFoundException("Category not found with ID: " + id);
        }
        categoryRepository.deleteById(id);
    }

    @Override
    public CategoryProductResponseDTO updateCategoryProduct(Long id, CategoryProductRequestDTO categoryRequestDTO) {
        CategoryProduct category = categoryRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Category not found with ID: " + id));

        modelMapperUtils.getModelMapper().map(categoryRequestDTO, category);
        category = categoryRepository.save(category);
        return modelMapperUtils.getModelMapper().map(category, CategoryProductResponseDTO.class);
    }
}
