package dyn.controllers;

/**
 * Created by OM on 17.02.2017.
 */


import dyn.model.*;
import dyn.model.Character;
import dyn.repository.AchievementRepository;
import dyn.repository.CharacterRepository;
import dyn.repository.FamilyRepository;
import dyn.repository.UserRepository;
import dyn.service.AppearanceService;
import dyn.service.RaceService;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Locale;
import java.util.Set;

@Controller
public class GameController {
    private static final Logger logger = LogManager.getLogger(GameController.class);
    @Autowired
    MessageSource messageSource;

    @Autowired
    AppearanceService app;
    @Autowired
    RaceService raceService;

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private FamilyRepository familyRepository;
    @Autowired
    private CharacterRepository characterRepository;

    @Autowired
    private AchievementRepository achievementRepository;


    public static Locale loc() {
        return LocaleContextHolder.getLocale();
    }

    public static UserDetails getAuthUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return (UserDetails) auth.getPrincipal();
    }

    @RequestMapping("/game")
    public String main(ModelMap model, RedirectAttributes redirectAttributes) {
        User user = userRepository.findByUserName(getAuthUser().getUsername());
        model.addAttribute("user", user);

        List<Family> families = user.getFamilies();
        if (families.size() == 0) {
            logger.info(user.getUserName() + " doesn't have any family, redirect to create the first family");
            redirectAttributes.addFlashAttribute("mess", messageSource.getMessage("new.user", null, loc()));
            return "redirect:/game/addNewFamily";
        }
        Family family = user.getCurrentFamily();
        logger.debug(user.getUserName() + " Current family: " + family.getFamilyName() + ", level: " + family.getLevel());
        model.addAttribute("currentFamily", family);

        List<Character> fathers;
        if (family.getLevel() > 0) {
            fathers = characterRepository.findByFamilyAndLevelAndSexAndSpouseIsNotNull(family, family.getLevel() - 1, "male");
        } else {
            fathers = characterRepository.findByFamilyAndLevel(family, family.getLevel());
        }
        model.addAttribute("fathers", fathers);

        return "game";
    }

    @RequestMapping("/game/achievements")
    public String characterView(ModelMap model, RedirectAttributes redirectAttributes) {
        User user = userRepository.findByUserName(getAuthUser().getUsername());
        Set<Achievement> achievements = user.getAchievements();

        model.addAttribute("achievements", achievements);
        return "/game/awarded";
    }


    @RequestMapping("/game/character")
    public String characterView(ModelMap model, RedirectAttributes redirectAttributes,
                                @RequestParam(value = "characterId") long characterId) {
        Character character = characterRepository.findOne(characterId);
        model.addAttribute("character", character);

        return "/game/character";
    }

    @RequestMapping(value = "/game/turn", method = RequestMethod.POST)
    public String turn(ModelMap model, RedirectAttributes redirectAttributes) {
        User user = userRepository.findByUserName(getAuthUser().getUsername());
        logger.info(user.getUserName() + " makes a turn!");

        StringBuilder sb = new StringBuilder();

        Family family = user.getCurrentFamily();

        List<Character> characters = characterRepository.findByFamilyAndLevelAndSexAndSpouseIsNotNull(family, family.getLevel(), "male");
        if (characters.isEmpty()) {
            redirectAttributes.addFlashAttribute("mess", messageSource.getMessage("turn.noCouplesToContinueTheDinasty", null, loc()));
            return "redirect:/game";
        }

        int newLevel = family.getLevel() + 1;
        for (Character character : characters) {

            double dominantPercent = 0.5; // whose feature is inherited, father or mother
            if (character.isBuffedBy(Buff.DOMINANT_MOTHER)) {
                dominantPercent = 0.2;
            }
            if (character.isBuffedBy(Buff.DOMINANT_FATHER)) {
                dominantPercent = 0.8;
            }

            double sonOrDaughterPercent = 0.5; // male or female child
            if (character.isBuffedBy(Buff.MANY_SONS)) {
                sonOrDaughterPercent = 0.8;
            }
            if (character.isBuffedBy(Buff.MANY_DAUGHTERS)) {
                sonOrDaughterPercent = 0.2;
            }

            double genModPercent = 0.05; // percentage of genetic modification
            if (character.isBuffedBy(Buff.GENETIC_MOD)) {
                genModPercent = 0.40;
            }
            if (character.isBuffedBy(Buff.FIVE_CHILDREN)) {
                genModPercent = 1.00;
            }

            Character wife = character.getSpouse();
            int childAmount;
            if (character.isBuffedBy(Buff.FIVE_CHILDREN)) {
                childAmount = 5;
            } else {
                childAmount = getAmountOfChildren(character);
            }

            logger.info(user.getUserName() + "'s character " + character.getName() + " marries " + wife.getName() + " and they have " + childAmount + " children");
            sb.append(messageSource.getMessage("turn.marriage", new Object[]{character.getName(), wife.getName(), childAmount}, loc()));
            sb.append("<br>");
            boolean genModded = false;

            for (int i = 0; i < childAmount; i++) {
                Character child = new Character();
                if (character.isBuffedBy(Buff.FIVE_CHILDREN)) {
                    if (i < 3) {
                        child.setSex("male");
                    } else {
                        child.setSex("female");
                    }
                } else {
                    if (Math.random() < sonOrDaughterPercent) {
                        child.setSex("male");
                    } else {
                        child.setSex("female");
                    }
                }

                if (child.getSex().equals("male")) {
                    child.setName(characterRepository.getRandomNameMale());
                } else {
                    child.setName(characterRepository.getRandomNameFemale());
                }
                sb.append(child.getName());


                child.setFather(character);
                child.setFamily(family);
                child.setLevel(newLevel);

                int featureToGenMod = 0; // which feature will have genetic modification
                if (genModded == false && Math.random() < genModPercent) {
                    featureToGenMod = (int) (1 + Math.random() * 12);
                    if (character.isBuffedBy(Buff.FIVE_CHILDREN)) {
                        genModded = true;
                    }
                }

                child.setBody(featureToGenMod == 1 ? app.getRandomBody(app.RARE) : (Math.random() < dominantPercent ? character.getBody() : wife.getBody()));
                child.setEars(featureToGenMod == 2 ? app.getRandomEars(app.RARE) : (Math.random() < dominantPercent ? character.getEars() : wife.getEars()));
                child.setEyebrows(featureToGenMod == 3 ? app.getRandomEyeBrows(app.RARE) : (Math.random() < dominantPercent ? character.getEyebrows() : wife.getEyebrows()));
                child.setEyeColor(featureToGenMod == 4 ? app.getRandomEyeColor(app.RARE) : (Math.random() < dominantPercent ? character.getEyeColor() : wife.getEyeColor()));
                child.setEyes(featureToGenMod == 5 ? app.getRandomEyes(app.RARE) : (Math.random() < dominantPercent ? character.getEyes() : wife.getEyes()));
                child.setHairColor(featureToGenMod == 6 ? app.getRandomHairColor(app.RARE) : (Math.random() < dominantPercent ? character.getHairColor() : wife.getHairColor()));
                child.setHairType(featureToGenMod == 7 ? app.getRandomHairType(app.RARE) : (Math.random() < dominantPercent ? character.getHairType() : wife.getHairType()));
                child.setHead(featureToGenMod == 8 ? app.getRandomHead(app.RARE) : (Math.random() < dominantPercent ? character.getHead() : wife.getHead()));
                child.setHeight(featureToGenMod == 9 ? app.getRandomHeight(app.RARE) : (Math.random() < dominantPercent ? character.getHeight() : wife.getHeight()));
                child.setMouth(featureToGenMod == 10 ? app.getRandomMouth(app.RARE) : (Math.random() < dominantPercent ? character.getMouth() : wife.getMouth()));
                child.setNose(featureToGenMod == 11 ? app.getRandomNose(app.RARE) : (Math.random() < dominantPercent ? character.getNose() : wife.getNose()));
                child.setSkinColor(featureToGenMod == 12 ? app.getRandomSkinColor(app.RARE) : (Math.random() < dominantPercent ? character.getSkinColor() : wife.getSkinColor()));

                if (featureToGenMod > 0) {
                    String genModefeature = "";
                    if (featureToGenMod == 1) genModefeature = messageSource.getMessage("app.body", null, loc());
                    if (featureToGenMod == 2) genModefeature = messageSource.getMessage("app.ears", null, loc());
                    if (featureToGenMod == 3) genModefeature = messageSource.getMessage("app.eyebrows", null, loc());
                    if (featureToGenMod == 4) genModefeature = messageSource.getMessage("app.eye_color", null, loc());
                    if (featureToGenMod == 5) genModefeature = messageSource.getMessage("app.eyes", null, loc());
                    if (featureToGenMod == 6) genModefeature = messageSource.getMessage("app.hair_color", null, loc());
                    if (featureToGenMod == 7) genModefeature = messageSource.getMessage("app.hair_type", null, loc());
                    if (featureToGenMod == 8) genModefeature = messageSource.getMessage("app.head", null, loc());
                    if (featureToGenMod == 9) genModefeature = messageSource.getMessage("app.height", null, loc());
                    if (featureToGenMod == 10) genModefeature = messageSource.getMessage("app.mouth", null, loc());
                    if (featureToGenMod == 11) genModefeature = messageSource.getMessage("app.nose", null, loc());
                    if (featureToGenMod == 12) genModefeature = messageSource.getMessage("app.skin_color", null, loc());
                    sb.append(messageSource.getMessage("turn.genModObtained", new Object[]{genModefeature}, loc()));
                }

                child.setHairStyle(app.getRandomHairStyle(child.getSex(), child.getHairType()));

                Race race = raceService.defineRace(child);
                child.setRace(race);

                child.generateView();
                logger.info("   child: " + child.getName() + ", genModFeature = " + featureToGenMod + ", race: " + race.getName());
                characterRepository.save(child);

                Achievement achievement = achievementRepository.findByTypeAndForWhat(AchievementType.newborn, race.getName());
                if (achievement != null && !user.getAchievements().contains(achievement)) {
                    user.getAchievements().add(achievement);
                    logger.info(user.getUserName() + " is awarded! Achievement: " + achievement.getName());
                    String locAchievementName = messageSource.getMessage(achievement.getName(), null, loc());
                    sb.append(messageSource.getMessage("turn.achievement", new Object[]{locAchievementName}, loc()));
                    userRepository.save(user);
                }
                sb.append("<br>");
            }
            sb.append("<br>");

            family.setLevel(newLevel);
            familyRepository.save(family);
        }
        redirectAttributes.addFlashAttribute("mess", sb.toString());
        return "redirect:/game";
    }

    @RequestMapping(value = "/logout", method = RequestMethod.GET)
    public String logout(HttpServletRequest request, HttpServletResponse response) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth != null) {
            new SecurityContextLogoutHandler().logout(request, response, auth);
            request.getSession().invalidate();
        }
        return "redirect:/";
    }

    private int getAmountOfChildren(Character character) {
        double[] percentage = new double[]{0.25, 0.55, 0.80, 0.90, 0.95, 0.98, 1.00}; // normal
        if (character.isBuffedBy(Buff.FERTILITY)) {
            percentage = new double[]{0.05, 0.15, 0.30, 0.50, 0.75, 0.90, 1.00}; // buff fertility
        }

        double random = Math.random();
        if (0 <= random && random < percentage[0]) {
            return 1;
        } else if (percentage[0] <= random && random < percentage[1]) {
            return 2;
        } else if (percentage[1] <= random && random < percentage[2]) {
            return 3;
        } else if (percentage[2] <= random && random < percentage[3]) {
            return 4;
        } else if (percentage[3] <= random && random < percentage[4]) {
            return 5;
        } else if (percentage[4] <= random && random < percentage[5]) {
            return 6;
        } else if (percentage[5] <= random && random < percentage[6]) {
            return 7;
        }

        return 1;
    }
}
