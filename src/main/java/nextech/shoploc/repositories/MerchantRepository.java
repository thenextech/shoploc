package nextech.shoploc.repositories;

import nextech.shoploc.domains.Merchant;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface MerchantRepository extends JpaRepository<Merchant, Long> {

    Optional<Merchant> findMerchantByEmail(String email);

}
