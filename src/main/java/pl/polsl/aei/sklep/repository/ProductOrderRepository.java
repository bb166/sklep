package pl.polsl.aei.sklep.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import pl.polsl.aei.sklep.repository.entity.ProductOrder;

@Repository
public interface ProductOrderRepository extends CrudRepository<ProductOrder, Long> {
}
