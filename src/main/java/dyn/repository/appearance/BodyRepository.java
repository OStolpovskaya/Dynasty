package dyn.repository.appearance;

import dyn.model.appearance.AppearanceType;
import dyn.model.appearance.Body;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BodyRepository extends CrudRepository<Body, Long> {
    public Body findByName(String name);

    @Query(value = "SELECT * FROM app_body ORDER BY RAND() LIMIT 1", nativeQuery = true)
    public Body getRandom();

    @Query(value = "SELECT * FROM app_body WHERE type='usual' ORDER BY RAND() LIMIT 1", nativeQuery = true)
    public Body getRandomUsual();

    @Query(value = "SELECT * FROM app_body WHERE type='rare' ORDER BY RAND() LIMIT 1", nativeQuery = true)
    public Body getRandomRare();

    List<Body> findByType(AppearanceType type);

    List<Body> findAllByOrderByTypeAsc();
}
