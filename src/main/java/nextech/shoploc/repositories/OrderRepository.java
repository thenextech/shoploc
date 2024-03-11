package nextech.shoploc.repositories;

import nextech.shoploc.domains.Order;
import nextech.shoploc.domains.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findAllByUser(User user);
    @Query("SELECT o FROM Order o JOIN o.orderLines ol JOIN ol.product p JOIN p.categoryProduct cp JOIN cp.merchant m WHERE m.userId = :merchantId")
    List<Order> findOrdersByMerchantId(@Param("merchantId") Long merchantId);

    @Query("SELECT COUNT(DISTINCT o.user.userId) FROM Order o " +
            "JOIN o.orderLines ol " +
            "JOIN ol.product p JOIN p.categoryProduct cp " +
            "JOIN cp.merchant m " +
            "WHERE cp.merchant.userId = :merchantId AND o.creationDate BETWEEN :startDate AND :endDate")
    Integer nombreClientsToMerchantId(@Param("merchantId") Long merchantId);


    @Query("SELECT o FROM Order o " +
            "JOIN o.orderLines ol " +
            "JOIN ol.product p JOIN p.categoryProduct cp " +
            "JOIN cp.merchant m " +
            "WHERE cp.merchant.userId = :merchantId AND o.creationDate BETWEEN :startDate AND :endDate")
    List<Order> findOrdersBetweenDatesByMerchantId(@Param("merchantId") Long merchantId,@Param("startDate") LocalDateTime startDate,
                                                   @Param("endDate") LocalDateTime endDate);

    // Calcule le chiffre d'affaires total pour un marchand dans une plage de dates
    @Query("SELECT COALESCE(SUM(ol.quantity * ol.unitPrice), 0) " +
            "FROM OrderLine ol " +
            "JOIN ol.order o " +
            "JOIN ol.product p " +
            "JOIN p.categoryProduct cp " +
            "WHERE cp.merchant.userId = :merchantId AND o.creationDate BETWEEN :startDate AND :endDate")
    Double calculateTotalRevenue(@Param("merchantId") Long merchantId,
                                 @Param("startDate") LocalDateTime startDate,
                                 @Param("endDate") LocalDateTime endDate);

    // Calcule le chiffre d'affaires quotidien pour un marchand dans une plage de dates
    @Query("SELECT o.creationDate, COALESCE(SUM(ol.quantity * ol.unitPrice), 0) " +
            "FROM OrderLine ol " +
            "JOIN ol.order o " +
            "JOIN ol.product p " +
            "JOIN p.categoryProduct cp " +
            "WHERE cp.merchant.userId = :merchantId AND o.creationDate BETWEEN :startDate AND :endDate " +
            "GROUP BY o.creationDate")
    Double calculateDailyRevenue(@Param("merchantId") Long merchantId,
                                         @Param("startDate") LocalDateTime startDate,
                                         @Param("endDate") LocalDateTime endDate);



    @Query("SELECT COALESCE(SUM(ol.quantity * ol.unitPrice), 0) " +
            "FROM OrderLine ol " +
            "JOIN ol.product p " +
            "JOIN p.categoryProduct cp " +
            "WHERE cp.merchant.userId = :merchantId " +
            "AND ol.order.creationDate BETWEEN :month AND :month")
    Double calculateMonthRevenue(@Param("merchantId") Long merchantId,
                                 @Param("month") LocalDateTime month);

    @Query("SELECT COALESCE(SUM(ol.quantity * ol.unitPrice), 0) " +
            "FROM OrderLine ol " +
            "JOIN ol.product p " +
            "JOIN p.categoryProduct cp " +
            "WHERE cp.merchant.userId = :merchantId " +
            "AND ol.order.creationDate BETWEEN :startDate AND :endDate")
    Double calculateYearlyRevenue(@Param("merchantId") Long merchantId,
                                  @Param("startDate") LocalDateTime startDate,
                                  @Param("endDate") LocalDateTime endDate);

    @Query("SELECT p.categoryProduct.categoryName " +
            "FROM OrderLine ol " +
            "JOIN ol.product p " +
            "JOIN p.categoryProduct cp " +
            "WHERE cp.merchant.userId = :merchantId " +
            "AND ol.order.creationDate BETWEEN :startDate AND :endDate " +
            "GROUP BY p.categoryProduct.categoryName " +
            "ORDER BY SUM(ol.quantity) DESC")
    List<String> findTopCategories(@Param("merchantId") Long merchantId,
                                   @Param("startDate") LocalDateTime startDate,
                                   @Param("endDate") LocalDateTime endDate);

    @Query("SELECT p.name " +
            "FROM OrderLine ol " +
            "JOIN ol.product p " +
            "JOIN p.categoryProduct cp " +
            "WHERE cp.merchant.userId = :merchantId " +
            "AND ol.order.creationDate BETWEEN :startDate AND :endDate " +
            "GROUP BY p.name " +
            "ORDER BY SUM(ol.quantity) DESC")
    List<String> findTopProducts(@Param("merchantId") Long merchantId,
                                 @Param("startDate") LocalDateTime startDate,
                                 @Param("endDate") LocalDateTime endDate);




    @Query("SELECT NEW map(p.categoryProduct.categoryName as category, COALESCE(SUM(ol.quantity * ol.unitPrice), 0) as revenue) " +
            "FROM OrderLine ol " +
            "JOIN ol.product p " +
            "JOIN p.categoryProduct cp " +
            "WHERE cp.merchant.userId = :merchantId AND ol.order.creationDate BETWEEN :startDate AND :endDate " +
            "GROUP BY p.categoryProduct.categoryName " +
            "ORDER BY SUM(ol.quantity) DESC")
    List<Map<String, Double>> findTopCategoriesWithRevenue(@Param("merchantId") Long merchantId,
                                                           @Param("startDate") LocalDateTime startDate,
                                                           @Param("endDate") LocalDateTime endDate);

    @Query("SELECT NEW map(p.name as product, COALESCE(SUM(ol.quantity * ol.unitPrice), 0) as revenue) " +
            "FROM OrderLine ol " +
            "JOIN ol.product p " +
            "JOIN p.categoryProduct cp " +
            "WHERE cp.merchant.userId = :merchantId AND ol.order.creationDate BETWEEN :startDate AND :endDate " +
            "GROUP BY p.name " +
            "ORDER BY SUM(ol.quantity) DESC")
    List<Map<String, Double>> findTopProductsWithRevenue(@Param("merchantId") Long merchantId,
                                                         @Param("startDate") LocalDateTime startDate,
                                                         @Param("endDate") LocalDateTime endDate);



    @Query("SELECT COUNT(DISTINCT o.user.userId) " +
            "FROM Order o " +
            "JOIN o.orderLines ol " +
            "JOIN ol.product p " +
            "JOIN p.categoryProduct cp " +
            "WHERE cp.merchant.userId = :merchantId AND o.creationDate BETWEEN :startDate AND :endDate")
    Integer nombreClientsToMerchantId(@Param("merchantId") Long merchantId,
                                      @Param("startDate") LocalDateTime startDate,
                                      @Param("endDate") LocalDateTime endDate);


    // Classement des produits les plus consultés
    @Query("SELECT p.name " +
            "FROM OrderLine ol " +
            "JOIN ol.product p " +
            "JOIN p.categoryProduct cp " +
            "WHERE cp.merchant.userId = :merchantId AND ol.order.creationDate BETWEEN :startDate AND :endDate " +
            "GROUP BY p.name " +
            "ORDER BY COUNT(ol) DESC")
    List<String> findMostViewedProducts(@Param("merchantId") Long merchantId,
                                        @Param("startDate") LocalDateTime startDate,
                                        @Param("endDate") LocalDateTime endDate);

    // Classement des produits les plus achetés
    @Query("SELECT p.name " +
            "FROM OrderLine ol " +
            "JOIN ol.product p " +
            "JOIN p.categoryProduct cp " +
            "WHERE cp.merchant.userId = :merchantId AND ol.order.creationDate BETWEEN :startDate AND :endDate " +
            "GROUP BY p.name " +
            "ORDER BY SUM(ol.quantity) DESC")
    List<String> findMostPurchasedProducts(@Param("merchantId") Long merchantId,
                                           @Param("startDate") LocalDateTime startDate,
                                           @Param("endDate") LocalDateTime endDate);

}



