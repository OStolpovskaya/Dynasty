package dyn.repository.buildings;

import dyn.model.buildings.Building;
import dyn.model.buildings.BuildingThing;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BuildingThingRepository extends CrudRepository<BuildingThing, Long> {
    List<BuildingThing> findAll();

    List<BuildingThing> findAllByBuilding(Building building);
}
