package dyn.repository.career;

import dyn.model.career.Education;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EducationRepository extends CrudRepository<Education, Long> {
    public Education findByName(String name);

    List<Education> findAllBy();
}
