package nextech.shoploc.controllers.crud;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import nextech.shoploc.models.benefits_client.BenefitsClientRequestDTO;
import nextech.shoploc.models.benefits_client.BenefitsClientResponseDTO;
import nextech.shoploc.services.benefitsClient.BenefitsClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/benefitsclients")
@Api(tags = "BenefitsClient")
public class BenefitsClientController {

    private final BenefitsClientService benefitsClientService;

    @Autowired
    public BenefitsClientController(final BenefitsClientService benefitsClientService) {
        this.benefitsClientService = benefitsClientService;
    }

    @PostMapping("/create")
    @ApiOperation(value = "Create a benefits client", notes = "Creates a new benefits client")
    public ResponseEntity<BenefitsClientResponseDTO> createBenefitsClient(@RequestBody BenefitsClientRequestDTO benefitsClientRequestDTO) {
        BenefitsClientResponseDTO createdBenefitsClient = benefitsClientService.createBenefitsClient(benefitsClientRequestDTO);
        return new ResponseEntity<>(createdBenefitsClient, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    @ApiOperation(value = "Get benefits client by ID", notes = "Retrieve a benefits client by its ID")
    public ResponseEntity<BenefitsClientResponseDTO> getBenefitsClientById(@PathVariable Long id) {
        BenefitsClientResponseDTO benefitsClient = benefitsClientService.getBenefitsClientById(id);
        if (benefitsClient != null) {
            return new ResponseEntity<>(benefitsClient, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/all")
    @ApiOperation(value = "Get all benefits clients", notes = "Retrieve all benefits clients")
    public ResponseEntity<List<BenefitsClientResponseDTO>> getAllBenefitsClients() {
        List<BenefitsClientResponseDTO> benefitsClients = benefitsClientService.getAllBenefitsClients();
        return new ResponseEntity<>(benefitsClients, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    @ApiOperation(value = "Update a benefits client", notes = "Update an existing benefits client by its ID")
    public ResponseEntity<BenefitsClientResponseDTO> updateBenefitsClient(@PathVariable Long id, @RequestBody BenefitsClientRequestDTO benefitsClientRequestDTO) {
        BenefitsClientResponseDTO updatedBenefitsClient = benefitsClientService.updateBenefitsClient(id, benefitsClientRequestDTO);
        if (updatedBenefitsClient != null) {
            return new ResponseEntity<>(updatedBenefitsClient, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}")
    @ApiOperation(value = "Delete a benefits client", notes = "Delete a benefits client by its ID")
    public ResponseEntity<Void> deleteBenefitsClient(@PathVariable Long id) {
        benefitsClientService.deleteBenefitsClient(id);
        return ResponseEntity.noContent().build();
    }
}
