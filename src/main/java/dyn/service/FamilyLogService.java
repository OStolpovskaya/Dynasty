package dyn.service;

import dyn.model.Character;
import dyn.model.Family;
import dyn.model.FamilyLog;
import dyn.repository.FamilyLogRepository;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by OM on 15.03.2017.
 */
@Service
public class FamilyLogService {

    private static final Logger logger = LogManager.getLogger(FamilyLogService.class);

    @Autowired
    FamilyLogRepository familyLogRepository;

    public FamilyLog createNewFamilyLog(Family family, Character founder, Character foundress) {

        FamilyLog familyLog = new FamilyLog(family);
        familyLog.addText("История вашей династии началась! Основатели: " + family.getFamilyName() + " " + founder.getName() + " и " + foundress.getName());
        familyLogRepository.save(familyLog);

        return familyLog;
    }

    public FamilyLog createNewLevelFamilyLog(Family family, String mess) {
        FamilyLog familyLog = new FamilyLog(family);
        familyLog.addText("Новый уровень: " + family.getLevel() + "!<br>" + mess);
        familyLogRepository.save(familyLog);
        return familyLog;
    }

    public FamilyLog getLevelFamilyLog(Family family) {
        FamilyLog familyLog = familyLogRepository.findByFamilyAndLevel(family, family.getLevel());
        return familyLog;
    }
}