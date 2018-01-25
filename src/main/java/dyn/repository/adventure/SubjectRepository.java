package dyn.repository.adventure;

import dyn.model.adventure.Subject;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by OM on 12.01.2018.
 */
@Repository
public interface SubjectRepository extends CrudRepository<Subject, Long> {
    List<Subject> findAllByOrderByNameAsc();
}
