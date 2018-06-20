package pl.polsl.aei.sklep.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import pl.polsl.aei.sklep.repository.entity.Size;

@Repository
public interface SizeRepository extends CrudRepository<Size, Long> {
    Size findSizeByName(String name);
}
