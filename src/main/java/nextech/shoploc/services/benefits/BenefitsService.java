package nextech.shoploc.services.benefits;

import nextech.shoploc.models.benefits.BenefitsRequestDTO;
import nextech.shoploc.models.benefits.BenefitsResponseDTO;

import java.util.List;

public interface BenefitsService {
    BenefitsResponseDTO createBenefits(BenefitsRequestDTO benefitsRequestDTO);

    BenefitsResponseDTO getBenefitsById(Long id);

    List<BenefitsResponseDTO> getBenefitsByUserId(Long userId);

    List<BenefitsResponseDTO> getAllBenefitss();

    void deleteBenefits(Long id);

    BenefitsResponseDTO updateBenefits(Long id, BenefitsRequestDTO benefitsRequestDTO);
}
