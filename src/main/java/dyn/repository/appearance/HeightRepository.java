package dyn.repository.appearance;

import dyn.model.appearance.Height;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HeightRepository extends CrudRepository<Height, Long> {
    public Height findByName(String name);

    @Query(value = "SELECT * FROM app_height ORDER BY RAND() LIMIT 1", nativeQuery = true)
    public Height getRandom();
}