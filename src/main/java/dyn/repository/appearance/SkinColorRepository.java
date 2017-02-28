package dyn.repository.appearance;

import dyn.model.appearance.SkinColor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SkinColorRepository extends CrudRepository<SkinColor, Long> {
    public SkinColor findByName(String name);

    @Query(value = "SELECT * FROM app_skin_color ORDER BY RAND() LIMIT 1", nativeQuery = true)
    public SkinColor getRandom();

    @Query(value = "SELECT * FROM app_skin_color WHERE type='usual' ORDER BY RAND() LIMIT 1", nativeQuery = true)
    public SkinColor getRandomUsual();
}