package pl.polsl.aei.sklep.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import pl.polsl.aei.sklep.repository.entity.Product;

import java.util.List;

@Repository
public interface ProductRepository extends CrudRepository<Product, Long> {
    List<Product> findProductsByNameContaining(String name);
    Product findProductByName(String name);
}
