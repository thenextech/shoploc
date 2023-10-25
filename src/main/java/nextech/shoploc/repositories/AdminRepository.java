package nextech.shoploc.repositories;

import nextech.shoploc.domains.Admin;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface AdminRepository extends JpaRepository<Admin, Long> {
    Optional<Admin> findAdminByEmail(String email);
}
