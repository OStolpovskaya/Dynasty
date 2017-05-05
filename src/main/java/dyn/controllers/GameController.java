package dyn.controllers;

/**
 * Created by OM on 17.02.2017.
 */


import dyn.model.*;
import dyn.model.Character;
import dyn.model.career.Career;
import dyn.model.career.Vocation;
import dyn.repository.CharacterRepository;
import dyn.repository.FamilyRepository;
import dyn.repository.UserRepository;
import dyn.service.*;
import dyn.utils.ResourcesUtils;
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
import java.util.*;

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
    CareerService careerService;
    @Autowired
    AchievementService achievementService;

    @Autowired
    FamilyLogService familyLogService;

    @Autowired
    HouseService houseService;

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private FamilyRepository familyRepository;
    @Autowired
    private CharacterRepository characterRepository;

    public static Locale loc() {
        return LocaleContextHolder.getLocale();
    }

    public static UserDetails getAuthUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return (UserDetails) auth.getPrincipal();
    }

    @RequestMapping("/game")
    public String main(ModelMap model, RedirectAttributes redirectAttributes) {
        System.out.println("GameController.main");
        long startTime = System.currentTimeMillis();

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
        model.addAttribute("family", family);

        List<Character> fathers;
        if (family.getLevel() > 0) {
            fathers = characterRepository.findByFamilyAndLevelAndSexAndSpouseIsNotNull(family, family.getLevel() - 1, "male");
        } else {
            fathers = characterRepository.findByFamilyAndLevel(family, family.getLevel());
        }

        List<Item> buffs = houseService.getBuffsInStorage(family);
        model.addAttribute("buffs", buffs);

        Map<Project, List<Item>> buffsForParents = new LinkedHashMap<>();
        Map<Project, List<Item>> buffsMarried = new LinkedHashMap<>();
        Map<Project, List<Item>> buffsResources = new LinkedHashMap<>();
        Map<Project, List<Item>> buffsSkillImprovement = new LinkedHashMap<>();
        Map<Project, List<Item>> buffsFamily = new LinkedHashMap<>();
        Map<Project, List<Item>> buffsForChildrenChange = new LinkedHashMap<>();

        for (Item item : buffs) {
            Project project = item.getProject();
            Thing thing = project.getThing();
            if (thing.getId() == Const.THING_CHILDREN_BUFF) {
                if (!buffsForChildrenChange.containsKey(project)) {
                    buffsForChildrenChange.put(project, new ArrayList<>());
                }
                buffsForChildrenChange.get(project).add(item);
            }
            if (thing.getId() == Const.THING_SKILL_IMPROVEMENT) {
                if (!buffsSkillImprovement.containsKey(project)) {
                    buffsSkillImprovement.put(project, new ArrayList<>());
                }
                buffsSkillImprovement.get(project).add(item);
            }
            if (thing.getId() == Const.THING_MARRIED_BUFF) {
                if (!buffsMarried.containsKey(project)) {
                    buffsMarried.put(project, new ArrayList<>());
                }
                buffsMarried.get(project).add(item);
            }
            if (thing.getId() == Const.THING_PARENTS_BUFF) {
                if (!buffsForParents.containsKey(project)) {
                    buffsForParents.put(project, new ArrayList<>());
                }
                buffsForParents.get(project).add(item);
            }
            if (thing.getId() == Const.THING_FAMILY_BUFF) {
                if (!buffsFamily.containsKey(project)) {
                    buffsFamily.put(project, new ArrayList<>());
                }
                buffsFamily.get(project).add(item);
            }
            if (thing.getId() == Const.THING_RESOURCES_SERTIFICATE) {
                if (!buffsResources.containsKey(project)) {
                    buffsResources.put(project, new ArrayList<>());
                }
                buffsResources.get(project).add(item);
            }
        }
        model.addAttribute("buffsSkillImprovement", buffsSkillImprovement);
        model.addAttribute("buffsMarried", buffsMarried);
        model.addAttribute("buffsForParents", buffsForParents);
        model.addAttribute("buffsForChildrenChange", buffsForChildrenChange);
        model.addAttribute("buffsFamily", buffsFamily);
        model.addAttribute("buffsResources", buffsResources);


        Map<Thing, Map<Project, List<Item>>> itemMap = new HashMap<>();
        for (Item item : buffs) {

            Thing thing = item.getProject().getThing();
            if (!itemMap.containsKey(thing)) {
                itemMap.put(thing, new HashMap<>());
            }
            Map<Project, List<Item>> projectMap = itemMap.get(thing);
            Project project = item.getProject();
            if (!projectMap.containsKey(project)) {
                projectMap.put(project, new ArrayList<>());
            }
            projectMap.get(project).add(item);
        }
        model.addAttribute("itemMap", itemMap);

        Map<Character, List<Character>> fathersMap = new LinkedHashMap<>();
        for (Character father : fathers) {
            fathersMap.put(father, father.getChildren());
        }
        model.addAttribute("fathers", fathersMap);


        model.addAttribute("familyLog", familyLogService.getLevelFamilyLog(family));

        long endTime = System.currentTimeMillis();

        System.out.println("That took " + (endTime - startTime) + " milliseconds");
        return "game";
    }

    @RequestMapping("/game/achievements")
    public String characterView(ModelMap model, RedirectAttributes redirectAttributes) {
        User user = userRepository.findByUserName(getAuthUser().getUsername());
        Family family = user.getCurrentFamily();
        model.addAttribute("family", family);

        Set<Achievement> achievements = user.getAchievements();
        model.addAttribute("achievements", achievements);

        List<Character> characters = new ArrayList<>();
        characters.add(characterRepository.findOne(1357L));
        characters.add(characterRepository.findFirstBySex("female"));
        model.addAttribute("characters", characters);
        return "/game/awarded";
    }


    @RequestMapping("/game/character")
    public String characterView(ModelMap model, RedirectAttributes redirectAttributes,
                                @RequestParam(value = "characterId") long characterId) {
        User user = userRepository.findByUserName(getAuthUser().getUsername());
        Family family = user.getCurrentFamily();
        model.addAttribute("family", family);

        Character character = characterRepository.findOne(characterId);
        model.addAttribute("character", character);

        return "/game/character";
    }

    @RequestMapping("/game/vocation")
    public String vocationView(ModelMap model, RedirectAttributes redirectAttributes,
                               @RequestParam(value = "vocationId") long vocationId) {
        User user = userRepository.findByUserName(getAuthUser().getUsername());
        Family family = user.getCurrentFamily();
        model.addAttribute("family", family);

        Vocation vocation = careerService.getVocation(vocationId);
        if (vocation != null) {
            model.addAttribute("vocation", vocation);
            return "/game/vocation";
        }

        redirectAttributes.addFlashAttribute("mess", "Призвание не найдено");
        logger.error(user.getUserName() + " tried to see non-existing vocation");
        return "redirect:/game";
    }

    @RequestMapping("/game/improveEducation")
    // todo: проверить, это гет-запрос?
    public String improveEducation(ModelMap model, RedirectAttributes redirectAttributes,
                                   @RequestParam(value = "character") long characterId) {
        User user = userRepository.findByUserName(getAuthUser().getUsername());
        Family family = user.getCurrentFamily();

        // Character character = characterRepository.findByIdAndFamilyAndLevel(characterId, family, family.getLevel());
        Character character = characterRepository.findOne(characterId);
        if (character != null && character.getLevel() == family.getLevel()) {
            boolean condition = false; // повысить образование можно у сыновей, дочерей и жен сыновей - проверка всего этого ниже
            switch (character.getSex()) {
                case "male":
                    condition = character.getFamily() == family;
                    break;
                case "female":
                    condition = character.getFamily() == family;
                    if (!condition && character.getSpouse() != null) {
                        condition = character.getSpouse().getFamily() == family;
                    }
                    break;
            }
            if (condition) {
                if (careerService.mayImproveEducation(character.getCareer())) {
                    if (family.getMoney() >= Career.IMPROVE_COST) {

                        careerService.improveEducation(character.getCareer());
                        characterRepository.save(character);

                        family.setMoney(family.getMoney() - Career.IMPROVE_COST);
                        familyRepository.save(family);

                        redirectAttributes.addFlashAttribute("mess", "Персонаж " + character.getName() + " повысил свое образование. Потрачено: " + Career.IMPROVE_COST);
                        logger.info(user.getUserName() + " improve the education!");
                        return "redirect:/game#char" + character.getFather().getId();
                    } else {
                        logger.error(user.getUserName() + " hasn't enough money to improve education");
                        redirectAttributes.addFlashAttribute("mess", "Недостаточно денег");
                        return "redirect:/game";
                    }
                }
            }
            redirectAttributes.addFlashAttribute("mess", "Вы можете повысить образование только у сыновей, дочерей или жен сыновей. Персонаж никем из них не является.");
            logger.error(user.getUserName() + "'s character " + characterId + " try to improve education, but he/she is not son, daughter or son's wife");
            return "redirect:/game";
        }
        redirectAttributes.addFlashAttribute("mess", "Персонаж не найден");
        logger.error(user.getUserName() + "'s character " + characterId + " can not belongs to user's current family");
        return "redirect:/game";
    }

    @RequestMapping(value = "/game/turn", method = RequestMethod.POST)
    public String turn(ModelMap model, RedirectAttributes redirectAttributes) {
        User user = userRepository.findByUserName(getAuthUser().getUsername());
        logger.info(user.getUserName() + " makes a turn!");

        StringBuilder sb = new StringBuilder();

        Family family = user.getCurrentFamily();

        // profession
        ResourcesUtils resourcesUtils = new ResourcesUtils();
        resourcesUtils.saveInitValues(family);

        List<Character> workers = characterRepository.findByFamilyAndLevel(family, family.getLevel());
        for (Character worker : workers) {
            if (worker.getSex().equals("male")) {
                genProfession(worker, family, user, sb);
                if (worker.hasSpouse()) {
                    genProfession(worker.getSpouse(), family, user, sb);
                }
            } else {
                if (!worker.isFiancee() && !worker.hasSpouse()) {
                    genProfession(worker, family, user, sb);
                }
            }
        }

        // children
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
            if (character.isBuffedBy(Buff.SIX_CHILDREN)) {
                genModPercent = 1.00;
            }

            Character wife = character.getSpouse();
            int childAmount;
            if (character.isBuffedBy(Buff.SIX_CHILDREN)) {
                childAmount = 6;
            } else {
                childAmount = getAmountOfChildren(character);
            }
            if (character.isBuffedBy(Buff.ONE_MORE_CHILD)) {
                childAmount += 1;
            }

            logger.info(user.getUserName() + "'s character " + character.getName() + " marries " + wife.getName() + " and they have " + childAmount + " children");
            sb.append(messageSource.getMessage("turn.marriage", new Object[]{character.getName(), wife.getName(), childAmount}, loc()));
            sb.append("<br>");
            boolean genModded = false;

            for (int i = 0; i < childAmount; i++) {
                Character child = new Character();
                if (character.isBuffedBy(Buff.SIX_CHILDREN)) {
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
                    if (character.isBuffedBy(Buff.SIX_CHILDREN)) {
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

                // vocation and skills
                child.setCareer(new Career());
                careerService.inheritVocationAndSkills(child.getCareer(), character.getCareer(), wife.getCareer());

                child.generateView();
                logger.info("   child: " + child.getName() + ", genModFeature = " + featureToGenMod + ", race: " + race.getName());
                characterRepository.save(child);

                Achievement achievement = achievementService.checkAchievement(AchievementType.newborn, user, child);
                if (achievement != null) {
                    String locAchievementName = messageSource.getMessage(achievement.getName(), null, loc());
                    sb.append(messageSource.getMessage("turn.achievement", new Object[]{locAchievementName}, loc()));
                }
                sb.append("<br>");
            }
            sb.append("<br>");

        }
        family.setLevel(newLevel);
        family.setCraftPoint(family.getCraftPoint() + 1);
        family.setPairsNum(0);
        family.setFianceeNum(0);
        familyRepository.save(family);

        sb.append("Всего получено: <br>");
        sb.append(resourcesUtils.getDifference(family));

        familyLogService.createNewLevelFamilyLog(family, sb.toString());

        redirectAttributes.addFlashAttribute("mess", sb.toString());
        return "redirect:/game";
    }

    public void genProfession(Character worker, Family family, User user, StringBuilder sb) {
        int inc = 0;
        careerService.generateProfession(worker);
        int salary = worker.getCareer().getResultSalary();
        if (worker.isBuffedBy(Buff.SALARY_INC)) {
            inc = (int) (0.5 * salary);
        }
        family.setMoney(family.getMoney() + salary + inc);
        sb.append(worker.getName() + " приобретает профессию " + worker.getCareer().getProfession().getName() + " (" + worker.getCareer().getProfession().getLevel() + ") и зарабатывает " + salary + " р. " +
                (inc == 0 ? "" : "Премия: " + inc + " р. "));

        family.getFamilyResources().addResFromVocation(worker.getCareer());
        sb.append("А призвание приносит ресурсы: " + worker.getCareer().getVocation().resString(worker.getCareer().getProfession().getLevel()) + "<br>");

        Achievement achievement = achievementService.checkAchievement(AchievementType.vocation10level, user, worker);
        if (achievement != null) {
            String locAchievementName = messageSource.getMessage(achievement.getName(), null, loc());
            sb.append(messageSource.getMessage("turn.achievement", new Object[]{locAchievementName}, loc()));
        }
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
        double[] percentage = new double[]{0.20, 0.60, 0.85, 0.93, 0.97, 0.99, 1.00}; // normal
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
