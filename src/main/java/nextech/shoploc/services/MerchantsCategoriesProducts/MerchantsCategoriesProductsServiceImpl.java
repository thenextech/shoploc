package nextech.shoploc.services.MerchantsCategoriesProducts;

import nextech.shoploc.domains.Merchant;
import nextech.shoploc.domains.MerchantsCategoriesProducts;
import nextech.shoploc.models.MerchantsCategoriesProducts.MerchantsCategoriesProductsRequestDTO;
import nextech.shoploc.models.MerchantsCategoriesProducts.MerchantsCategoriesProductsResponseDTO;
import nextech.shoploc.models.merchant.MerchantRequestDTO;
import nextech.shoploc.repositories.MerchantRepository;
import nextech.shoploc.repositories.MerchantsCategoriesProductsRepository;
import nextech.shoploc.utils.ModelMapperUtils;
import nextech.shoploc.utils.exceptions.NotFoundException;
import org.modelmapper.Converter;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class MerchantsCategoriesProductsServiceImpl implements MerchantsCategoriesProductsService {

    private final MerchantsCategoriesProductsRepository categoryRepository;
    private final MerchantRepository merchantRepository;
    private final ModelMapperUtils modelMapperUtils;

    public MerchantsCategoriesProductsServiceImpl(MerchantsCategoriesProductsRepository categoryRepository, MerchantRepository merchantRepository, MerchantRepository merchantRepository1, ModelMapperUtils modelMapperUtils) {
        this.categoryRepository = categoryRepository;
        this.merchantRepository = merchantRepository1;
        this.modelMapperUtils = modelMapperUtils;

        Converter<Long, Merchant> convertIdentifierToMerchant = context -> merchantRepository.findById(context.getSource())
                .orElseThrow(() -> new NotFoundException("Merchant not found with ID: " + context.getSource()));

        //Mapping MerchantRequestDTO -> Merchant
        this.modelMapperUtils.getModelMapper().typeMap(MerchantRequestDTO.class, Merchant.class).addMappings(mapper -> mapper.when(ctx -> ctx.getSource() != null)
                .using(convertIdentifierToMerchant)
                .map(MerchantRequestDTO::getUserId, Merchant::setUserId));

        //Mapping MerchantsCategoriesProducts -> MerchantsCategoriesProductsResponseDTO
        this.modelMapperUtils.getModelMapper().typeMap(MerchantsCategoriesProducts.class, MerchantsCategoriesProductsResponseDTO.class).addMappings(mapper -> mapper.map(src -> src.getMerchant().getUserId(), MerchantsCategoriesProductsResponseDTO::setUserId));
        //Mapping MerchantsCategoriesProductsRequestDTO -> MerchantsCategoriesProducts
        this.modelMapperUtils.getModelMapper().typeMap(MerchantsCategoriesProductsRequestDTO.class, MerchantsCategoriesProducts.class).addMappings(mapper -> mapper.map(src -> src.getUserId(), MerchantsCategoriesProducts::setMerchant));
    }

    @Override
    public MerchantsCategoriesProductsResponseDTO createCategory(MerchantsCategoriesProductsRequestDTO categoryRequestDTO) {
        MerchantsCategoriesProducts category = modelMapperUtils.getModelMapper().map(categoryRequestDTO, MerchantsCategoriesProducts.class);
        category.setMerchant(merchantRepository.findMerchantByUserId(categoryRequestDTO.getUserId()).get());
        category = categoryRepository.save(category);
        return modelMapperUtils.getModelMapper().map(category, MerchantsCategoriesProductsResponseDTO.class);
    }

    @Override
    public MerchantsCategoriesProductsResponseDTO getCategoryById(Long id) {
        MerchantsCategoriesProducts category = categoryRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Category not found with ID: " + id));
        return modelMapperUtils.getModelMapper().map(category, MerchantsCategoriesProductsResponseDTO.class);
    }

    @Override
    public List<MerchantsCategoriesProductsResponseDTO> getAllCategories() {
        List<MerchantsCategoriesProducts> categories = categoryRepository.findAll();
        return categories.stream()
                .map(category -> modelMapperUtils.getModelMapper().map(category, MerchantsCategoriesProductsResponseDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public void deleteCategory(Long id) {
        if (!categoryRepository.existsById(id)) {
            throw new NotFoundException("Category not found with ID: " + id);
        }
        categoryRepository.deleteById(id);
    }

    @Override
    public MerchantsCategoriesProductsResponseDTO updateCategory(Long id, MerchantsCategoriesProductsRequestDTO categoryRequestDTO) {
        MerchantsCategoriesProducts category = categoryRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Category not found with ID: " + id));

        modelMapperUtils.getModelMapper().map(categoryRequestDTO, category);
        category = categoryRepository.save(category);
        return modelMapperUtils.getModelMapper().map(category, MerchantsCategoriesProductsResponseDTO.class);
    }
}
