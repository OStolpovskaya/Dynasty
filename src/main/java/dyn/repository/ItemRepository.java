package dyn.repository;

import dyn.model.*;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ItemRepository extends CrudRepository<Item, Long> {
    List<Item> findByFamilyAndProjectThing(Family family, Thing thing);

    List<Item> findByFamily(Family family);

    Item findByFamilyAndId(Family family, Long itemId);

    Item findByFamilyAndInteriorId(Family family, Long roomInteriorId);

    List<Item> findByPlaceAndProjectThing(ItemPlace place, Thing thing);

    List<Item> findByFamilyAndPlaceOrderByProjectThing(Family family, ItemPlace place);

    List<Item> findByFamilyAndPlaceAndProjectThingCraftBranchIdLessThanEqualOrderByProjectThingAscProjectAscQualityAsc(Family family, ItemPlace place, Long maxCraftBranchIdForUsualThings);

    List<Item> findByFamilyAndPlaceAndProjectThingCraftBranchIdOrderByProjectThingAscProjectAsc(Family family, ItemPlace place, Long craftBranchIdForServicesAndBuffs);

    List<Item> findAllByProjectThingOrderByCost(Thing thing);

    List<Item> findByPlaceAndProject(ItemPlace store, Project project);
}