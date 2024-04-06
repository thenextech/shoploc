package nextech.shoploc.repositories;

import nextech.shoploc.domains.Order;
import nextech.shoploc.domains.OrderLine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderLineRepository extends JpaRepository<OrderLine, Long> {
    List<OrderLine> findAllByOrder(Order order);
    List<OrderLine> findAllByMerchantId(Long MerchantId);

}
