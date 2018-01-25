package dyn.repository.adventure;

import dyn.model.Family;
import dyn.model.adventure.Adventure;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by OM on 12.01.2018.
 */
@Repository
public interface AdventureRepository extends CrudRepository<Adventure, Long> {
    List<Adventure> findAllByOrderByCreationDateDesc();

    List<Adventure> findAllByCreatedByOrderByCreationDateDesc(Family createdBy);

    @Query(value = "select * from adventure where adventure.landscape_id=?1 ORDER BY RAND() LIMIT ?2", nativeQuery = true)
    List<Adventure> findRandomAdventuresOfLandscape(long landscape, int amountPerTurn);

    @Query(value = "select * from adventure ORDER BY RAND() LIMIT ?1", nativeQuery = true)
    List<Adventure> findRandomAdventures(int adventureAmountPerTurn);
}
