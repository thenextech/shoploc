package nextech.shoploc.repositories;

import nextech.shoploc.domains.Benefits;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BenefitsRepository extends JpaRepository<Benefits, Long> {

    @Query("SELECT b FROM Benefits b WHERE b.user.userId = :userId")
    List<Benefits> findByUserUserId(@Param("userId") Long userId);
}
