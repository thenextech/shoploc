package nextech.shoploc.services.benefitsClient;

import nextech.shoploc.models.benefits_client.BenefitsClientRequestDTO;
import nextech.shoploc.models.benefits_client.BenefitsClientResponseDTO;

import java.util.List;

public interface BenefitsClientService {
    BenefitsClientResponseDTO createBenefitsClient(BenefitsClientRequestDTO benefitsRequestDTO);

    BenefitsClientResponseDTO getBenefitsClientById(Long id);

    List<BenefitsClientResponseDTO> getAllBenefitsClients();

    void deleteBenefitsClient(Long id);

    BenefitsClientResponseDTO updateBenefitsClient(Long id, BenefitsClientRequestDTO benefitsRequestDTO);
}
