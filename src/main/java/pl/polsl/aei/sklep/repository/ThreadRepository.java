package pl.polsl.aei.sklep.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import pl.polsl.aei.sklep.repository.entity.Thread;

@Repository
public interface ThreadRepository extends CrudRepository<Thread, Long> {
    Thread getThreadByName(String name);
}
