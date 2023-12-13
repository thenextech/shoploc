package nextech.shoploc.repositories;

import nextech.shoploc.domains.Order;
import nextech.shoploc.domains.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findAllByUser(User user);
    @Query("SELECT o FROM Order o JOIN o.orderLines ol JOIN ol.product p JOIN p.categoryProduct cp JOIN cp.merchant m WHERE m.userId = :merchantId")
    List<Order> findOrdersByMerchantId(@Param("merchantId") Long merchantId);}
