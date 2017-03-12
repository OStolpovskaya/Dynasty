package dyn.repository.appearance;

import dyn.model.appearance.HairStyle;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HairStyleRepository extends CrudRepository<HairStyle, Long> {
    public HairStyle findByName(String name);

    @Query(value = "SELECT * FROM app_hair_style WHERE sex=?1 and hair_type=?2 ORDER BY RAND() LIMIT 1", nativeQuery = true)
    public HairStyle getRandom(String sex, long hairTypeId);

    @Query(value = "SELECT * FROM app_hair_style WHERE type='usual' ORDER BY RAND() LIMIT 1", nativeQuery = true)
    public HairStyle getRandomUsual();

    @Query(value = "SELECT * FROM app_hair_style WHERE type='rare' ORDER BY RAND() LIMIT 1", nativeQuery = true)
    public HairStyle getRandomRare();
}
