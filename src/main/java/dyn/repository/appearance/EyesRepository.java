package dyn.repository.appearance;

import dyn.model.appearance.AppearanceType;
import dyn.model.appearance.Eyes;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EyesRepository extends CrudRepository<Eyes, Long> {
    public Eyes findByName(String name);

    @Query(value = "SELECT * FROM app_eyes ORDER BY RAND() LIMIT 1", nativeQuery = true)
    public Eyes getRandom();

    @Query(value = "SELECT * FROM app_eyes WHERE type='usual' ORDER BY RAND() LIMIT 1", nativeQuery = true)
    public Eyes getRandomUsual();

    @Query(value = "SELECT * FROM app_eyes WHERE type='rare' ORDER BY RAND() LIMIT 1", nativeQuery = true)
    public Eyes getRandomRare();

    List<Eyes> findByType(AppearanceType type);
}
