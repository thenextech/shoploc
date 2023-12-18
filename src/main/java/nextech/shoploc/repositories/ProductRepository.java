package nextech.shoploc.repositories;

import nextech.shoploc.domains.CategoryProduct;
import nextech.shoploc.domains.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    List<Product> findAllByCategoryProduct(CategoryProduct categoryProduct);
}
