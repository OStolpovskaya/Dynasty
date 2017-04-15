package dyn.repository;

import dyn.model.Thing;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ThingRepository extends CrudRepository<Thing, Long> {
    Thing findByName(String thingName);

    Thing findByCraftBranchIdAndCraftNumber(int craftBranchId, int craftNumber);

    @Query(value = "SELECT * FROM thing where craft_branch_id=1 AND parent is null LIMIT 1", nativeQuery = true)
    Thing getParentThing();
}