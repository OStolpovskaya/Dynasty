package dyn.repository.appearance;

import dyn.model.appearance.AppearanceType;
import dyn.model.appearance.Nose;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NoseRepository extends CrudRepository<Nose, Long> {
    public Nose findByName(String name);

    @Query(value = "SELECT * FROM app_nose ORDER BY RAND() LIMIT 1", nativeQuery = true)
    public Nose getRandom();

    @Query(value = "SELECT * FROM app_nose WHERE type='usual' ORDER BY RAND() LIMIT 1", nativeQuery = true)
    public Nose getRandomUsual();

    @Query(value = "SELECT * FROM app_nose WHERE type='rare' ORDER BY RAND() LIMIT 1", nativeQuery = true)
    public Nose getRandomRare();

    List<Nose> findByType(AppearanceType type);

    List<Nose> findAllByOrderByTypeAsc();
}
