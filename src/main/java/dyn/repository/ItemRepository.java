package dyn.repository;

import dyn.model.Family;
import dyn.model.Item;
import dyn.model.Thing;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ItemRepository extends CrudRepository<Item, Long> {
    public List<Item> findByFamilyAndProjectThing(Family family, Thing thing);

    public List<Item> findByFamily(Family family);
}