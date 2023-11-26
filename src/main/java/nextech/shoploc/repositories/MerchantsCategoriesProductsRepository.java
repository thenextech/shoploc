package nextech.shoploc.repositories;

import nextech.shoploc.domains.MerchantsCategoriesProducts;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MerchantsCategoriesProductsRepository extends JpaRepository<MerchantsCategoriesProducts, Long> {

}
