package dyn.repository;

import dyn.model.Thing;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ThingRepository extends CrudRepository<Thing, Long> {
    Thing findByName(String thingName);

    Thing findByCraftBranchIdAndCraftNumber(Long craftBranchId, int craftNumber);
}