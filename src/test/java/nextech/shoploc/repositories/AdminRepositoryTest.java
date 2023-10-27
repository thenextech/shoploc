package nextech.shoploc.repositories;

import nextech.shoploc.domains.Admin;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class AdminRepositoryTest {

    @Autowired
    private AdminRepository adminRepository;
    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }
    @Test
    public void testFindByEmail() {
        Optional<Admin> adminResponseDTO = adminRepository.findAdminByEmail("test@admin.com");
        assertTrue(adminResponseDTO.isPresent());
        assertEquals("test@admin.com", adminResponseDTO.get().getEmail());
    }

}
