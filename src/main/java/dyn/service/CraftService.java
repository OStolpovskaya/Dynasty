package dyn.service;

import dyn.model.*;
import dyn.repository.ItemRepository;
import dyn.repository.ProjectRepository;
import dyn.repository.ThingRepository;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by OM on 30.03.2017.
 */
@Service
public class CraftService {

    private static final Logger logger = LogManager.getLogger(CraftService.class);
    private final ThingRepository thingRepository;
    private final ProjectRepository projectRepository;
    private final ItemRepository itemRepository;

    @Autowired
    public CraftService(ThingRepository thingRepository, ProjectRepository projectRepository, ItemRepository itemRepository) {
        this.thingRepository = thingRepository;
        this.projectRepository = projectRepository;
        this.itemRepository = itemRepository;
    }

    public void newFamily(Family family) {
        family.setCraftPoint(3);
        family.getCraftThings().add(thingRepository.findByCraftBranchIdAndCraftNumber(1, 1)); // кухонный стул
        Project firstChairProject = projectRepository.findOne(1L);
        family.getCraftProjects().add(firstChairProject); // простая табуретка
        createItem(firstChairProject, family);
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
}
