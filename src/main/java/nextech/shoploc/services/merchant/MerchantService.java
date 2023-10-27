package nextech.shoploc.services.merchant;

import nextech.shoploc.models.merchant.MerchantRequestDTO;
import nextech.shoploc.models.merchant.MerchantResponseDTO;

import java.util.List;

public interface MerchantService {
    MerchantResponseDTO createMerchant(MerchantRequestDTO merchantRequestDTO);
    MerchantResponseDTO getMerchantById(Long id);
    MerchantResponseDTO getMerchantByEmail(String email);
    List<MerchantResponseDTO> getAllMerchants();
    void deleteMerchant(Long id);
    MerchantResponseDTO updateMerchant(Long id, MerchantRequestDTO merchantRequestDTO);
}