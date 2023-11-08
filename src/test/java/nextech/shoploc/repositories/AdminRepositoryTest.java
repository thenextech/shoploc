package nextech.shoploc.repositories;

import nextech.shoploc.domains.Admin;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
public class AdminRepositoryTest {

    @Autowired
    private AdminRepository adminRepository;

    @Test
    public void testFindAdminByEmail() {
        Admin admin = new Admin();
        admin.setEmail("test@admin.com");
        adminRepository.save(admin);

        Optional<Admin> found = adminRepository.findAdminByEmail("test@admin.com");

        assertTrue(found.isPresent());
        assertEquals("test@admin.com", found.get().getEmail());
    }

}