package nextech.shoploc.controllers.crud;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.Operation;
import nextech.shoploc.models.merchant.MerchantRequestDTO;
import nextech.shoploc.models.merchant.MerchantResponseDTO;
import nextech.shoploc.services.merchant.MerchantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/merchants")
@Api(tags = "Merchants")
public class MerchantController {

    private final MerchantService merchantService;

    @Autowired
    public MerchantController(MerchantService merchantService) {
        this.merchantService = merchantService;
    }

    @PostMapping("/create")
    @ApiOperation(value = "Create a merchant", notes = "Creates a new merchant")
    public ResponseEntity<MerchantResponseDTO> createMerchant(@RequestBody MerchantRequestDTO merchantRequestDTO) {
        MerchantResponseDTO createdMerchant = merchantService.createMerchant(merchantRequestDTO);
        return new ResponseEntity<>(createdMerchant, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    @ApiOperation(value = "Get merchant by ID", notes = "Retrieve a merchant by their ID")
    public ResponseEntity<MerchantResponseDTO> getMerchantById(@PathVariable Long id) {
        MerchantResponseDTO merchant = merchantService.getMerchantById(id);
        if (merchant != null) {
            return new ResponseEntity<>(merchant, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/email/{email}")
    @ApiOperation(value = "Get merchant by email", notes = "Retrieve a merchant by their email address")
    public ResponseEntity<MerchantResponseDTO> getMerchantByEmail(@PathVariable String email) {
        MerchantResponseDTO merchant = merchantService.getMerchantByEmail(email);
        if (merchant != null) {
            return new ResponseEntity<>(merchant, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/all")
    @Operation(summary = "Get all Merchants")
    public ResponseEntity<List<MerchantResponseDTO>> getAllMerchants() {
        List<MerchantResponseDTO> merchants = merchantService.getAllMerchants();
        return new ResponseEntity<>(merchants, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    @ApiOperation(value = "Update merchant", notes = "Update an existing merchant by their ID")
    public ResponseEntity<MerchantResponseDTO> updateMerchant(@PathVariable Long id, @RequestBody MerchantRequestDTO merchantRequestDTO) {
        Optional<MerchantResponseDTO> merchantResponseDTO = Optional.ofNullable(merchantService.updateMerchant(id, merchantRequestDTO));
        return merchantResponseDTO.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    @ApiOperation(value = "Delete a merchant", notes = "Delete a merchant by their ID")
    public ResponseEntity<Void> deleteMerchant(@PathVariable Long id) {
        merchantService.deleteMerchant(id);
        return ResponseEntity.noContent().build();
    }
}
