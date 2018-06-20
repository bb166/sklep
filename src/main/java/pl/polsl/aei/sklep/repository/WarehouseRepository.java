package pl.polsl.aei.sklep.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import pl.polsl.aei.sklep.repository.entity.Warehouse;

@Repository
public interface WarehouseRepository extends CrudRepository<Warehouse, Long> {

}
