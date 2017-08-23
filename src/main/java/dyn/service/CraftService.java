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
    private final FamilyProjectRepository familyProjectRepository;

    @Autowired
    public CraftService(ThingRepository thingRepository, ProjectRepository projectRepository, ItemRepository itemRepository, CraftBranchRepository craftBranchRepository, FamilyRepository familyRepository, FamilyProjectRepository familyProjectRepository) {
        this.thingRepository = thingRepository;
        this.projectRepository = projectRepository;
        this.itemRepository = itemRepository;
        this.familyRepository = familyRepository;
        this.craftBranchRepository = craftBranchRepository;
        this.familyProjectRepository = familyProjectRepository;
    }

    public void newFamily(Family family) {
        family.setCraftPoint(Const.CRAFT_POINTS_START);

        // дарим новой семье плиту Скоровар
        giveGift(family, Const.PROJECT_SKOROVAR);

        // дарим айтемы баффов: плодовитость 5, ген.мод. 3, доминант отца 1 и доминант мтери 1
        for (int i = 0; i < 5; i++) {
            giveGift(family, Const.PROJECT_FERTILITY);
        }
        for (int i = 0; i < 3; i++) {
            giveGift(family, Const.PROJECT_GEN_MOD);
        }
        giveGift(family, Const.PROJECT_FATHER_DOMINANT);
        giveGift(family, Const.PROJECT_MOTHER_DOMINANT);

    }

    private void giveGift(Family family, Long projectId) {
        Project project = projectRepository.findOne(projectId);

        Item item = new Item();
        item.setProject(project);
        item.setFamily(family);
        item.setAuthor(familyRepository.findOne(1L));
        item.setPlace(ItemPlace.storage);
        item.setInteriorId(0L);
        item.setCost(0);
        itemRepository.save(item);
    }

    public Thing getThing(Long thingId) {
        return thingRepository.findOne(thingId);
    }

    public Project getProject(Long projectId) {
        return projectRepository.findOne(projectId);
    }

    public Item createItem(Project project, Family family, boolean applyBuffItemQuality) {
        FamilyProject familyProject = familyProjectRepository.findByFamilyAndProject(family, project);

        Item item = new Item();
        item.setProject(project);
        item.setFamily(family);
        item.setAuthor(family);
        item.setPlace(ItemPlace.storage);
        item.setInteriorId(0L);
        item.setCost(0);

        int quality = 0;
        if (project.getAuthor().getId() != 1L) { // project is developed by player
            quality += 1;
        }
        if (familyProject.getCount() >= Const.ITEM_QUALITY_1) { // number of items, made of this project
            quality += 1;
        }
        if (familyProject.getCount() >= Const.ITEM_QUALITY_2) { // number of items, made of this project
            quality += 1;
        }
        if (familyProject.getCount() >= Const.ITEM_QUALITY_3) { // number of items, made of this project
            quality += 1;
        }
        if (applyBuffItemQuality) { // buff
            quality += 1;
        }

        item.setQuality(quality);
        itemRepository.save(item);

        familyProject.incCount();
        familyProjectRepository.save(familyProject);

        return item;
    }

    public Item createProductionItem(Project project, Family family) {
        FamilyProject familyProject = familyProjectRepository.findByFamilyAndProject(family, project);

        Item item = new Item();
        item.setProject(project);
        item.setFamily(family);
        item.setAuthor(family);
        item.setPlace(ItemPlace.storage);
        item.setInteriorId(0L);
        item.setCost(0);
        item.setQuality(0);
        itemRepository.save(item);

        familyProject.incCount();
        familyProjectRepository.save(familyProject);

        return item;
    }

    public List<Item> createItemForStore(Project project, Family family, int cost) {
        List<Item> items = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            Item item = new Item();
            item.setProject(project);
            item.setFamily(family);
            item.setAuthor(family);
            item.setPlace(ItemPlace.store);
            item.setInteriorId(0L);
            item.setCost(cost);
            itemRepository.save(item);

            items.add(item);
        }
        return items;
    }

    public List<Thing> getThingsForTree() {
        List<Thing> things = new ArrayList<>();
        things.add(thingRepository.getParentThingForCraftBranchId(1));
        things.add(thingRepository.getParentThingForCraftBranchId(2));
        things.add(thingRepository.getParentThingForCraftBranchId(3));
        things.add(thingRepository.getParentThingForCraftBranchId(4));
        things.add(thingRepository.getParentThingForCraftBranchId(5));
        things.add(thingRepository.getParentThingForCraftBranchId(6));
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
        craftBranchList.add(craftBranchRepository.findOne(6L));
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

    public void saveProject(Project project) {
        projectRepository.save(project);
    }

    public List<Project> getApprovedProjectsByThing(Thing thing) {
        return projectRepository.findByThingAndStatus(thing, ProjectStatus.approved);
    }

    public List<Project> getAuthorProjects(Family family) {
        return projectRepository.findByAuthor(family);
    }

    public List<Project> getProjectsByStatus(ProjectStatus status) {
        return projectRepository.findAllByStatusOrderByThing(status);
    }

    public List<FamilyProject> getFamilyProjectsForThing(Family family, Thing thing) {
        return familyProjectRepository.findByFamilyAndProjectThing(family, thing);
    }

    public boolean isItemBelongsToBuffAndServices(Item item) {
        if (item.getProject().getThing().getCraftBranch().getId() == Const.CRAFTBRANCH_SERVICE_AND_BUFFS) {
            return true;
        }
        return false;
    }
}
