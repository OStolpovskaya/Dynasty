package dyn.repository.appearance;

import dyn.model.appearance.AppearanceType;
import dyn.model.appearance.Mouth;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MouthRepository extends CrudRepository<Mouth, Long> {
    public Mouth findByName(String name);

    @Query(value = "SELECT * FROM app_mouth ORDER BY RAND() LIMIT 1", nativeQuery = true)
    public Mouth getRandom();

    @Query(value = "SELECT * FROM app_mouth WHERE type='usual' ORDER BY RAND() LIMIT 1", nativeQuery = true)
    public Mouth getRandomUsual();

    @Query(value = "SELECT * FROM app_mouth WHERE type='rare' ORDER BY RAND() LIMIT 1", nativeQuery = true)
    public Mouth getRandomRare();

    List<Mouth> findByType(AppearanceType type);
}
