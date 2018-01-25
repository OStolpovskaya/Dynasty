package dyn.repository.adventure;

import dyn.model.adventure.Landscape;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by OM on 12.01.2018.
 */
@Repository
public interface LandscapeRepository extends CrudRepository<Landscape, Long> {
    List<Landscape> findAllByOrderByNameAsc();

    @Query(value = "SELECT * FROM adventure_landscape ORDER BY RAND() LIMIT 1", nativeQuery = true)
    Landscape findRandomLandscape();
}
