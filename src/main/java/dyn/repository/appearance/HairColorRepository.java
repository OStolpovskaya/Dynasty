package dyn.repository.appearance;

import dyn.model.appearance.AppearanceType;
import dyn.model.appearance.HairColor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HairColorRepository extends CrudRepository<HairColor, Long> {
    public HairColor findByName(String name);

    @Query(value = "SELECT * FROM app_hair_color ORDER BY RAND() LIMIT 1", nativeQuery = true)
    public HairColor getRandom();

    @Query(value = "SELECT * FROM app_hair_color WHERE type='usual' ORDER BY RAND() LIMIT 1", nativeQuery = true)
    public HairColor getRandomUsual();

    @Query(value = "SELECT * FROM app_hair_color WHERE type='rare' ORDER BY RAND() LIMIT 1", nativeQuery = true)
    public HairColor getRandomRare();

    List<HairColor> findByType(AppearanceType type);
}
