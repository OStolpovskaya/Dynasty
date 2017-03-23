package dyn.repository.career;

import dyn.model.career.Education;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EducationRepository extends CrudRepository<Education, Long> {
    public Education findByName(String name);
}
