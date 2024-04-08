package nextech.shoploc.repositories;

import nextech.shoploc.domains.Admin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;


public interface AdminRepository extends JpaRepository<Admin, Long> {
    Optional<Admin> findAdminByEmail(String email);
    @Query("SELECT COUNT(c.userId) FROM Client c WHERE c.vfp = true")
    Long countByVfp();

    @Query("SELECT COUNT(c.userId) FROM Client c")
    Long countClients();

    @Query("SELECT COUNT(DISTINCT c.userId) FROM Client c JOIN c.orders o")
    Integer countClientsOrdered();


    @Query("SELECT COUNT(m.userId) FROM Merchant m")
    Integer countMerchants();

    @Query("SELECT cp.categoryName FROM CategoryProduct cp")
    List<String> listeTypesMerchants();
    @Query("SELECT COUNT(m.client.userId) FROM BenefitsClient m")
    Integer countBenefitsClients();


    @Query("SELECT COALESCE(SUM(ol.quantity * ol.unitPrice), 0) " +
            "FROM OrderLine ol ")
    Double calculateAllTotalRevenue();

    @Query("SELECT SUM(bc.benefits.cost) FROM BenefitsClient bc")
    Double calculateTotalCostOfUsedBenefits();

    @Query("SELECT m.businessName, SUM(ol.unitPrice * ol.quantity) AS totalRevenue " +
            "FROM OrderLine ol JOIN ol.order o JOIN ol.product p JOIN p.categoryProduct cp JOIN cp.merchant m " +
            "GROUP BY m.userId, m.businessName " +
            "ORDER BY totalRevenue DESC")
    List<Object[]> calculateRevenuePerMerchant();



}
