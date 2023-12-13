package nextech.shoploc.repositories;

import nextech.shoploc.domains.BenefitsClient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BenefitsClientRepository extends JpaRepository<BenefitsClient, Long> {

}
