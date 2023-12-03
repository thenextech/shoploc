package nextech.shoploc.services;

import nextech.shoploc.repositories.AdminRepository;
import nextech.shoploc.services.admin.AdminService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class AdminServiceTests {

    @Autowired
    private AdminService adminService;

    @Autowired
    private AdminRepository adminRepository;

    private Long adminId;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @AfterEach
    public void teardown() {
        // Vous pouvez ajouter ici la logique pour nettoyer ou réinitialiser les ressources après chaque test
    }
/*
    @Test
    public void testCreateAdmin() {
        AdminRequestDTO adminRequestDTO = new AdminRequestDTO();
        adminRequestDTO.setEmail("test@admin.com");
        adminRequestDTO.setPassword("encodedPassword");

        Optional<Admin> existingAdmin = adminRepository.findAdminByEmail(adminRequestDTO.getEmail());
        if (existingAdmin.isPresent()) {
            // L'administrateur existe déjà, vous pouvez soit lancer une exception, soit mettre à jour l'administrateur existant.
            // Vous pouvez choisir la meilleure approche en fonction de votre logique métier.
        } else {
            AdminResponseDTO adminResponseDTO = adminService.createAdmin(adminRequestDTO);

            assertNotNull(adminResponseDTO);
            assertEquals("test@admin.com", adminResponseDTO.getEmail());
            adminId = adminResponseDTO.getId(); // Stocke l'ID de l'administrateur créé
        }
    }

    @Test
    public void testGetAdminById() {
        // Test de récupération d'administrateur par ID en utilisant l'ID précédemment stocké
        if (adminId != null) {
            AdminResponseDTO adminResponseDTO = adminService.getAdminById(adminId);
            assertNotNull(adminResponseDTO);
            assertEquals(adminId, adminResponseDTO.getId());
        }
    }

    @Test
    public void testGetAdminByEmail() {
        String email = "test@admin.com";
        AdminResponseDTO adminResponseDTO = adminService.getAdminByEmail(email);

        assertNotNull(adminResponseDTO);
        assertEquals(email, adminResponseDTO.getEmail());
    }

    @Test
    public void testGetAllAdmins() {
        List<AdminResponseDTO> adminResponseDTOs = adminService.getAllAdmins();

        assertNotNull(adminResponseDTOs);
        assertFalse(adminResponseDTOs.isEmpty());
    }

    @Test
    public void testDeleteAdmin() {
        // Test de suppression de l'administrateur en utilisant l'ID précédemment stocké
        if (adminId != null) {
            adminService.deleteAdmin(adminId);
            Optional<Admin> adminOptional = adminRepository.findById(adminId);
            assertFalse(adminOptional.isPresent());
        }
    }

    @Test
    public void testUpdateAdmin() {
        // Test de mise à jour de l'administrateur en utilisant l'ID précédemment stocké
        if (adminId != null) {
            AdminRequestDTO adminRequestDTO = new AdminRequestDTO();
            adminRequestDTO.setEmail("updated@admin.com");
            adminRequestDTO.setPassword("updatedPassword");
            AdminResponseDTO adminResponseDTO = adminService.updateAdmin(adminId, adminRequestDTO);
            assertNotNull(adminResponseDTO);
            assertEquals("updated@admin.com", adminResponseDTO.getEmail());
        }
    }

 */
}
