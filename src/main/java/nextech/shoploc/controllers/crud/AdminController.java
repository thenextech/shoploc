package nextech.shoploc.controllers.crud;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.v3.oas.annotations.Operation;
import nextech.shoploc.models.admin.AdminRequestDTO;
import nextech.shoploc.models.admin.AdminResponseDTO;
import nextech.shoploc.services.admin.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/admins")
@Api(tags = "Admins")
public class AdminController {

    private final AdminService adminService;
    PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Autowired
    public AdminController(final AdminService adminService) {
        this.adminService = adminService;
    }

    @PostMapping("/create")
    @ApiOperation(value = "Create an admin", notes = "Creates a new admin")
    public ResponseEntity<AdminResponseDTO> createAdmin(@RequestBody AdminRequestDTO adminRequestDTO) {
        // Hasher le mot de passe avec BCrypt
        String hashedPassword = passwordEncoder.encode(adminRequestDTO.getPassword());

        // Remplacez le mot de passe en clair par le mot de passe haché
        adminRequestDTO.setPassword(hashedPassword);

        AdminResponseDTO createdAdmin = adminService.createAdmin(adminRequestDTO);
        if (createdAdmin != null) {
            return new ResponseEntity<>(createdAdmin, HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{id}")
    @ApiOperation(value = "Get admin by ID", notes = "Retrieve an admin by their ID")
    public ResponseEntity<AdminResponseDTO> getAdminById(
            @ApiParam(value = "ID de l'administrateur", required = true) @PathVariable Long id) {
        AdminResponseDTO admin = adminService.getAdminById(id);
        if (admin != null) {
            return new ResponseEntity<>(admin, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/email/{email}")
    @ApiOperation(value = "Get admin by email", notes = "Retrieve an admin by their email address")
    public ResponseEntity<AdminResponseDTO> getAdminByEmail(
            @ApiParam(value = "Adresse e-mail de l'administrateur", required = true) @PathVariable String email) {
        AdminResponseDTO admin = adminService.getAdminByEmail(email);
        if (admin != null) {
            return new ResponseEntity<>(admin, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/all")
    @Operation(summary = "Get all Admins")
    public ResponseEntity<List<AdminResponseDTO>> getAllAdmins() {
        List<AdminResponseDTO> admins = adminService.getAllAdmins();
        return new ResponseEntity<>(admins, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    @ApiOperation(value = "Update admin", notes = "Update an existing admin by their ID")
    public ResponseEntity<AdminResponseDTO> updateUser(@PathVariable Long id, @RequestBody AdminResponseDTO adminRequestDTO) {
        Optional<AdminResponseDTO> adminResponseDTO = Optional.ofNullable(adminService.updateAdmin(id, adminRequestDTO));
        return adminResponseDTO.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    @ApiOperation(value = "Delete a admin", notes = "Delete a admin by their ID")
    public ResponseEntity<Void> deleteAdmin(@PathVariable Long id) {
        adminService.deleteAdmin(id);
        return ResponseEntity.noContent().build();
    }


    @GetMapping("/stats")
    @ApiOperation(value = "Get sales statistics", notes = "Retrieve sales statistics for a merchant")
    public ResponseEntity<Map<String, Object>> getSalesStatistics(
            @RequestParam Long merchantId,
            @RequestParam(required = false) String startDate,
            @RequestParam(required = false) String endDate) {

        return ResponseEntity.ok(adminService.getSalesStatistics(startDate,endDate));
    }

}
