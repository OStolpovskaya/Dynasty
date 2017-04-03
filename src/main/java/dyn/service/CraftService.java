package dyn.service;

import dyn.model.Family;
import dyn.model.Project;
import dyn.model.Thing;
import dyn.repository.CraftBranchRepository;
import dyn.repository.FamilyRepository;
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
    @Autowired
    private ThingRepository thingRepository;
    @Autowired
    private ProjectRepository projectRepository;
    @Autowired
    private CraftBranchRepository craftBranchRepository;
    @Autowired
    private FamilyRepository familyRepository;

    public void newFamily(Family family) {
        family.setCraftPoint(3);
        family.getCraftThings().add(thingRepository.findByCraftBranchIdAndCraftNumber(1L, 1)); // кухонный стул
        family.getCraftProjects().add(projectRepository.findOne(1L)); // простая табуретка
    }

    public Thing getThing(Long thingId) {
        return thingRepository.findOne(thingId);
    }

    public Project getProject(Long projectId) {
        return projectRepository.findOne(projectId);
    }
}
