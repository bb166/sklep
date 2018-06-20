package pl.polsl.aei.sklep.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import pl.polsl.aei.sklep.repository.entity.Category;

@Repository
public interface CategoryRepository extends CrudRepository<Category, Long> {
    Category findCategoryByName(String name);
}
