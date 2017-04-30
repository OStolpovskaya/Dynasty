package dyn.service;

import dyn.model.*;
import dyn.repository.*;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by OM on 30.03.2017.
 */
@Service
public class CraftService {

    private static final Logger logger = LogManager.getLogger(CraftService.class);
    final
    CraftBranchRepository craftBranchRepository;
    private final ThingRepository thingRepository;
    private final ProjectRepository projectRepository;
    private final ItemRepository itemRepository;
    private final FamilyRepository familyRepository;

    @Autowired
    public CraftService(ThingRepository thingRepository, ProjectRepository projectRepository, ItemRepository itemRepository, CraftBranchRepository craftBranchRepository, FamilyRepository familyRepository) {
        this.thingRepository = thingRepository;
        this.projectRepository = projectRepository;
        this.itemRepository = itemRepository;
        this.familyRepository = familyRepository;
        this.craftBranchRepository = craftBranchRepository;
    }

    public void newFamily(Family family) {
        family.setCraftPoint(3);
        Item item = createItem(projectRepository.findOne(4L), familyRepository.findOne(1L)); // дарим новой семье плиту Скоровар
        item.setFamily(family);
        itemRepository.save(item);
    }

    public Thing getThing(Long thingId) {
        return thingRepository.findOne(thingId);
    }

    public Project getProject(Long projectId) {
        return projectRepository.findOne(projectId);
    }

    public Item createItem(Project project, Family family) {
        Item item = new Item();
        item.setProject(project);
        item.setFamily(family);
        item.setAuthor(family);
        item.setPlace(ItemPlace.storage);
        item.setInteriorId(0L);
        item.setCost(0);
        itemRepository.save(item);
        return item;
    }

    public List<Thing> getThingsForTree() {
        List<Thing> things = new ArrayList<>();
        things.add(thingRepository.getParentThingForCraftBranchId(1));
        things.add(thingRepository.getParentThingForCraftBranchId(2));
        things.add(thingRepository.getParentThingForCraftBranchId(3));
        things.add(thingRepository.getParentThingForCraftBranchId(4));
        things.add(thingRepository.getParentThingForCraftBranchId(5));
        return things;
    }

    public List<Thing> getAllThings() {
        return (List<Thing>) thingRepository.findAll();
    }

    public void changeThing(Long thingId, String thingName, Long thingParentId, int thingCost, int thingWidth, int thingHeight) {
        Thing thing = thingRepository.findOne(thingId);
        Thing parentThing = thingRepository.findOne(thingParentId);

        thing.setName(thingName);
        thing.setParent(parentThing);
        thing.setCost(thingCost);
        thing.setWidth(thingWidth);
        thing.setHeight(thingHeight);
        thingRepository.save(thing);
    }


    public List<CraftBranch> getCraftBranches() {
        List<CraftBranch> craftBranchList = new ArrayList<>();
        craftBranchList.add(craftBranchRepository.findOne(1L));
        craftBranchList.add(craftBranchRepository.findOne(2L));
        craftBranchList.add(craftBranchRepository.findOne(3L));
        craftBranchList.add(craftBranchRepository.findOne(4L));
        craftBranchList.add(craftBranchRepository.findOne(5L));
        return craftBranchList;
    }

    public void newThing(String name, Long craftBranchId, Long parentId, int cost, int width, int height) {
        Thing thing = new Thing();
        CraftBranch craftBranch = craftBranchRepository.findOne(craftBranchId);
        Thing parentThing = thingRepository.findOne(parentId);

        thing.setName(name);
        thing.setCraftBranch(craftBranch);
        thing.setParent(parentThing);
        thing.setCost(cost);
        thing.setWidth(width);
        thing.setHeight(height);
        thingRepository.save(thing);
    }

    public List<Item> getItemsOfThing(Thing thing) {
        return itemRepository.findAllByProjectThingOrderByCost(thing);
    }
}
