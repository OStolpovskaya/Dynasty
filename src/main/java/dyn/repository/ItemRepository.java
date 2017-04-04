package dyn.repository;

import dyn.model.Family;
import dyn.model.Item;
import dyn.model.Thing;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ItemRepository extends CrudRepository<Item, Long> {
    List<Item> findByFamilyAndProjectThing(Family family, Thing thing);

    List<Item> findByFamily(Family family);

    Item findByFamilyAndId(Family family, Long itemId);

    Item findByFamilyAndInteriorId(Family family, Long roomInteriorId);

    @Modifying
    @Query(value = "update item u set u.interior_id = -1 where u.id = ?1", nativeQuery = true)
    void putItemIntoStore(Long itemId);
}