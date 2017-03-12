package dyn.repository.appearance;

import dyn.model.appearance.AppearanceType;
import dyn.model.appearance.SkinColor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SkinColorRepository extends CrudRepository<SkinColor, Long> {
    public SkinColor findByName(String name);

    @Query(value = "SELECT * FROM app_skin_color ORDER BY RAND() LIMIT 1", nativeQuery = true)
    public SkinColor getRandom();

    @Query(value = "SELECT * FROM app_skin_color WHERE type='usual' ORDER BY RAND() LIMIT 1", nativeQuery = true)
    public SkinColor getRandomUsual();

    @Query(value = "SELECT * FROM app_skin_color WHERE type='rare' ORDER BY RAND() LIMIT 1", nativeQuery = true)
    public SkinColor getRandomRare();

    List<SkinColor> findByType(AppearanceType type);
}