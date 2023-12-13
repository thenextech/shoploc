package nextech.shoploc.repositories;

import nextech.shoploc.domains.Benefits;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BenefitsRepository extends JpaRepository<Benefits, Long> {

}
