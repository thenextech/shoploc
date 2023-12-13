package nextech.shoploc.services.benefitsClient;

import nextech.shoploc.domains.Benefits;
import nextech.shoploc.domains.BenefitsClient;
import nextech.shoploc.domains.Client;
import nextech.shoploc.models.benefits_client.BenefitsClientRequestDTO;
import nextech.shoploc.models.benefits_client.BenefitsClientResponseDTO;
import nextech.shoploc.repositories.BenefitsClientRepository;
import nextech.shoploc.repositories.BenefitsRepository;
import nextech.shoploc.repositories.ClientRepository;
import nextech.shoploc.utils.ModelMapperUtils;
import nextech.shoploc.utils.exceptions.NotFoundException;
import org.modelmapper.Converter;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class BenefitsClientServiceImpl implements BenefitsClientService {

    private final BenefitsClientRepository benefitsClientRepository;
    private final ModelMapperUtils modelMapperUtils;

    public BenefitsClientServiceImpl(BenefitsClientRepository benefitsClientRepository,
                                     ModelMapperUtils modelMapperUtils,
                                     ClientRepository clientRepository,
                                     BenefitsRepository benefitsRepository) {
        this.benefitsClientRepository = benefitsClientRepository;
        this.modelMapperUtils = modelMapperUtils;

        // Mapper & Converter
        Converter<Long, Client> convertIdentifierToClient = context -> clientRepository.findById(context.getSource())
                .orElseThrow(() -> new NotFoundException("User not found with ID: " + context.getSource()));
        Converter<Long, Benefits> convertIdentifierToBenefits = context -> benefitsRepository.findById(context.getSource())
                .orElseThrow(() -> new NotFoundException("Benefits not found with ID: " + context.getSource()));

        this.modelMapperUtils.getModelMapper().typeMap(BenefitsClientRequestDTO.class, BenefitsClient.class)
                .addMappings(mapper -> mapper.when(ctx -> ctx.getSource() != null)
                        .using(convertIdentifierToClient)
                        .map(BenefitsClientRequestDTO::getClientId, BenefitsClient::setClient))
                .addMappings(mapper -> mapper.when(ctx -> ctx.getSource() != null)
                        .using(convertIdentifierToBenefits)
                        .map(BenefitsClientRequestDTO::getBenefitsId, BenefitsClient::setBenefits));

        this.modelMapperUtils.getModelMapper().typeMap(BenefitsClient.class, BenefitsClientResponseDTO.class)
                .addMappings(mapper -> mapper.map(src -> src.getClient().getUserId(), BenefitsClientResponseDTO::setClientId))
                .addMappings(mapper -> mapper.map(src -> src.getBenefits().getBenefitsId(), BenefitsClientResponseDTO::setBenefitsId));

    }

    @Override
    public BenefitsClientResponseDTO createBenefitsClient(BenefitsClientRequestDTO benefitsClientRequestDTO) {
        BenefitsClient benefitsClient = modelMapperUtils.getModelMapper().map(benefitsClientRequestDTO, BenefitsClient.class);
        benefitsClient = benefitsClientRepository.save(benefitsClient);
        return modelMapperUtils.getModelMapper().map(benefitsClient, BenefitsClientResponseDTO.class);
    }

    @Override
    public BenefitsClientResponseDTO getBenefitsClientById(Long id) {
        BenefitsClient benefitsClient = benefitsClientRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("BenefitsClient not found with ID: " + id));
        return modelMapperUtils.getModelMapper().map(benefitsClient, BenefitsClientResponseDTO.class);
    }

    @Override
    public List<BenefitsClientResponseDTO> getAllBenefitsClients() {
        List<BenefitsClient> benefitsClients = benefitsClientRepository.findAll();
        return benefitsClients.stream()
                .map(benefitsClient -> modelMapperUtils.getModelMapper().map(benefitsClient, BenefitsClientResponseDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public void deleteBenefitsClient(Long id) {
        if (!benefitsClientRepository.existsById(id)) {
            throw new NotFoundException("BenefitsClient not found with ID: " + id);
        }
        benefitsClientRepository.deleteById(id);
    }

    @Override
    public BenefitsClientResponseDTO updateBenefitsClient(Long id, BenefitsClientRequestDTO benefitsClientRequestDTO) {
        BenefitsClient benefitsClient = benefitsClientRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("BenefitsClient not found with ID: " + id));

        modelMapperUtils.getModelMapper().map(benefitsClientRequestDTO, benefitsClient);
        benefitsClient = benefitsClientRepository.save(benefitsClient);
        return modelMapperUtils.getModelMapper().map(benefitsClient, BenefitsClientResponseDTO.class);
    }
}
