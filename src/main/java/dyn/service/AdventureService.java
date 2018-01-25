package dyn.service;

import dyn.model.Family;
import dyn.model.Item;
import dyn.model.adventure.*;
import dyn.repository.adventure.AdventureRepository;
import dyn.repository.adventure.FamilyAdventureRepository;
import dyn.repository.adventure.LandscapeRepository;
import dyn.repository.adventure.SubjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by OM on 12.01.2018.
 */
@Service
public class AdventureService {
    @Autowired
    MessageSource messageSource;
    @Autowired
    FamilyAdventureRepository familyAdventureRepository;
    @Autowired
    AdventureRepository adventureRepository;
    @Autowired
    LandscapeRepository landscapeRepository;
    @Autowired
    SubjectRepository subjectRepository;
    @Autowired
    CraftService craftService;

    public List<FamilyAdventure> getFamilyAdventures(Family family) {

        return familyAdventureRepository.findAllByFamilyOrderByNumAsc(family);
    }


    public FamilyAdventure getCurrentAdventure(Family family) {
        return familyAdventureRepository.findByFamilyAndCurrentIsTrue(family);
    }

    public void answer(Adventure adventure, Character character, List<Item> items) {

    }

    public List<Landscape> getLandscapes() {
        return landscapeRepository.findAllByOrderByNameAsc();
    }

    public List<Subject> getSubjects() {
        return subjectRepository.findAllByOrderByNameAsc();
    }

    public void saveAdventure(Adventure newAdventure) {
        adventureRepository.save(newAdventure);
    }

    public List<Adventure> getAllAdventures() {
        return adventureRepository.findAllByOrderByCreationDateDesc();
    }

    public Adventure getAdventure(Long id) {
        return adventureRepository.findOne(id);
    }

    public void createFamilyAdventures(Family family) {
        /* // todo: раскомментировать, когда приключений будет много
        Landscape landscape = landscapeRepository.findRandomLandscape();
        List<Adventure> adventureList = adventureRepository.findRandomAdventuresOfLandscape(landscape.getId(), Const.ADVENTURE_AMOUNT_PER_TURN);
        */
        List<Adventure> adventureList = adventureRepository.findRandomAdventures(Const.ADVENTURE_AMOUNT_PER_TURN);
        int c = 1;
        for (Adventure adventure : adventureList) {
            FamilyAdventure familyAdventure = new FamilyAdventure();
            familyAdventure.setFamily(family);
            familyAdventure.setAdventure(adventure);
            familyAdventure.setStatus(FamilyAdventureStatus.inprogress);
            familyAdventure.setNum(c);
            if (c == 1) {
                familyAdventure.setCurrent(true);
            } else {
                familyAdventure.setCurrent(false);
            }
            familyAdventureRepository.save(familyAdventure);

            adventure.incProposedTimes();
            adventureRepository.save(adventure);

            c++;
        }
    }

    public void setCurrentFamilyAdventure(Family family, int num) {
        FamilyAdventure currentFamilyAdventure = familyAdventureRepository.findByFamilyAndCurrentIsTrue(family);
        FamilyAdventure familyAdventure = familyAdventureRepository.findByFamilyAndNum(family, num);

        currentFamilyAdventure.setCurrent(false);
        familyAdventure.setCurrent(true);

        familyAdventureRepository.save(currentFamilyAdventure);
        familyAdventureRepository.save(familyAdventure);
    }

    public void saveFamilyAdventure(FamilyAdventure familyAdventure) {
        familyAdventureRepository.save(familyAdventure);
    }

    public boolean checkIfAllAdventuresCompleted(Family family) {

        boolean b = familyAdventureRepository.countAllByStatus(FamilyAdventureStatus.finished) == Const.ADVENTURE_AMOUNT_PER_TURN;
        System.out.println("completed " + b);
        return b;
    }

    public void deleteFamilyAdventures(Family family) {
        List<FamilyAdventure> familyAdventures = familyAdventureRepository.findAllByFamily(family);
        familyAdventureRepository.delete(familyAdventures);
    }

    public List<Adventure> getAdventureCreatedBy(Family family) {
        return adventureRepository.findAllByCreatedByOrderByCreationDateDesc(family);
    }
}
