package dyn.repository.adventure;

import dyn.model.Family;
import dyn.model.adventure.FamilyAdventure;
import dyn.model.adventure.FamilyAdventureStatus;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by OM on 12.01.2018.
 */
@Repository
public interface FamilyAdventureRepository extends CrudRepository<FamilyAdventure, Long> {

    List<FamilyAdventure> findAllByFamily(Family family);

    List<FamilyAdventure> findAllByFamilyOrderByNumAsc(Family family);

    FamilyAdventure findByFamilyAndCurrentIsTrue(Family family);

    FamilyAdventure findByFamilyAndNum(Family family, int num);

    int countAllByStatus(FamilyAdventureStatus familyAdventureStatus);

}
