package nextech.shoploc.services.merchant;

import nextech.shoploc.domains.Merchant;
import nextech.shoploc.domains.enums.Status;
import nextech.shoploc.models.merchant.MerchantRequestDTO;
import nextech.shoploc.models.merchant.MerchantResponseDTO;
import nextech.shoploc.repositories.MerchantRepository;
import nextech.shoploc.services.auth.EmailSenderService;
import nextech.shoploc.utils.ModelMapperUtils;
import nextech.shoploc.utils.exceptions.EmailAlreadyExistsException;
import nextech.shoploc.utils.exceptions.MerchantNotFoundException;
import nextech.shoploc.utils.exceptions.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class MerchantServiceImpl implements MerchantService {

    @Autowired
    private PasswordEncoder passwordEncoder;

    private final MerchantRepository merchantRepository;
    private final ModelMapperUtils modelMapperUtils;
    
    @Autowired                                    
    private EmailSenderService emailSenderService;

    public MerchantServiceImpl(MerchantRepository merchantRepository, ModelMapperUtils modelMapperUtils) {
        this.merchantRepository = merchantRepository;
        this.modelMapperUtils = modelMapperUtils;
    }

    @Override
    public MerchantResponseDTO createMerchant(MerchantRequestDTO merchantRequestDTO) throws EmailAlreadyExistsException {
        try {
        	Merchant merchant = modelMapperUtils.getModelMapper().map(merchantRequestDTO, Merchant.class);
            String encodedPassword = passwordEncoder.encode(merchant.getPassword());
            merchant.setPassword(encodedPassword);
            merchant = merchantRepository.save(merchant);
            return modelMapperUtils.getModelMapper().map(merchant, MerchantResponseDTO.class);
        } catch(Exception e) {
        	throw new EmailAlreadyExistsException();
        }
    }

    @Override
    public MerchantResponseDTO getMerchantById(Long id) {
        Merchant merchant = merchantRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Merchant not found with ID: " + id));
        return modelMapperUtils.getModelMapper().map(merchant, MerchantResponseDTO.class);
    }

    @Override
    public MerchantResponseDTO getMerchantByEmail(String email) {
        Merchant merchant = merchantRepository.findMerchantByEmail(email)
                .orElseThrow(() -> new NotFoundException("Merchant not found with email: " + email));
        return modelMapperUtils.getModelMapper().map(merchant, MerchantResponseDTO.class);
    }

    @Override
    public List<MerchantResponseDTO> getAllMerchants() {
        List<Merchant> merchants = merchantRepository.findAll();
        return merchants.stream()
                .map(merchant -> modelMapperUtils.getModelMapper().map(merchant, MerchantResponseDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public void deleteMerchant(Long id) {
        if (!merchantRepository.existsById(id)) {
            throw new NotFoundException("Merchant not found with ID: " + id);
        }
        merchantRepository.deleteById(id);
    }

    @Override
    public MerchantResponseDTO updateMerchant(Long id, MerchantRequestDTO merchantRequestDTO) {
        Merchant merchant = merchantRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Merchant not found with ID: " + id));

        modelMapperUtils.getModelMapper().map(merchantRequestDTO, merchant);
        merchant = merchantRepository.save(merchant);
        return modelMapperUtils.getModelMapper().map(merchant, MerchantResponseDTO.class);
    }
    
    @Override
    public void activateMerchant(Long merchantId) {
    	try {
    		Optional<Merchant> optionalMerchant = merchantRepository.findMerchantByUserId(merchantId);
            if (optionalMerchant.isPresent()) {
                Merchant merchant = optionalMerchant.get();
                merchant.setStatus(Status.ACTIVE);
                //emailSenderService.sendMerchantAccountActivatedEmail(merchant.getEmail());
                merchantRepository.save(merchant); 
            } else {
                throw new MerchantNotFoundException("Commerçant non trouvé avec l'identifiant : " + merchantId);
            }   		
    	} catch(Exception e) {
    		e.printStackTrace();
    	}
    }
}