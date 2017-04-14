package dyn.service;

import dyn.model.*;
import dyn.model.Character;
import dyn.repository.AchievementRepository;
import dyn.repository.FamilyRepository;
import dyn.repository.UserRepository;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by OM on 23.03.2017.
 */
@Service
public class AchievementService {
    private static final Logger logger = LogManager.getLogger(AchievementService.class);

    @Autowired
    AchievementRepository achievementRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    FamilyRepository familyRepository;


    public Achievement checkAchievement(AchievementType achievementType, User user, Character character) {
        Achievement achievement = null;
        switch (achievementType) {
            case newborn:
                achievement = achievementRepository.findByTypeAndForWhat(AchievementType.newborn, character.getRace().getId());
                break;
            case vocation10level:
                if (character.getCareer().getProfession().getLevel() == 10) {
                    achievement = achievementRepository.findByTypeAndForWhat(AchievementType.vocation10level, character.getCareer().getVocation().getId());
                }
                break;
        }
        if (achievement != null && !user.getAchievements().contains(achievement)) {
            user.getAchievements().add(achievement);
            userRepository.save(user);

            Family family = character.getFamily();
            family.setCraftPoint(family.getCraftPoint() + 2);
            familyRepository.save(family);

            logger.info(user.getUserName() + " is awarded! Achievement: " + achievement.getName() + ". Added 2 craft points to family " + family.getFamilyName());
            return achievement;
        }
        return null;
    }
}
