package dyn.repository.appearance;

import dyn.model.appearance.AppearanceType;
import dyn.model.appearance.Ears;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EarsRepository extends CrudRepository<Ears, Long> {
    public Ears findByName(String name);

    @Query(value = "SELECT * FROM app_ears ORDER BY RAND() LIMIT 1", nativeQuery = true)
    public Ears getRandom();

    @Query(value = "SELECT * FROM app_ears WHERE type='usual' ORDER BY RAND() LIMIT 1", nativeQuery = true)
    public Ears getRandomUsual();

    @Query(value = "SELECT * FROM app_ears WHERE type='rare' ORDER BY RAND() LIMIT 1", nativeQuery = true)
    public Ears getRandomRare();

    List<Ears> findByType(AppearanceType type);

    List<Ears> findAllByOrderByTypeAsc();
}
