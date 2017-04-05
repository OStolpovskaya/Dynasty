package dyn.service;

import dyn.model.*;
import dyn.repository.*;
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
    @Autowired
    private ThingRepository thingRepository;
    @Autowired
    private ProjectRepository projectRepository;
    @Autowired
    private CraftBranchRepository craftBranchRepository;
    @Autowired
    private FamilyRepository familyRepository;
    @Autowired
    private ItemRepository itemRepository;

    public void newFamily(Family family) {
        family.setCraftPoint(3);
        family.getCraftThings().add(thingRepository.findByCraftBranchIdAndCraftNumber(1, 1)); // кухонный стул
        family.getCraftProjects().add(projectRepository.findOne(1L)); // простая табуретка
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
