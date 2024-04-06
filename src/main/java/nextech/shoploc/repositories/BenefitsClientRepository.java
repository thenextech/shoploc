package nextech.shoploc.repositories;

import nextech.shoploc.domains.Benefits;
import nextech.shoploc.domains.BenefitsClient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BenefitsClientRepository extends JpaRepository<BenefitsClient, Long> {
    @Query("SELECT b FROM BenefitsClient b WHERE b.client.userId = :userId")
    List<BenefitsClient> findByClientId(@Param("userId") Long userId);

}
