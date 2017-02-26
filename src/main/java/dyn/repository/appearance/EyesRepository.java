package dyn.repository.appearance;

import dyn.model.appearance.Eyes;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EyesRepository extends CrudRepository<Eyes, Long> {
    public Eyes findByName(String name);

    @Query(value = "SELECT * FROM app_eyes ORDER BY RAND() LIMIT 1", nativeQuery = true)
    public Eyes getRandom();
}
