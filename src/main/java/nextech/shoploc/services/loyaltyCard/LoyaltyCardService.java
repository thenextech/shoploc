package nextech.shoploc.services.loyaltyCard;

import nextech.shoploc.models.loyalty_card.LoyaltyCardRequestDTO;
import nextech.shoploc.models.loyalty_card.LoyaltyCardResponseDTO;

import java.util.List;

public interface LoyaltyCardService {
    LoyaltyCardResponseDTO createLoyaltyCard(LoyaltyCardRequestDTO loyaltyCardRequestDTO);

    LoyaltyCardResponseDTO getLoyaltyCardById(Long id);

    List<LoyaltyCardResponseDTO> getAllLoyaltyCards();

    void deleteLoyaltyCard(Long id);

    LoyaltyCardResponseDTO updateLoyaltyCard(Long id, LoyaltyCardRequestDTO loyaltyCardRequestDTO);

    LoyaltyCardResponseDTO getLoyaltyCardByClient(Long userId);
}
