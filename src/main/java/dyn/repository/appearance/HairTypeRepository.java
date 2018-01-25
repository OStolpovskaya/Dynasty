package dyn.repository.appearance;

import dyn.model.appearance.AppearanceType;
import dyn.model.appearance.HairType;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HairTypeRepository extends CrudRepository<HairType, Long> {
    public HairType findByName(String name);

    @Query(value = "SELECT * FROM app_hair_type ORDER BY RAND() LIMIT 1", nativeQuery = true)
    public HairType getRandom();

    @Query(value = "SELECT * FROM app_hair_type WHERE type='usual' ORDER BY RAND() LIMIT 1", nativeQuery = true)
    public HairType getRandomUsual();

    @Query(value = "SELECT * FROM app_hair_type WHERE type='rare' ORDER BY RAND() LIMIT 1", nativeQuery = true)
    public HairType getRandomRare();

    List<HairType> findByType(AppearanceType type);

    List<HairType> findAllByOrderByTypeAsc();
}
