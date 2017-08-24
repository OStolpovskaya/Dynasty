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
import dyn.utils.ResUtils;
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
    CharacterService characterService;

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
        //System.out.println("***GameController.main***");
        //long startTime = System.currentTimeMillis();

        User user = userRepository.findByUserName(getAuthUser().getUsername());
        model.addAttribute("user", user);

        List<Family> families = user.getFamilies();
        if (families.size() == 0) {
            logger.info(user.getUserName() + " doesn't have any family, redirect to create the first family");
            redirectAttributes.addFlashAttribute("mess", messageSource.getMessage("new.user", null, loc()));
            return "redirect:/game/addNewFamily";
        }
        Family family = user.getCurrentFamily();
        //logger.debug(user.getUserName() + " Current family: " + family.getFamilyName() + ", level: " + family.getLevel());
        model.addAttribute("family", family);

        //System.out.println("GET FATHERS");
        List<Character> fathers;
        if (family.getLevel() > 0) {
            fathers = characterRepository.findByFamilyAndLevelAndSexAndSpouseIsNotNull(family, family.getLevel() - 1, "male");
        } else {
            fathers = characterRepository.findByFamilyAndLevel(family, family.getLevel());
        }
        //System.out.println("GET CHILDREN");
        Map<Character, List<Character>> fathersMap = new LinkedHashMap<>();
        for (Character father : fathers) {
            //System.out.println("FATHER "+father.getName());
            List<Character> children = father.getChildren();
            for (Character child : children) {
                //System.out.println("CHILD MAYIMPROVEEDUCATION");
                Career career = child.getCareer();
                career.mayImproveEducation = careerService.mayImproveEducation(career);
            }
            fathersMap.put(father, children);
        }
        model.addAttribute("fathers", fathersMap);

        //System.out.println("BUFFS");
        List<Item> buffs = houseService.getBuffsInStorage(family);
        model.addAttribute("buffs", buffs);

        Map<Project, List<Item>> buffsForParents = new LinkedHashMap<>();
        Map<Project, List<Item>> buffsMarried = new LinkedHashMap<>();
        Map<Project, List<Item>> buffsResources = new LinkedHashMap<>();
        Map<Project, List<Item>> buffsSkillImprovement = new LinkedHashMap<>();
        Map<Project, List<Item>> buffsFamily = new LinkedHashMap<>();
        Map<Project, List<Item>> buffsForChildrenChange = new LinkedHashMap<>();

        //System.out.println("BUFFS PARSING");
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


       /* Map<Thing, Map<Project, List<Item>>> itemMap = new HashMap<>();
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
        model.addAttribute("itemMap", itemMap);*/
        //System.out.println("FAMILY LOG");
        model.addAttribute("familyLog", familyLogService.getLevelFamilyLog(family));
        //System.out.println("END");
        //long endTime = System.currentTimeMillis();
        //System.out.println("That took " + (endTime - startTime) + " milliseconds");
        return "game";
    }

    @RequestMapping("/game/vertTree")
    public String thingTreeView(ModelMap model, RedirectAttributes redirectAttributes) {
        User user = userRepository.findByUserName(getAuthUser().getUsername());
        Family family = user.getCurrentFamily();

        Character founder = characterRepository.findByFamilyAndLevelAndSexAndSpouseIsNotNull(family, 0, "male").get(0);
        model.addAttribute("parent", founder);

        return "game/vertTree";
    }

    @RequestMapping("/game/achievements")
    public String characterView(ModelMap model, RedirectAttributes redirectAttributes) {
        User user = userRepository.findByUserName(getAuthUser().getUsername());
        Family family = user.getCurrentFamily();
        model.addAttribute("family", family);

        List<UserAchievements> achievements = achievementService.getAchievementsOfUser(user);
        model.addAttribute("achievements", achievements);

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
                        logger.info(user.getUserName() + " improve the education for character " + character.getName());
                        if (character.getFamily() != family) {
                            return "redirect:/game#char" + character.getSpouse().getFather().getId();
                        }
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
        Family family = user.getCurrentFamily();

        // check that we have pairs to continue dynasty
        List<Character> levelFathers = characterRepository.findByFamilyAndLevelAndSexAndSpouseIsNotNull(family, family.getLevel(), "male");
        if (levelFathers.isEmpty()) {
            redirectAttributes.addFlashAttribute("mess", messageSource.getMessage("turn.noCouplesToContinueTheDinasty", null, loc()));
            return "redirect:/game";
        }

        logger.info(user.getUserName() + " makes a turn!");

        StringBuilder turnLog = new StringBuilder();
        StringBuilder turnAchievements = new StringBuilder();

        // save end-turn money, craft points and resources:
        FamilyLog previousTurnLog = familyLogService.saveEndTurn(family);


        // WORKERS: obtain money and resources by workers
        List<Character> charactersOnLevel = characterRepository.findByFamilyAndLevel(family, family.getLevel());
        workersProcessing(user, family, charactersOnLevel, turnLog, turnAchievements);

        // CHILDREN:
        childrenProcessing(user, family, levelFathers, turnLog, turnAchievements);

        family.setLevel(family.getLevel() + 1);
        family.setCraftPoint(family.getCraftPoint() + Const.CRAFT_POINTS_FOR_LEVEL);
        family.setPairsNum(0);
        family.setFianceeNum(0);
        familyRepository.save(family);

        StringBuilder turnIncome = new StringBuilder();
        turnIncome.append("<strong>Всего получено за ход:</strong> <br>");
        turnIncome.append(" Деньги: ").append(family.getMoney() - previousTurnLog.getMoney()).append("<br>");
        turnIncome.append(" Ресурсы: ").append(ResUtils.differenceToString(previousTurnLog, family.getFamilyResources())).append("<br>");
        turnIncome.append(" Крафт баллы: ").append(family.getCraftPoint() - previousTurnLog.getCraftpoint()).append("<br>");

        turnLog.append(turnIncome);

        familyLogService.createNewLevelFamilyLog(family, turnLog.toString());

        // deleting image
        for (Character levelChar : charactersOnLevel) {
            if ((levelChar.getSex().equals("male") && !levelChar.hasSpouse()) || (levelChar.getSex().equals("female") && !levelChar.isFiancee() && !levelChar.hasSpouse())) {
                levelChar.setView(null);
                characterRepository.save(levelChar);
            }
        }

        if (!turnAchievements.toString().equals("")) {
            turnIncome.append(" <strong>Достижения:</strong> <br>").append(turnAchievements);
        }

        String flashAttribute = turnIncome.toString();
        redirectAttributes.addFlashAttribute("mess", flashAttribute);
        return "redirect:/game";
    }

    public void childrenProcessing(User user, Family family, List<Character> levelFathers, StringBuilder turnLog, StringBuilder turnAchievements) {
        //  fertility calculation
        float houseQualityFertilitySub = houseService.countHouseQualityFertilitySub(family.getHouseQuality());
        logger.debug(family.familyNameAndUserName() + " houseQualityFertilitySub = " + houseQualityFertilitySub);

        float[] standartFertility = new float[]{
                0.20f - houseQualityFertilitySub,
                0.60f - houseQualityFertilitySub,
                0.85f - houseQualityFertilitySub,
                0.93f - houseQualityFertilitySub,
                0.97f - houseQualityFertilitySub,
                0.99f - houseQualityFertilitySub,
                1.00f};
        float[] buffedFertility = new float[]{
                ((0.01f - houseQualityFertilitySub) < 0 ? 0 : (0.01f - houseQualityFertilitySub)),
                0.15f - houseQualityFertilitySub,
                0.30f - houseQualityFertilitySub,
                0.50f - houseQualityFertilitySub,
                0.75f - houseQualityFertilitySub,
                0.90f - houseQualityFertilitySub,
                1.00f};

        for (Character character : levelFathers) {
            boolean firstTurn = character.isBuffedBy(Buff.SIX_CHILDREN);

            double fatherFeaturePercent = 0.5; // whose feature is inherited, father or mother
            if (character.isBuffedBy(Buff.DOMINANT_MOTHER)) {
                fatherFeaturePercent = 0.2;
            }
            if (character.isBuffedBy(Buff.DOMINANT_FATHER)) {
                fatherFeaturePercent = 0.8;
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

            Character wife = character.getSpouse();
            int childAmount;
            if (firstTurn) {
                childAmount = 6;
            } else {
                if (character.isBuffedBy(Buff.FERTILITY)) {
                    childAmount = getAmountOfChildren(buffedFertility);
                } else {
                    childAmount = getAmountOfChildren(standartFertility);
                }

            }
            if (character.isBuffedBy(Buff.ONE_MORE_CHILD)) {
                childAmount += 1;
            }

            logger.info(user.getUserName() + "'s character " + character.getName() + " marries " + wife.getName() + " and they have " + childAmount + " children");
            turnLog.append(messageSource.getMessage("turn.marriage", new Object[]{character.getName(), wife.getName(), childAmount}, loc()));
            turnLog.append("<br>");

            for (int childSeqNum = 0; childSeqNum < childAmount; childSeqNum++) {
                Character child = new Character();

                // main properties
                child.setSex(getSexForNewChild(firstTurn, sonOrDaughterPercent, childSeqNum));
                child.setName(characterService.getNameForNewChild(child));
                child.setFather(character);
                child.setFamily(family);
                child.setLevel(family.getLevel() + 1);

                // features
                child.setBody(Math.random() < fatherFeaturePercent ? character.getBody() : wife.getBody());
                child.setEars(Math.random() < fatherFeaturePercent ? character.getEars() : wife.getEars());
                child.setEyebrows(Math.random() < fatherFeaturePercent ? character.getEyebrows() : wife.getEyebrows());
                child.setEyeColor(Math.random() < fatherFeaturePercent ? character.getEyeColor() : wife.getEyeColor());
                child.setEyes(Math.random() < fatherFeaturePercent ? character.getEyes() : wife.getEyes());
                child.setHairColor(Math.random() < fatherFeaturePercent ? character.getHairColor() : wife.getHairColor());
                child.setHairType(Math.random() < fatherFeaturePercent ? character.getHairType() : wife.getHairType());
                child.setHead(Math.random() < fatherFeaturePercent ? character.getHead() : wife.getHead());
                child.setHeight(Math.random() < fatherFeaturePercent ? character.getHeight() : wife.getHeight());
                child.setMouth(Math.random() < fatherFeaturePercent ? character.getMouth() : wife.getMouth());
                child.setNose(Math.random() < fatherFeaturePercent ? character.getNose() : wife.getNose());
                child.setSkinColor(Math.random() < fatherFeaturePercent ? character.getSkinColor() : wife.getSkinColor());

                turnLog.append(child.getName());

                // apply genetic modification if needed
                String feature = "";
                if ((firstTurn && childSeqNum == 0) || (!firstTurn && Math.random() < genModPercent)) {
                    int featureToModify = (int) (1 + Math.random() * 12);
                    switch (featureToModify) {
                        case 1:
                            child.setBody(app.getRandomBody(app.RARE));
                            feature = messageSource.getMessage("app.body", null, loc()) + ": " + child.getBody().getTitle();
                            break;
                        case 2:
                            child.setEars(app.getRandomEars(app.RARE));
                            feature = messageSource.getMessage("app.ears", null, loc()) + ": " + child.getEars().getTitle();
                            break;
                        case 3:
                            child.setEyebrows(app.getRandomEyeBrows(app.RARE));
                            feature = messageSource.getMessage("app.eyebrows", null, loc()) + ": " + child.getEyebrows().getTitle();
                            break;
                        case 4:
                            child.setEyeColor(app.getRandomEyeColor(app.RARE));
                            feature = messageSource.getMessage("app.eye_color", null, loc()) + ": " + child.getEyeColor().getTitle();
                            break;
                        case 5:
                            child.setEyes(app.getRandomEyes(app.RARE));
                            feature = messageSource.getMessage("app.eyes", null, loc()) + ": " + child.getEyes().getTitle();
                            break;
                        case 6:
                            child.setHairColor(app.getRandomHairColor(app.RARE));
                            feature = messageSource.getMessage("app.hair_color", null, loc()) + ": " + child.getHairColor().getTitle();
                            break;
                        case 7:
                            child.setHairType(app.getRandomHairType(app.RARE));
                            feature = messageSource.getMessage("app.hair_type", null, loc()) + ": " + child.getHairType().getTitle();
                            break;
                        case 8:
                            child.setHead(app.getRandomHead(app.RARE));
                            feature = messageSource.getMessage("app.head", null, loc()) + ": " + child.getHead().getTitle();
                            break;
                        case 9:
                            child.setHeight(app.getRandomHeight(app.RARE));
                            feature = messageSource.getMessage("app.height", null, loc()) + ": " + child.getHeight().getTitle();
                            break;
                        case 10:
                            child.setMouth(app.getRandomMouth(app.RARE));
                            feature = messageSource.getMessage("app.mouth", null, loc()) + ": " + child.getMouth().getTitle();
                            break;
                        case 11:
                            child.setNose(app.getRandomNose(app.RARE));
                            feature = messageSource.getMessage("app.nose", null, loc()) + ": " + child.getNose().getTitle();
                            break;
                        case 12:
                            child.setSkinColor(app.getRandomSkinColor(app.RARE));
                            feature = messageSource.getMessage("app.skin_color", null, loc()) + ": " + child.getSkinColor().getTitle();
                            break;
                        default:
                            feature = "Error: " + featureToModify;
                    }
                    turnLog.append(messageSource.getMessage("turn.genModObtained", new Object[]{feature}, loc()));
                }
                // hairstyle after set hair type
                child.setHairStyle(app.getRandomHairStyle(child.getSex(), child.getHairType()));

                // picture
                child.generateView();

                // race and check newborn achievement
                child.setRace(raceService.defineRace(child));
                Achievement achievement = achievementService.checkAchievement(AchievementType.newborn, user, child);
                if (achievement != null) {
                    turnLog.append(messageSource.getMessage("turn.achievement", new Object[]{achievement.getName()}, loc()));
                    turnAchievements.append(child.getName()).append(": ").append(achievement.getName()).append("<br>");
                }

                // vocation and skills
                child.setCareer(new Career());
                careerService.inheritVocationAndSkills(child.getCareer(), character.getCareer(), wife.getCareer());

                logger.info("   child: " + child.getName() + ", genModFeature = " + feature + ", race: " + child.getRace().getName());
                characterRepository.save(child);

                turnLog.append("<br>");
            }
            turnLog.append("<br>");

        }
    }

    public void workersProcessing(User user, Family family, List<Character> charactersOnLevel, StringBuilder turnLog, StringBuilder turnAchievements) {
        // workers: sons, their wifes and non-fiancee single daughters
        List<Character> workers = new ArrayList<>();
        for (Character character : charactersOnLevel) {
            if (character.getSex().equals("male")) {
                workers.add(character);
                if (character.hasSpouse()) {
                    workers.add(character.getSpouse());
                }
            } else {
                if (!character.isFiancee() && !character.hasSpouse()) {
                    workers.add(character);
                }
            }
        }

        // salary coeff for house quality
        float houseQualitySalaryCoeff = houseService.countHouseQualitySalaryCoeff(family.getHouseQuality());
        logger.debug(family.familyNameAndUserName() + " houseQualitySalaryCoeff = " + houseQualitySalaryCoeff);

        // processing
        for (Character worker : workers) {
            // money and resources from career:
            careerService.generateProfession(worker);

            Career career = worker.getCareer();
            int salary = career.getResultSalary();

            int inc = 0;
            if (worker.isBuffedBy(Buff.SALARY_INC)) {
                inc = (int) (0.5 * salary);
            }

            int houseInc = (int) (houseQualitySalaryCoeff * salary);

            family.setMoney(family.getMoney() + salary + inc + houseInc);
            family.getFamilyResources().addResFromVocation(career);

            turnLog.append(worker.getName() + " приобретает профессию " + career.getProfession().getName() + " (" + career.getProfession().getLevel() + ") и зарабатывает " + salary + " р. " +
                    (inc == 0 ? "" : "Премия: " + inc + " р. ") +
                    (houseInc == 0 ? "" : "Надбавка за дом: " + houseInc + " р. "));

            turnLog.append("А призвание приносит ресурсы: " + career.getVocation().resString(career.getProfession().getLevel()) + ". ");

            // achievement career 10 level
            Achievement achievement = achievementService.checkAchievement(AchievementType.vocation10level, user, worker);
            if (achievement != null) {
                turnLog.append(messageSource.getMessage("turn.achievement", new Object[]{achievement.getName()}, loc()));
                turnAchievements.append(worker.getName()).append(": ").append(achievement.getName()).append("<br>");
            }

            //resources from race
            Race race = worker.getRace();
            Long raceId = race.getId();
            if (raceId != Race.RACE_HUMAN && raceId != Race.RACE_GM_HUMAN) {
                family.getFamilyResources().addResFromRace(race);
                turnLog.append("Раса приносит ресурсы: " + ResUtils.getResString(race) + ". ");
            }
            turnLog.append("<br>");
        }
    }

    public String getSexForNewChild(boolean firstTurn, double sonOrDaughterPercent, int sequenceNumberOFChild) {
        String sex = "female";
        if (firstTurn) {
            if (sequenceNumberOFChild < 3) {
                sex = "male";
            }
        } else {
            if (Math.random() < sonOrDaughterPercent) {
                sex = "male";
            }
        }
        return sex;
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

    private int getAmountOfChildren(float[] percentage) {
        double random = Math.random();
        System.out.println("getAmountOfChildren: " + Arrays.toString(percentage) + ", random=" + random);
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

    //game/userview?userId
    @RequestMapping(value = "/game/userview", method = RequestMethod.GET)
    public String userview(ModelMap model, RedirectAttributes redirectAttributes,
                           @RequestParam(value = "userId") Long userId) {
        if (userId.equals(1L)) {
            redirectAttributes.addFlashAttribute("mess", "У разработчиков нет текущей семьи, просмотр не разрешен");
            return "redirect:/game";
        }
        User user = userRepository.findByUserName(getAuthUser().getUsername());
        Family family = user.getCurrentFamily();
        model.addAttribute("family", family);

        User player = userRepository.findOne(userId);
        if (player == null) {
            redirectAttributes.addFlashAttribute("mess", "Нет такого игрока");
            return "redirect:/game";
        }
        model.addAttribute("player", player);

        Family playerCurrentFamily = player.getCurrentFamily();
        model.addAttribute("playerCurrentFamily", playerCurrentFamily);

        List<Character> fathers;
        if (playerCurrentFamily.getLevel() > 0) {
            fathers = characterRepository.findByFamilyAndLevelAndSexAndSpouseIsNotNull(playerCurrentFamily, playerCurrentFamily.getLevel() - 1, "male");
        } else {
            fathers = characterRepository.findByFamilyAndLevel(playerCurrentFamily, playerCurrentFamily.getLevel());
        }

        Map<Character, List<Character>> fathersMap = new LinkedHashMap<>();
        for (Character father : fathers) {
            fathersMap.put(father, father.getChildren());
        }
        model.addAttribute("fathers", fathersMap);

        Set<Achievement> achievements = player.getAchievements();
        model.addAttribute("achievements", achievements);

        model.addAttribute("roomList", houseService.getRoomsByHouseId(playerCurrentFamily.getHouse().getId()));

        List<RoomView> roomViewList = houseService.getRoomMaps(playerCurrentFamily.getHouse(), playerCurrentFamily);
        model.addAttribute("roomViewList", roomViewList);

        List<House> buildingList = playerCurrentFamily.getBuildings();
        model.addAttribute("buildingList", buildingList);

        Map<House, List<RoomView>> buildingMap = new LinkedHashMap<>();
        for (House house : buildingList) {
            buildingMap.put(house, houseService.getRoomMaps(house, playerCurrentFamily));
        }
        model.addAttribute("buildingMap", buildingMap);

        return "game/userview";
    }
}
