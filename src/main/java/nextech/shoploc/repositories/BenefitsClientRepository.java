package nextech.shoploc.repositories;

import nextech.shoploc.domains.BenefitsClient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BenefitsClientRepository extends JpaRepository<BenefitsClient, Long> {
    List<BenefitsClient> findByClientId(Long clientId);

}
