package dyn.repository.appearance;

import dyn.model.appearance.AppearanceType;
import dyn.model.appearance.Eyebrows;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EyebrowsRepository extends CrudRepository<Eyebrows, Long> {
    public Eyebrows findByName(String name);

    @Query(value = "SELECT * FROM app_eyebrows ORDER BY RAND() LIMIT 1", nativeQuery = true)
    public Eyebrows getRandom();

    @Query(value = "SELECT * FROM app_eyebrows WHERE type='usual' ORDER BY RAND() LIMIT 1", nativeQuery = true)
    public Eyebrows getRandomUsual();

    @Query(value = "SELECT * FROM app_eyebrows WHERE type='rare' ORDER BY RAND() LIMIT 1", nativeQuery = true)
    public Eyebrows getRandomRare();

    List<Eyebrows> findByType(AppearanceType type);
}
