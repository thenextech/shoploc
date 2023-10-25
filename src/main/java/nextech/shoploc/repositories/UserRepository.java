package nextech.shoploc.repositories;

import nextech.shoploc.domains.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;


public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findUserByEmail(String email);

}
