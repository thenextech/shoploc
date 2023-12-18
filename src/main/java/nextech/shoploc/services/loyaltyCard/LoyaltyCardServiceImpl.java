package nextech.shoploc.services.loyaltyCard;

import nextech.shoploc.domains.LoyaltyCard;
import nextech.shoploc.domains.User;
import nextech.shoploc.models.loyalty_card.LoyaltyCardRequestDTO;
import nextech.shoploc.models.loyalty_card.LoyaltyCardResponseDTO;
import nextech.shoploc.repositories.LoyaltyCardRepository;
import nextech.shoploc.repositories.UserRepository;
import nextech.shoploc.utils.ModelMapperUtils;
import nextech.shoploc.utils.exceptions.NotFoundException;
import org.modelmapper.Converter;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class LoyaltyCardServiceImpl implements LoyaltyCardService {

    private final LoyaltyCardRepository loyaltyCardRepository;
    private final ModelMapperUtils modelMapperUtils;

    public LoyaltyCardServiceImpl(LoyaltyCardRepository loyaltyCardRepository, ModelMapperUtils modelMapperUtils, UserRepository userRepository) {
        this.loyaltyCardRepository = loyaltyCardRepository;
        this.modelMapperUtils = modelMapperUtils;

        // Mapper & Converter
        Converter<Long, User> convertIdentifierToUser = context -> userRepository.findById(context.getSource())
                .orElseThrow(() -> new NotFoundException("User not found with ID: " + context.getSource()));

        this.modelMapperUtils.getModelMapper().typeMap(LoyaltyCardRequestDTO.class, LoyaltyCard.class)
                .addMappings(mapper -> mapper.when(ctx -> ctx.getSource() != null)
                        .using(convertIdentifierToUser)
                        .map(LoyaltyCardRequestDTO::getUserId, LoyaltyCard::setClient));

        this.modelMapperUtils.getModelMapper().typeMap(LoyaltyCard.class, LoyaltyCardResponseDTO.class)
                .addMappings(mapper -> mapper.map(src -> src.getClient().getUserId(), LoyaltyCardResponseDTO::setUserId));
    }

    @Override
    public LoyaltyCardResponseDTO createLoyaltyCard(LoyaltyCardRequestDTO loyaltyCardRequestDTO) {
        LoyaltyCard loyaltyCard = modelMapperUtils.getModelMapper().map(loyaltyCardRequestDTO, LoyaltyCard.class);
        loyaltyCard = loyaltyCardRepository.save(loyaltyCard);
        return modelMapperUtils.getModelMapper().map(loyaltyCard, LoyaltyCardResponseDTO.class);
    }

    @Override
    public LoyaltyCardResponseDTO getLoyaltyCardById(Long id) {
        LoyaltyCard loyaltyCard = loyaltyCardRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("LoyaltyCard not found with ID: " + id));
        return modelMapperUtils.getModelMapper().map(loyaltyCard, LoyaltyCardResponseDTO.class);
    }

    @Override
    public List<LoyaltyCardResponseDTO> getAllLoyaltyCards() {
        List<LoyaltyCard> loyaltyCards = loyaltyCardRepository.findAll();
        return loyaltyCards.stream()
                .map(loyaltyCard -> modelMapperUtils.getModelMapper().map(loyaltyCard, LoyaltyCardResponseDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public void deleteLoyaltyCard(Long id) {
        if (!loyaltyCardRepository.existsById(id)) {
            throw new NotFoundException("LoyaltyCard not found with ID: " + id);
        }
        loyaltyCardRepository.deleteById(id);
    }

    @Override
    public LoyaltyCardResponseDTO updateLoyaltyCard(Long id, LoyaltyCardRequestDTO loyaltyCardRequestDTO) {
        LoyaltyCard loyaltyCard = loyaltyCardRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("LoyaltyCard not found with ID: " + id));

        modelMapperUtils.getModelMapper().map(loyaltyCardRequestDTO, loyaltyCard);
        loyaltyCard = loyaltyCardRepository.save(loyaltyCard);
        return modelMapperUtils.getModelMapper().map(loyaltyCard, LoyaltyCardResponseDTO.class);
    }
}
