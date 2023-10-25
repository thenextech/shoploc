package nextech.shoploc.controllers;

import nextech.shoploc.models.merchant.MerchantRequestDTO;
import nextech.shoploc.models.merchant.MerchantResponseDTO;
import nextech.shoploc.services.merchant.MerchantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/merchants")
public class MerchantController {

    private final MerchantService merchantService;

    @Autowired
    public MerchantController(MerchantService merchantService) {
        this.merchantService = merchantService;
    }

    @PostMapping("/create")
    public ResponseEntity<MerchantResponseDTO> createMerchant(@RequestBody MerchantRequestDTO merchantRequestDTO) {
        MerchantResponseDTO createdMerchant = merchantService.createMerchant(merchantRequestDTO);
        return new ResponseEntity<>(createdMerchant, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<MerchantResponseDTO> getMerchantById(@PathVariable Long id) {
        MerchantResponseDTO merchant = merchantService.getMerchantById(id);
        if (merchant != null) {
            return new ResponseEntity<>(merchant, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<MerchantResponseDTO> getMerchantByEmail(@PathVariable String email) {
        MerchantResponseDTO merchant = merchantService.getMerchantByEmail(email);
        if (merchant != null) {
            return new ResponseEntity<>(merchant, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/all")
    public ResponseEntity<List<MerchantResponseDTO>> getAllMerchants() {
        List<MerchantResponseDTO> merchants = merchantService.getAllMerchants();
        return new ResponseEntity<>(merchants, HttpStatus.OK);
    }

    // Ajoutez d'autres méthodes de contrôleur si nécessaire.
}
