package nextech.shoploc.controllers.crud;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import nextech.shoploc.models.benefits.BenefitsRequestDTO;
import nextech.shoploc.models.benefits.BenefitsResponseDTO;
import nextech.shoploc.services.benefits.BenefitsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/benefits")
@Api(tags = "Benefits")
public class BenefitsController {

    private final BenefitsService benefitsService;

    @Autowired
    public BenefitsController(final BenefitsService benefitsService) {
        this.benefitsService = benefitsService;
    }

    @PostMapping("/create")
    @ApiOperation(value = "Create a benefit", notes = "Creates a new benefit")
    public ResponseEntity<BenefitsResponseDTO> createBenefit(@RequestBody BenefitsRequestDTO benefitsRequestDTO) {
        BenefitsResponseDTO createdBenefit = benefitsService.createBenefits(benefitsRequestDTO);
        return new ResponseEntity<>(createdBenefit, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    @ApiOperation(value = "Get benefit by ID", notes = "Retrieve a benefit by its ID")
    public ResponseEntity<BenefitsResponseDTO> getBenefitById(@PathVariable Long id) {
        BenefitsResponseDTO benefit = benefitsService.getBenefitsById(id);
        if (benefit != null) {
            return new ResponseEntity<>(benefit, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/all")
    @ApiOperation(value = "Get all benefits", notes = "Retrieve all benefits")
    public ResponseEntity<List<BenefitsResponseDTO>> getAllBenefits() {
        List<BenefitsResponseDTO> benefits = benefitsService.getAllBenefitss();
        return new ResponseEntity<>(benefits, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    @ApiOperation(value = "Update a benefit", notes = "Update an existing benefit by its ID")
    public ResponseEntity<BenefitsResponseDTO> updateBenefit(@PathVariable Long id, @RequestBody BenefitsRequestDTO benefitsRequestDTO) {
        BenefitsResponseDTO updatedBenefit = benefitsService.updateBenefits(id, benefitsRequestDTO);
        if (updatedBenefit != null) {
            return new ResponseEntity<>(updatedBenefit, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}")
    @ApiOperation(value = "Delete a benefit", notes = "Delete a benefit by its ID")
    public ResponseEntity<Void> deleteBenefit(@PathVariable Long id) {
        benefitsService.deleteBenefits(id);
        return ResponseEntity.noContent().build();
    }
    @GetMapping("/user/{userId}")
    @ApiOperation(value = "Get benefits by User ID", notes = "Retrieve benefits associated with a specific User ID")
    public ResponseEntity<List<BenefitsResponseDTO>> getBenefitsByUserId(@PathVariable Long userId) {
        List<BenefitsResponseDTO> benefits = benefitsService.getBenefitsByUserId(userId);
        if (!benefits.isEmpty()) {
            return new ResponseEntity<>(benefits, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

}
