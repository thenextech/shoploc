package nextech.shoploc.services.benefits;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.Converter;
import org.springframework.stereotype.Service;

import nextech.shoploc.domains.Benefits;
import nextech.shoploc.domains.Product;
import nextech.shoploc.domains.User;
import nextech.shoploc.models.benefits.BenefitsRequestDTO;
import nextech.shoploc.models.benefits.BenefitsResponseDTO;
import nextech.shoploc.repositories.BenefitsRepository;
import nextech.shoploc.repositories.ProductRepository;
import nextech.shoploc.repositories.UserRepository;
import nextech.shoploc.utils.ModelMapperUtils;
import nextech.shoploc.utils.exceptions.NotFoundException;

@Service
public class BenefitsServiceImpl implements BenefitsService {

    private final BenefitsRepository benefitsRepository;

    private final ModelMapperUtils modelMapperUtils;

    public BenefitsServiceImpl(BenefitsRepository benefitsRepository, ModelMapperUtils modelMapperUtils, UserRepository userRepository, ProductRepository productRepository) {
        this.benefitsRepository = benefitsRepository;
        this.modelMapperUtils = modelMapperUtils;

        // Mapper & Converter
        Converter<Long, User> convertIdentifierToUser = context -> userRepository.findById(context.getSource())
                .orElseThrow(() -> new NotFoundException("User not found with ID: " + context.getSource()));
        Converter<Long, Product> convertIdentifierToProduct = context -> productRepository.findById(context.getSource())
                .orElseThrow(() -> new NotFoundException("Product not found with ID: " + context.getSource()));

        this.modelMapperUtils.getModelMapper().typeMap(BenefitsRequestDTO.class, Benefits.class)
                .addMappings(mapper -> mapper.when(ctx -> ctx.getSource() != null)
                        .using(convertIdentifierToUser)
                        .map(BenefitsRequestDTO::getUserId, Benefits::setUser))
                .addMappings(mapper -> mapper.when(ctx -> ctx.getSource() != null)
                        .using(convertIdentifierToProduct)
                        .map(BenefitsRequestDTO::getProductId, Benefits::setProduct));

        this.modelMapperUtils.getModelMapper().typeMap(Benefits.class, BenefitsResponseDTO.class)
                .addMappings(mapper -> mapper.map(src -> src.getUser().getUserId(), BenefitsResponseDTO::setUserId))
                .addMappings(mapper -> mapper.map(src -> src.getProduct().getProductId(), BenefitsResponseDTO::setProductId));

    }

    @Override
    public BenefitsResponseDTO createBenefits(BenefitsRequestDTO benefitsRequestDTO) {
        Benefits benefits = modelMapperUtils.getModelMapper().map(benefitsRequestDTO, Benefits.class);
        benefits = benefitsRepository.save(benefits);
        return modelMapperUtils.getModelMapper().map(benefits, BenefitsResponseDTO.class);
    }

    @Override
    public BenefitsResponseDTO getBenefitsById(Long id) {
        Benefits benefits = benefitsRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Benefits not found with ID: " + id));
        return modelMapperUtils.getModelMapper().map(benefits, BenefitsResponseDTO.class);
    }

    @Override
    public List<BenefitsResponseDTO> getAllBenefitss() {
        List<Benefits> benefitss = benefitsRepository.findAll();
        return benefitss.stream()
                .map(benefits -> modelMapperUtils.getModelMapper().map(benefits, BenefitsResponseDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public void deleteBenefits(Long id) {
        if (!benefitsRepository.existsById(id)) {
            throw new NotFoundException("Benefits not found with ID: " + id);
        }
        benefitsRepository.deleteById(id);
    }

    @Override
    public BenefitsResponseDTO updateBenefits(Long id, BenefitsRequestDTO benefitsRequestDTO) {
        Benefits benefits = benefitsRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Benefits not found with ID: " + id));

        modelMapperUtils.getModelMapper().map(benefitsRequestDTO, benefits);
        benefits = benefitsRepository.save(benefits);
        return modelMapperUtils.getModelMapper().map(benefits, BenefitsResponseDTO.class);
    }
    
    @Override
    public List<BenefitsResponseDTO> getBenefitsByUserId(Long userId) {
        List<Benefits> benefitsList = benefitsRepository.findByUserUserId(userId);
        return benefitsList.stream()
                .map(benefits -> modelMapperUtils.getModelMapper().map(benefits, BenefitsResponseDTO.class))
                .collect(Collectors.toList());
    }
}
