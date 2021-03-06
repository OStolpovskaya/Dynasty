package dyn.repository;

import dyn.model.CraftBranch;
import dyn.model.Thing;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ThingRepository extends CrudRepository<Thing, Long> {
    Thing findByName(String thingName);

    @Query(value = "SELECT * FROM thing where craft_branch_id=:id AND parent is null LIMIT 1", nativeQuery = true)
    Thing getParentThingForCraftBranchId(@Param("id") int i);

    List<Thing> findByCraftBranch(CraftBranch craftBranch);

    List<Thing> findByCraftBranchId(Long craftBranchId);

    List<Thing> findByCraftBranchIdLessThanEqualOrderByName(Long craftBranchId);

    List<Thing> findAllByOrderByName();

    @Query(value = "SELECT sum(cost) as summa FROM thing where craft_branch_id=:id", nativeQuery = true)
    int sumCostOfThingsOfCraftBranchId(@Param("id") Long craftBranchId);
}