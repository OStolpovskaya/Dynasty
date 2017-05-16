package dyn.repository;

import dyn.model.Family;
import dyn.model.FamilyBuilding;
import dyn.model.House;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FamilyBuildingRepository extends CrudRepository<FamilyBuilding, Long> {
    List<FamilyBuilding> findByFamily(Family family);

    FamilyBuilding findByFamilyAndBuilding(Family family, House project);

}