package dyn.repository.appearance;

import dyn.model.appearance.AppearanceType;
import dyn.model.appearance.EyeColor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EyeColorRepository extends CrudRepository<EyeColor, Long> {
    public EyeColor findByName(String name);

    @Query(value = "SELECT * FROM app_eye_color ORDER BY RAND() LIMIT 1", nativeQuery = true)
    public EyeColor getRandom();

    @Query(value = "SELECT * FROM app_eye_color WHERE type='usual' ORDER BY RAND() LIMIT 1", nativeQuery = true)
    public EyeColor getRandomUsual();

    @Query(value = "SELECT * FROM app_eye_color WHERE type='rare' ORDER BY RAND() LIMIT 1", nativeQuery = true)
    public EyeColor getRandomRare();

    List<EyeColor> findByType(AppearanceType type);
}
