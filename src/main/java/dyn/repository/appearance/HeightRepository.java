package dyn.repository.appearance;

import dyn.model.appearance.AppearanceType;
import dyn.model.appearance.Height;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HeightRepository extends CrudRepository<Height, Long> {
    public Height findByName(String name);

    @Query(value = "SELECT * FROM app_height ORDER BY RAND() LIMIT 1", nativeQuery = true)
    public Height getRandom();

    @Query(value = "SELECT * FROM app_height WHERE type='usual' ORDER BY RAND() LIMIT 1", nativeQuery = true)
    public Height getRandomUsual();

    @Query(value = "SELECT * FROM app_height WHERE type='rare' ORDER BY RAND() LIMIT 1", nativeQuery = true)
    public Height getRandomRare();

    List<Height> findByType(AppearanceType type);
}