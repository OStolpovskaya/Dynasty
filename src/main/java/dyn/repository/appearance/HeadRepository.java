package dyn.repository.appearance;

import dyn.model.appearance.AppearanceType;
import dyn.model.appearance.Head;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HeadRepository extends CrudRepository<Head, Long> {
    public Head findByName(String name);

    @Query(value = "SELECT * FROM app_head ORDER BY RAND() LIMIT 1", nativeQuery = true)
    public Head getRandom();

    @Query(value = "SELECT * FROM app_head WHERE type='usual' ORDER BY RAND() LIMIT 1", nativeQuery = true)
    public Head getRandomUsual();

    @Query(value = "SELECT * FROM app_head WHERE type='rare' ORDER BY RAND() LIMIT 1", nativeQuery = true)
    public Head getRandomRare();

    List<Head> findByType(AppearanceType type);

    List<Head> findAllByOrderByTypeAsc();
}