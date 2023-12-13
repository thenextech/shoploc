package nextech.shoploc.controllers.crud;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.Operation;
import nextech.shoploc.models.loyalty_card.LoyaltyCardRequestDTO;
import nextech.shoploc.models.loyalty_card.LoyaltyCardResponseDTO;
import nextech.shoploc.services.loyaltyCard.LoyaltyCardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/loyaltyCards")
@Api(tags = "LoyaltyCard")
public class LoyaltyCardController {

    private final LoyaltyCardService loyaltyCardService;

    @Autowired
    public LoyaltyCardController(final LoyaltyCardService loyaltyCardService) {
        this.loyaltyCardService = loyaltyCardService;
    }

    @PostMapping("/create")
    @ApiOperation(value = "Create an loyaltyCard", notes = "Creates a new loyaltyCard")
    public ResponseEntity<LoyaltyCardResponseDTO> createLoyaltyCard(@RequestBody LoyaltyCardRequestDTO loyaltyCardRequestDTO) {
        LoyaltyCardResponseDTO createdLoyaltyCard = loyaltyCardService.createLoyaltyCard(loyaltyCardRequestDTO);
        if (createdLoyaltyCard != null) {
            return new ResponseEntity<>(createdLoyaltyCard, HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{id}")
    @ApiOperation(value = "Get loyaltyCard by ID", notes = "Retrieve an loyaltyCard by its ID")
    public ResponseEntity<LoyaltyCardResponseDTO> getLoyaltyCardById(@PathVariable Long id) {
        LoyaltyCardResponseDTO loyaltyCard = loyaltyCardService.getLoyaltyCardById(id);
        if (loyaltyCard != null) {
            return new ResponseEntity<>(loyaltyCard, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/all")
    @Operation(summary = "Get all LoyaltyCards")
    public ResponseEntity<List<LoyaltyCardResponseDTO>> getAllLoyaltyCards() {
        List<LoyaltyCardResponseDTO> loyaltyCards = loyaltyCardService.getAllLoyaltyCards();
        return new ResponseEntity<>(loyaltyCards, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    @ApiOperation(value = "Update an loyaltyCard", notes = "Update an existing loyaltyCard by its ID")
    public ResponseEntity<LoyaltyCardResponseDTO> updateLoyaltyCard(@PathVariable Long id, @RequestBody LoyaltyCardRequestDTO loyaltyCardRequestDTO) {
        Optional<LoyaltyCardResponseDTO> updatedLoyaltyCard = Optional.ofNullable(loyaltyCardService.updateLoyaltyCard(id, loyaltyCardRequestDTO));
        return updatedLoyaltyCard.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    @ApiOperation(value = "Delete an loyaltyCard", notes = "Delete an loyaltyCard by its ID")
    public ResponseEntity<Void> deleteLoyaltyCard(@PathVariable Long id) {
        loyaltyCardService.deleteLoyaltyCard(id);
        return ResponseEntity.noContent().build();
    }

}
