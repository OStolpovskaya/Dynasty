package dyn.repository.buildings;

import dyn.model.buildings.Building;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BuildingRepository extends CrudRepository<Building, Long> {
    List<Building> findAll();
}
