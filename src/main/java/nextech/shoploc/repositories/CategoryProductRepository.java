package nextech.shoploc.repositories;

import nextech.shoploc.domains.CategoryProduct;
import nextech.shoploc.domains.Merchant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryProductRepository extends JpaRepository<CategoryProduct, Long> {
    List<CategoryProduct> findAllByMerchant(Merchant merchant);
}
