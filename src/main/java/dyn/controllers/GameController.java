package dyn.controllers;

/**
 * Created by OM on 17.02.2017.
 */


import dyn.form.RaceAppearanceForm;
import dyn.model.*;
import dyn.model.Character;
import dyn.model.appearance.*;
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
import org.springframework.web.bind.annotation.ModelAttribute;
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
        long startTime = System.currentTimeMillis();

        User user = userRepository.findByUserName(getAuthUser().getUsername());
        model.addAttribute("user", user);

        List<Family> families = user.getFamilies();
        if (families.size() == 0 || (families.size() == 1 && families.get(0).isCurrent() == false)) {
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
        int freeDaughters = 0;
        int freeSons = 0;
        Map<Character, List<Character>> fathersMap = new LinkedHashMap<>();
        for (Character father : fathers) {
            //System.out.println("FATHER "+father.getName());
            List<Character> children = father.getChildren();
            fathersMap.put(father, children);

            for (Character child : children) {
                if (Objects.equals(child.getSex(), "male") && child.getSpouse() == null) {
                    freeSons += 1;
                }
                if (Objects.equals(child.getSex(), "female") && child.getSpouse() == null && !child.isFiancee()) {
                    freeDaughters += 1;
                }
            }
        }
        model.addAttribute("fathers", fathersMap);
        model.addAttribute("freeSons", freeSons);
        model.addAttribute("freeDaughters", freeDaughters);

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

        model.addAttribute("bridesNum", family.getHouse().getFianceeNum() - family.getFianceeNum());
        model.addAttribute("pairsNum", family.getHouse().getPairsNum() - family.getPairsNum());

        model.addAttribute("familyLog", familyLogService.getLevelFamilyLog(family));
        //System.out.println("END");
        long endTime = System.currentTimeMillis();
        logger.debug("@Game took " + (endTime - startTime) + " milliseconds");
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

    @RequestMapping("/game/changeView")
    public String changeView(ModelMap model,
                             @RequestParam(value = "gameView") GameView gameView,
                             RedirectAttributes redirectAttributes) {
        User user = userRepository.findByUserName(getAuthUser().getUsername());
        Family family = user.getCurrentFamily();
        family.setGameView(gameView);

        familyRepository.save(family);

        logger.debug(family.familyNameAndUserName() + " changed game view");
        return "redirect:/game";
    }

    @RequestMapping("/game/achievements")
    public String characterView(ModelMap model, RedirectAttributes redirectAttributes) {
        User user = userRepository.findByUserName(getAuthUser().getUsername());
        Family family = user.getCurrentFamily();
        if (family == null) {
            logger.debug(user.getUserName() + " doesn't have any family");
            redirectAttributes.addFlashAttribute("mess", messageSource.getMessage("new.user", null, loc()));
            return "redirect:/game/addNewFamily";
        }
        model.addAttribute("family", family);

        List<UserAchievements> achievements = achievementService.getAchievementsOfUser(user);
        model.addAttribute("achievements", achievements);

        return "/game/awarded";
    }

    @RequestMapping("/game/help")
    public String help(ModelMap model, RedirectAttributes redirectAttributes) {
        User user = userRepository.findByUserName(getAuthUser().getUsername());
        Family family = user.getCurrentFamily();
        if (family == null) {
            logger.debug(user.getUserName() + " doesn't have any family");
            redirectAttributes.addFlashAttribute("mess", messageSource.getMessage("new.user", null, loc()));
            return "redirect:/game/addNewFamily";
        }
        model.addAttribute("family", family);
        return "/game/help";
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
                               @RequestParam(value = "vocationId", defaultValue = "1") long vocationId) {
        User user = userRepository.findByUserName(getAuthUser().getUsername());
        Family family = user.getCurrentFamily();
        model.addAttribute("family", family);

        Vocation vocation = careerService.getVocation(vocationId);
        if (vocation != null) {
            model.addAttribute("vocation", vocation);
            model.addAttribute("vocationList", careerService.getVocationList());
            return "/game/vocation";
        }

        redirectAttributes.addFlashAttribute("mess", "Призвание не найдено");
        logger.error(family.familyNameAndUserName() + " tried to see non-existing vocation");
        return "redirect:/game";
    }

    @RequestMapping("/game/races")
    public String races(ModelMap model, @ModelAttribute("raceAppearanceForm") RaceAppearanceForm form) {
        User user = userRepository.findByUserName(getAuthUser().getUsername());
        Family family = user.getCurrentFamily();
        model.addAttribute("family", family);

        model.addAttribute("raceList", raceService.getAllRaces());

        model.addAttribute("bodyList", app.getBodyList(app.ALL));
        model.addAttribute("earsList", app.getEarsList(app.ALL));
        model.addAttribute("eyebrowsList", app.getEyebrowsList(app.ALL));
        model.addAttribute("eyeColorList", app.getEyeColorList(app.ALL));
        model.addAttribute("eyesList", app.getEyesList(app.ALL));
        model.addAttribute("hairColorList", app.getHairColorList(app.ALL));
        model.addAttribute("hairStyleList", app.getHairStyleList(app.ALL));
        model.addAttribute("hairTypeList", app.getHairTypeList(app.ALL));
        model.addAttribute("headList", app.getHeadList(app.ALL));
        model.addAttribute("heightList", app.getHeightList(app.ALL));
        model.addAttribute("mouthList", app.getMouthList(app.ALL));
        model.addAttribute("noseList", app.getNoseList(app.ALL));
        model.addAttribute("skinColorList", app.getSkinColorList(app.ALL));
        return "game/races";
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

        logger.info(family.familyNameAndUserName() + " makes a turn!");

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
        family.setGameView(GameView.defView);
        familyRepository.save(family);

        int income = family.getMoney() - previousTurnLog.getMoney();
        previousTurnLog.setIncome(income);

        StringBuilder turnIncome = new StringBuilder();
        turnIncome.append("<strong>Всего получено за ход:</strong> <br>");
        turnIncome.append(" Деньги: ").append(income).append("<br>");
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

        for (Character character : levelFathers) {
            boolean firstTurn = character.isBuffedBy(Buff.SEVEN_CHILDREN);

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

            double genModPercent = Const.PERCENTAGE_GEN_MOD;// 0.05; // percentage of genetic modification
            if (character.isBuffedBy(Buff.GENETIC_MOD)) {
                genModPercent = Const.PERCENTAGE_GEN_MOD_BUFFED;//0.40;
            }

            Character wife = character.getSpouse();

            int houseQualityInt = (int) Math.floor(family.getHouseQuality());

            int childAmount;
            if (firstTurn) {
                childAmount = 7;
            } else {
                if (character.isBuffedBy(Buff.FERTILITY)) {
                    childAmount = getAmountOfChildren(Const.FERTILITY_BUFFED[houseQualityInt]);
                } else {
                    childAmount = getAmountOfChildren(Const.FERTILITY_USUAL[houseQualityInt]);
                }
            }
            if (character.isBuffedBy(Buff.ONE_MORE_CHILD)) {
                childAmount += 1;
            }

            logger.info(family.familyNameAndUserName() + "'s character " + character.getName() + " marries " + wife.getName() + " and they have " + childAmount + " children");
            turnLog.append(messageSource.getMessage("turn.marriage", new Object[]{character.getName(), wife.getName(), childAmount}, loc()));
            turnLog.append("<br>");

            for (int childSeqNum = 0; childSeqNum < childAmount; childSeqNum++) {
                Character child = generateChild(user, family, character, wife, firstTurn, fatherFeaturePercent, sonOrDaughterPercent, genModPercent, childSeqNum, turnLog, turnAchievements);
                child.setLevel(family.getLevel() + 1);
                characterRepository.save(child);
            }
            turnLog.append("<br>");

        }
    }

    public Character generateChild(User user, Family family,
                                   Character father, Character mother,
                                   boolean firstTurn, double fatherFeaturePercent, double sonOrDaughterPercent, double genModPercent,
                                   int childSeqNum,
                                   StringBuilder log, StringBuilder logAchievements) {
        Character child = new Character();

        String sex = "female";
        if (firstTurn) {
            if (childSeqNum < 3) {
                sex = "male";
            }
        } else {
            if (Math.random() < sonOrDaughterPercent) {
                sex = "male";
            }
        }

        // main properties
        child.setSex(sex);
        child.setName(characterService.getNameForNewChild(child));
        child.setFather(father);
        child.setFamily(family);

        // features
        child.setBody(Math.random() < fatherFeaturePercent ? father.getBody() : mother.getBody());
        child.setEars(Math.random() < fatherFeaturePercent ? father.getEars() : mother.getEars());
        child.setEyebrows(Math.random() < fatherFeaturePercent ? father.getEyebrows() : mother.getEyebrows());
        child.setEyeColor(Math.random() < fatherFeaturePercent ? father.getEyeColor() : mother.getEyeColor());
        child.setEyes(Math.random() < fatherFeaturePercent ? father.getEyes() : mother.getEyes());
        child.setHairColor(Math.random() < fatherFeaturePercent ? father.getHairColor() : mother.getHairColor());
        child.setHairType(Math.random() < fatherFeaturePercent ? father.getHairType() : mother.getHairType());
        child.setHead(Math.random() < fatherFeaturePercent ? father.getHead() : mother.getHead());
        child.setHeight(Math.random() < fatherFeaturePercent ? father.getHeight() : mother.getHeight());
        child.setMouth(Math.random() < fatherFeaturePercent ? father.getMouth() : mother.getMouth());
        child.setNose(Math.random() < fatherFeaturePercent ? father.getNose() : mother.getNose());
        child.setSkinColor(Math.random() < fatherFeaturePercent ? father.getSkinColor() : mother.getSkinColor());

        log.append(child.getName());

        // apply genetic modification if needed
        String feature = "";
        if ((firstTurn && childSeqNum == 0) || (!firstTurn && Math.random() < genModPercent)) {
            Appearance randomFeature = app.getRandomFeature();
            if (randomFeature instanceof Body) {
                child.setBody((Body) randomFeature);
            }
            if (randomFeature instanceof Ears) {
                child.setEars((Ears) randomFeature);
            }
            if (randomFeature instanceof Eyebrows) {
                child.setEyebrows((Eyebrows) randomFeature);
            }
            if (randomFeature instanceof EyeColor) {
                child.setEyeColor((EyeColor) randomFeature);
            }
            if (randomFeature instanceof Eyes) {
                child.setEyes((Eyes) randomFeature);
            }
            if (randomFeature instanceof HairColor) {
                child.setHairColor((HairColor) randomFeature);
            }
            if (randomFeature instanceof HairType) {
                child.setHairType((HairType) randomFeature);
            }
            if (randomFeature instanceof Head) {
                child.setHead((Head) randomFeature);
            }
            if (randomFeature instanceof Height) {
                child.setHeight((Height) randomFeature);
            }
            if (randomFeature instanceof Mouth) {
                child.setMouth((Mouth) randomFeature);
            }
            if (randomFeature instanceof Nose) {
                child.setNose((Nose) randomFeature);
            }
            if (randomFeature instanceof SkinColor) {
                child.setSkinColor((SkinColor) randomFeature);
            }

            feature = messageSource.getMessage(randomFeature.getName().substring(0, randomFeature.getName().lastIndexOf(".")), null, loc()) + ": " + randomFeature.getTitle();
            log.append(messageSource.getMessage("turn.genModObtained", new Object[]{feature}, loc()));
        }
        // hairstyle after set hair type
        child.setHairStyle(app.getRandomHairStyle(child.getSex(), child.getHairType()));

        // picture
        child.generateView();

        // race and check newborn achievement
        child.setRace(raceService.defineRace(child));
        Achievement achievement = achievementService.checkAchievement(AchievementType.newborn, user, child);
        if (achievement != null) {
            log.append(messageSource.getMessage("turn.achievement", new Object[]{achievement.getName()}, loc()));
            logAchievements.append(child.getName()).append(": ").append(achievement.getName()).append("<br>");
        }

        // vocation and skills
        child.setCareer(new Career());
        careerService.inheritVocationAndSkills(child.getCareer(), father.getCareer(), mother.getCareer());

        logger.info("   child: " + child.getName() + ", genModFeature = " + feature + ", race: " + child.getRace().getName());
        characterRepository.save(child);

        log.append("<br>");
        return child;
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

            turnLog.append(worker.getName() + " приобретает профессию " + career.getProfession().getName() + " (" + career.getProfession().getLevel() + ") и зарабатывает " + salary + " д. " +
                    (inc == 0 ? "" : "Премия: " + inc + " д. ") +
                    (houseInc == 0 ? "" : "Надбавка за дом: " + houseInc + " д. "));

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
                turnLog.append("Раса приносит ресурсы: " + ResUtils.getResString(race, Const.RACE_RESOURCE_COEFFICIENT) + ". ");
            }
            turnLog.append("<br>");
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

    private int getAmountOfChildren(float[] percentage) {
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

    @RequestMapping(value = "/game/userview", method = RequestMethod.GET)
    public String userview(ModelMap model, RedirectAttributes redirectAttributes,
                           @RequestParam(value = "userId") Long userId,
                           @RequestParam(value = "familyId") Long familyId) {
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

        Family playerFamily = familyRepository.findByIdAndUser(familyId, player);
        if (playerFamily == null) {
            playerFamily = player.getCurrentFamily();
            redirectAttributes.addFlashAttribute("mess", "У этого игрока нет такой семьи. Показываем его текущую семью.");
        }

        model.addAttribute("playerCurrentFamily", playerFamily);

        List<Character> fathers;
        if (playerFamily.getLevel() > 0) {
            fathers = characterRepository.findByFamilyAndLevelAndSexAndSpouseIsNotNull(playerFamily, playerFamily.getLevel() - 1, "male");
        } else {
            fathers = characterRepository.findByFamilyAndLevel(playerFamily, playerFamily.getLevel());
        }

        Map<Character, List<Character>> fathersMap = new LinkedHashMap<>();
        for (Character father : fathers) {
            fathersMap.put(father, father.getChildren());
        }
        model.addAttribute("fathers", fathersMap);

        Set<Achievement> achievements = player.getAchievements();
        model.addAttribute("achievements", achievements);

        model.addAttribute("roomList", houseService.getRoomsByHouseId(playerFamily.getHouse().getId()));

        List<RoomView> roomViewList = houseService.getRoomMaps(playerFamily.getHouse(), playerFamily);
        model.addAttribute("roomViewList", roomViewList);

        List<House> buildingList = playerFamily.getBuildings();
        model.addAttribute("buildingList", buildingList);

        Map<House, List<RoomView>> buildingMap = new LinkedHashMap<>();
        for (House house : buildingList) {
            buildingMap.put(house, houseService.getRoomMaps(house, playerFamily));
        }
        model.addAttribute("buildingMap", buildingMap);

        return "game/userview";
    }
}
