package dyn.service;

import dyn.model.*;
import dyn.model.Character;
import dyn.repository.AchievementRepository;
import dyn.repository.FamilyRepository;
import dyn.repository.UserAchievementsRepository;
import dyn.repository.UserRepository;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by OM on 23.03.2017.
 */
@Service
public class AchievementService {
    private static final Logger logger = LogManager.getLogger(AchievementService.class);

    @Autowired
    AchievementRepository achievementRepository;

    @Autowired
    UserAchievementsRepository userAchievementsRepository;

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

            Family family = user.getCurrentFamily();
            family.setCraftPoint(family.getCraftPoint() + Const.ACHIEVEMENT_CRAFT_POINTS);
            family.addMoney(Const.ACHIEVEMENT_MONEY);
            familyRepository.save(family);

            logger.info(user.getUserName() + " is awarded! Achievement: " + achievement.getName() + ". Added " + Const.ACHIEVEMENT_CRAFT_POINTS + " craft points and " + Const.ACHIEVEMENT_MONEY + " to family " + family.getFamilyName());
            return achievement;
        }
        return null;
    }

    public List<UserAchievements> getAchievementsOfUser(User user) {
        return userAchievementsRepository.findByUser(user);

    }

    public Map<User, Integer> getAcievementRatingMap() {
        Map<User, Integer> map = new LinkedHashMap<>();
        List<Object[]> list = userAchievementsRepository.countAchievements();
        for (Object[] objects : list) {
            Integer userId = (Integer) objects[0];
            BigInteger count = (BigInteger) objects[1];
            map.put(userRepository.findOne(userId.longValue()), count.intValue());

        }
        return map;
    }
}
