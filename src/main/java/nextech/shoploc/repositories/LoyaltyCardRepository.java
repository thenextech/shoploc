package nextech.shoploc.repositories;

import nextech.shoploc.domains.Client;
import nextech.shoploc.domains.LoyaltyCard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LoyaltyCardRepository extends JpaRepository<LoyaltyCard, Long> {

    Optional<LoyaltyCard> getLoyaltyCardByClient(Client client);
 
}
