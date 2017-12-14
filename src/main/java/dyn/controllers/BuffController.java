package dyn.controllers;

/**
 * Created by OM on 17.02.2017.
 */


import dyn.model.*;
import dyn.model.Character;
import dyn.model.career.Vocation;
import dyn.repository.BuffRepository;
import dyn.repository.CharacterRepository;
import dyn.repository.FamilyRepository;
import dyn.repository.UserRepository;
import dyn.service.*;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.List;

import static dyn.controllers.GameController.getAuthUser;
import static dyn.controllers.GameController.loc;

@Controller
public class BuffController {
    private static final Logger logger = LogManager.getLogger(FamilyController.class);
    @Autowired
    MessageSource messageSource;
    @Autowired
    BuffRepository buffRepository;
    @Autowired
    FamilyLogService familyLogService;
    @Autowired
    AppearanceService app;
    @Autowired
    CareerService careerService;
    @Autowired
    RaceService raceService;
    @Autowired
    HouseService houseService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private FamilyRepository familyRepository;
    @Autowired
    private CharacterRepository characterRepository;
    @Autowired
    private GameController gameController;

    // ============ CHOOSING BUFFS ============
    @RequestMapping(value = "/game/chooseBuffs", params = "characterId", method = RequestMethod.POST)
    public String chooseBuffs(ModelMap model, RedirectAttributes redirectAttributes,
                              @RequestParam(value = "characterId") long characterId) {
        User user = userRepository.findByUserName(getAuthUser().getUsername());
        Family family = user.getCurrentFamily();
        model.addAttribute("family", family);

        Character character = characterRepository.findByIdAndFamilyAndLevelAndSexAndSpouseIsNotNull(characterId, family, family.getLevel(), "male");
        if (character != null) {
            List<Buff> buffList = buffRepository.findByType(BuffType.usual);
            List<Buff> resultBuffs = new ArrayList<>();
            for (Buff buff : buffList) {
                if (!character.getBuffs().contains(buff) && !character.getBuffs().contains(buff.getContradictory())) {
                    resultBuffs.add(buff);
                }
            }
            model.addAttribute("character", character);
            model.addAttribute("buffList", resultBuffs);
            model.addAttribute("characterId", characterId);
            return "/game/chooseBuffs";
        }

        redirectAttributes.addFlashAttribute("mess", messageSource.getMessage("game.chooseBuffs.characterCantChooseBuffs", null, loc()));
        logger.error(family.familyNameAndUserName() + "'s character " + characterId + " is not belongs to user's current family fiances");
        return "redirect:/game";
    }

    @RequestMapping(value = "/game/applyBuff", params = "characterId", method = RequestMethod.POST)
    public String applyBuff(ModelMap model, RedirectAttributes redirectAttributes,
                            @RequestParam(value = "buff") Long buffId,
                            @RequestParam(value = "characterId") Long characterId) {
        User user = userRepository.findByUserName(getAuthUser().getUsername());
        Family family = user.getCurrentFamily();

        Character character = characterRepository.findByIdAndFamilyAndLevelAndSexAndSpouseIsNotNull(characterId, family, family.getLevel(), "male");
        if (character != null) {
            Buff buff = buffRepository.findOne(buffId);
            if (!character.getBuffs().contains(buff) && !character.getBuffs().contains(buff.getContradictory())) {
                if (family.getMoney() >= buff.getCost()) {
                    family.setMoney(family.getMoney() - buff.getCost());
                    familyRepository.save(family);

                    character.getBuffs().add(buff);
                    characterRepository.save(character);

                    familyLogService.addToLog(family, "Вы применили бафф " + messageSource.getMessage(buff.getTitle(), null, loc()) + " к персонажу " + character.getName() + ". Потрачено: " + buff.getCost() + " д.");
                    Object[] messageArguments = {character.getName(), messageSource.getMessage(buff.getTitle(), null, loc()), buff.getCost()};
                    redirectAttributes.addFlashAttribute("mess", messageSource.getMessage("game.chooseBuffs.success", messageArguments, loc()));
                    logger.debug(family.familyNameAndUserName() + "'s character " + characterId + " now has buff " + buff.getTitle());
                    return "redirect:/game#char" + character.getFather().getId();
                } else {
                    redirectAttributes.addFlashAttribute("mess", messageSource.getMessage("game.chooseBuffs.notEnoughMoney", null, loc()));
                    logger.error(family.familyNameAndUserName() + ": not enough money for buff " + buff.getTitle());
                    return "redirect:/game";
                }

            } else {
                redirectAttributes.addFlashAttribute("mess", messageSource.getMessage("game.chooseBuffs.characterHasThisBuffOrContradictoryToThisBuff", null, loc()));
                logger.error(family.familyNameAndUserName() + "'s character " + characterId + " has this buff or contradictory to this buff");
                return "redirect:/game";
            }

        }
        redirectAttributes.addFlashAttribute("mess", messageSource.getMessage("game.chooseBuffs.characterCantChooseBuffs", null, loc()));
        logger.error(family.familyNameAndUserName() + "'s character " + characterId + " is not belongs to user's current family fiances");
        return "redirect:/game";
    }

    @RequestMapping(value = "/game/applyItemResource", method = RequestMethod.POST)
    public String applyItemResource(ModelMap model, RedirectAttributes redirectAttributes,
                                    @RequestParam(value = "itemId") Long itemId) {
        User user = userRepository.findByUserName(getAuthUser().getUsername());
        Family family = user.getCurrentFamily();

        String mess = "";
        Item item = houseService.getItemByFamilyAndItemId(family, itemId);
        if (item != null) {
            Long projectId = item.getProject().getId();
            if (projectId.equals(Const.PROJECT_RES_WOOD)) {
                family.getFamilyResources().addWood(10);
                mess = "Добавлено: " + Const.RES_WOOD_NAME + " (10 шт.)";
            } else if (projectId.equals(Const.PROJECT_RES_FOOD)) {
                family.getFamilyResources().addFood(10);
                mess = "Добавлено: " + Const.RES_FOOD_NAME + " (10 шт.)";
            } else if (projectId.equals(Const.PROJECT_RES_METALL)) {
                family.getFamilyResources().addMetall(10);
                mess = "Добавлено: " + Const.RES_METALL_NAME + " (10 шт.)";
            } else if (projectId.equals(Const.PROJECT_RES_PLASTIC)) {
                family.getFamilyResources().addPlastic(10);
                mess = "Добавлено: " + Const.RES_PLASTIC_NAME + " (10 шт.)";
            } else if (projectId.equals(Const.PROJECT_RES_MICROELECTRONICS)) {
                family.getFamilyResources().addMicroelectronics(10);
                mess = "Добавлено: " + Const.RES_MICROELECTRONICS_NAME + " (10 шт.)";
            } else if (projectId.equals(Const.PROJECT_RES_CLOTH)) {
                family.getFamilyResources().addCloth(10);
                mess = "Добавлено: " + Const.RES_CLOTH_NAME + " (10 шт.)";
            } else if (projectId.equals(Const.PROJECT_RES_STONE)) {
                family.getFamilyResources().addStone(10);
                mess = "Добавлено: " + Const.RES_STONE_NAME + " (10 шт.)";
            } else if (projectId.equals(Const.PROJECT_RES_CHEMICAL)) {
                family.getFamilyResources().addChemical(10);
                mess = "Добавлено: " + Const.RES_CHEMICAL_NAME + " (10 шт.)";
            } else {
                logger.error(family.familyNameAndUserName() + "want to apply item of project with no rule: " + projectId);
                redirectAttributes.addFlashAttribute("mess", "Проект этого предмета еще не описан");
                return "redirect:/storage";
            }

            familyRepository.save(family);
            houseService.deleteItem(item);

            familyLogService.addToLog(family, mess);
            redirectAttributes.addFlashAttribute("mess", mess);
            logger.info(family.familyNameAndUserName() + " apply item of project: '" + item.getProject().getName());
            return "redirect:/game/storage";
        }
        logger.error(family.familyNameAndUserName() + "want to apply nonexisting item: " + itemId);
        redirectAttributes.addFlashAttribute("mess", "Нет такого предмета или он вам не принадлежит");
        return "redirect:/storage";
    }

    @RequestMapping(value = "/game/applyItemSkill", method = RequestMethod.POST)
    public String applyItemSkill(ModelMap model, RedirectAttributes redirectAttributes,
                                 @RequestParam(value = "itemId") Long itemId,
                                 @RequestParam(value = "characterId") Long characterId) {
        User user = userRepository.findByUserName(getAuthUser().getUsername());
        Family family = user.getCurrentFamily();

        String mess = "";
        Item item = houseService.getItemByFamilyAndItemId(family, itemId);
        if (item != null) {
            Character character = characterRepository.findOne(characterId);
            if (character != null) {
                boolean levelCheck = character.getLevel() == family.getLevel();
                boolean sonCheck = character.getSex().equals("male") && character.getFamily() == family;
                boolean wifeOfSonCheck = character.getSex().equals("female") && character.getFamily() != family && character.getSpouse().getFamily() == family;
                boolean daughterCheck = character.getSex().equals("female") && character.getFamily() == family && character.getSpouse() == null && !character.isFiancee();
                if ((levelCheck && (sonCheck || daughterCheck)) || (wifeOfSonCheck && (character.getSpouse().getLevel() == family.getLevel()))) {

                    Long projectId = item.getProject().getId();
                    if (projectId.equals(Const.PROJECT_SKILL_INTELLIGENCE)) {
                        character.getCareer().addToIntelligence(Const.SKILL_IMPROVEMENT_COEFFICIENT);
                        mess = "Интеллект повышен у персонажа: " + character.getFullName();
                    } else if (projectId.equals(Const.PROJECT_SKILL_CHARISMA)) {
                        character.getCareer().addToCharisma(Const.SKILL_IMPROVEMENT_COEFFICIENT);
                        mess = "Харизма повышена у персонажа: " + character.getFullName();
                    } else if (projectId.equals(Const.PROJECT_SKILL_STRENGTH)) {
                        character.getCareer().addToStrength(Const.SKILL_IMPROVEMENT_COEFFICIENT);
                        mess = "Сила повышена у персонажа: " + character.getFullName();
                    } else if (projectId.equals(Const.PROJECT_SKILL_CREATIVITY)) {
                        character.getCareer().addToCreativity(Const.SKILL_IMPROVEMENT_COEFFICIENT);
                        mess = "Творчество повышено у персонажа: " + character.getFullName();
                    } else if (projectId.equals(Const.PROJECT_SALARY_INC)) {
                        Buff buff = buffRepository.findOne(Const.BUFF_SALARY_INC);
                        if (!character.getBuffs().contains(buff) && !character.getBuffs().contains(buff.getContradictory())) {
                            character.getBuffs().add(buff);
                            mess = "Персонажу добавлен бафф: " + messageSource.getMessage(buff.getTitle(), null, loc());
                            characterRepository.save(character);
                        } else {
                            logger.error(family.familyNameAndUserName() + "want to apply buff, but character already has this buff or has contradictory buff: " + buff.getId());
                            redirectAttributes.addFlashAttribute("mess", "Персонаж уже имеет этот или противоположный ему бафф");
                            return "redirect:/game";
                        }
                    } else {
                        logger.error(family.familyNameAndUserName() + "want to apply item of project with no rule: " + projectId);
                        redirectAttributes.addFlashAttribute("mess", "Проект этого предмета еще не описан");
                        return "redirect:/game";
                    }

                    characterRepository.save(character);
                    houseService.deleteItem(item);

                    familyLogService.addToLog(family, mess);
                    redirectAttributes.addFlashAttribute("mess", mess);
                    logger.info(family.familyNameAndUserName() + " apply item of project: '" + item.getProject().getName());
                    return "redirect:/game#char" + character.getFather().getId();
                } else {
                    logger.error(family.familyNameAndUserName() + "want to apply item to not right character: " + character.getMainDetails());
                    redirectAttributes.addFlashAttribute("mess", "Условия применения не подходят к выбранному персонажу " + character.getFullName());
                    return "redirect:/game";
                }
            } else {
                logger.error(family.familyNameAndUserName() + "want to apply item to nonexisting character: " + characterId);
                redirectAttributes.addFlashAttribute("mess", "Нет такого персонажа");
                return "redirect:/game";
            }
        }
        logger.error(family.familyNameAndUserName() + "want to apply nonexisting item: " + itemId);
        redirectAttributes.addFlashAttribute("mess", "Нет такого предмета или он вам не принадлежит");
        return "redirect:/game";
    }

    @RequestMapping(value = "/game/applyItemMarried", method = RequestMethod.POST)
    public String applyItemMarried(ModelMap model, RedirectAttributes redirectAttributes,
                                   @RequestParam(value = "itemId") Long itemId,
                                   @RequestParam(value = "characterId") Long characterId) {
        User user = userRepository.findByUserName(getAuthUser().getUsername());
        Family family = user.getCurrentFamily();

        String mess = "";
        Item item = houseService.getItemByFamilyAndItemId(family, itemId);
        if (item != null) {
            Character character = characterRepository.findOne(characterId);
            if (character != null) {
                boolean levelCheck = character.getLevel() == family.getLevel();
                boolean sonCheck = character.getSex().equals("male") && character.getFamily() == family;
                boolean hasWifeCheck = character.getSpouse() != null;
                if (levelCheck && sonCheck && hasWifeCheck) {
                    Buff buff;
                    Long projectId = item.getProject().getId();
                    if (projectId.equals(Const.PROJECT_FERTILITY)) {
                        buff = buffRepository.findOne(Const.BUFF_FERTILITY);
                    } else if (projectId.equals(Const.PROJECT_GEN_MOD)) {
                        buff = buffRepository.findOne(Const.BUFF_GENETIC_MOD);
                    } else if (projectId.equals(Const.PROJECT_FATHER_DOMINANT)) {
                        buff = buffRepository.findOne(Const.BUFF_DOMINANT_FATHER);
                    } else if (projectId.equals(Const.PROJECT_MOTHER_DOMINANT)) {
                        buff = buffRepository.findOne(Const.BUFF_DOMINANT_MOTHER);
                    } else if (projectId.equals(Const.PROJECT_MORE_SONS)) {
                        buff = buffRepository.findOne(Const.BUFF_MANY_SONS);
                    } else if (projectId.equals(Const.PROJECT_MORE_DAUGHTERS)) {
                        buff = buffRepository.findOne(Const.BUFF_MANY_DAUGHTERS);
                    } else if (projectId.equals(Const.PROJECT_ONE_MORE_CHILD)) { // TODO: delete
                        buff = buffRepository.findOne(Const.BUFF_ONE_MORE_CHILD);
                    } else {
                        logger.error(family.familyNameAndUserName() + "want to apply item of project with no rule: " + projectId);
                        redirectAttributes.addFlashAttribute("mess", "Проект этого предмета еще не описан");
                        return "redirect:/game";
                    }

                    if (!character.getBuffs().contains(buff) && !character.getBuffs().contains(buff.getContradictory())) {
                        character.getBuffs().add(buff);
                        mess = "Персонажу " + character.getName() + " добавлен бафф: " + messageSource.getMessage(buff.getTitle(), null, loc());
                    } else {
                        logger.error(family.familyNameAndUserName() + "want to apply buff, but character already has this buff or has contradictory buff: " + buff.getId());
                        redirectAttributes.addFlashAttribute("mess", "Персонаж уже имеет этот или противоположный ему бафф");
                        return "redirect:/game";
                    }

                    characterRepository.save(character);
                    houseService.deleteItem(item);

                    familyLogService.addToLog(family, mess);
                    redirectAttributes.addFlashAttribute("mess", mess);
                    logger.info(family.familyNameAndUserName() + " apply item of project: '" + item.getProject().getName());
                    return "redirect:/game#char" + character.getFather().getId();
                } else {
                    logger.error(family.familyNameAndUserName() + "want to apply item to not right character: " + character.getMainDetails());
                    redirectAttributes.addFlashAttribute("mess", "Условия применения не подходят к выбранному персонажу " + character.getFullName());
                    return "redirect:/game";
                }
            } else {
                logger.error(family.familyNameAndUserName() + "want to apply item to nonexisting character: " + characterId);
                redirectAttributes.addFlashAttribute("mess", "Нет такого персонажа");
                return "redirect:/game";
            }
        }
        logger.error(family.familyNameAndUserName() + "want to apply nonexisting item: " + itemId);
        redirectAttributes.addFlashAttribute("mess", "Нет такого предмета или он вам не принадлежит");
        return "redirect:/game";
    }

    @RequestMapping(value = "/game/applyItemParents", method = RequestMethod.POST)
    public String applyItemParents(ModelMap model, RedirectAttributes redirectAttributes,
                                   @RequestParam(value = "itemId") Long itemId,
                                   @RequestParam(value = "characterId") Long characterId) {
        User user = userRepository.findByUserName(getAuthUser().getUsername());
        Family family = user.getCurrentFamily();

        String mess = "";
        Item item = houseService.getItemByFamilyAndItemId(family, itemId);
        if (item != null) {
            Character character = characterRepository.findOne(characterId);
            if (character != null) {
                boolean levelCheck;
                boolean familyCheck;
                boolean hasChildren;
                boolean canHaveChild;

                if (character.getSex().equals("male")) {
                    int size = character.getChildren().size();
                    hasChildren = size > 0;
                    familyCheck = character.getFamily() == family;
                    levelCheck = character.getLevel() == family.getLevel() - 1;
                    canHaveChild = size < Const.MAX_CHILDREN;
                } else {
                    int size = character.getSpouse().getChildren().size();
                    hasChildren = size > 0;
                    familyCheck = character.getSpouse().getFamily() == family;
                    levelCheck = character.getSpouse().getLevel() == family.getLevel() - 1;
                    canHaveChild = size < Const.MAX_CHILDREN;
                }
                if (!canHaveChild) {
                    logger.error(family.familyNameAndUserName() + " want to apply parents buff, but reached max children: " + item.getProject().getId());
                    redirectAttributes.addFlashAttribute("mess", "Персонаж не может иметь больше детей. Максимум: " + Const.MAX_CHILDREN);
                    return "redirect:/game";
                }

                if (levelCheck && familyCheck && hasChildren) {
                    Long projectId = item.getProject().getId();
                    if (projectId.equals(Const.PROJECT_CLONE)) {
                        String characterSex = character.getSex();

                        Character clone = new Character();
                        clone.setSex(characterSex);

                        if (characterSex.equals("male")) {
                            clone.setName(characterRepository.getRandomNameMale());
                            clone.setFather(character);
                        } else {
                            clone.setName(characterRepository.getRandomNameFemale());
                            clone.setFather(character.getSpouse());
                        }
                        clone.setRace(character.getRace());
                        clone.setLevel(character.getLevel() + 1);

                        clone.setCareer(careerService.copyCareer(character.getCareer()));

                        clone.setBody(character.getBody());
                        clone.setEars(character.getEars());
                        clone.setEyebrows(character.getEyebrows());
                        clone.setEyeColor(character.getEyeColor());
                        clone.setEyes(character.getEyes());
                        clone.setHairColor(character.getHairColor());
                        clone.setHairType(character.getHairType());
                        clone.setHairStyle(app.getRandomHairStyle(clone.getSex(), clone.getHairType()));
                        clone.setHead(character.getHead());
                        clone.setHeight(character.getHeight());
                        clone.setMouth(character.getMouth());
                        clone.setNose(character.getNose());
                        clone.setSkinColor(character.getSkinColor());

                        clone.setFamily(family);
                        characterRepository.save(clone);
                        mess = "Вы клонировали персонажа " + character.getName() + ". Новый персонаж: " + clone.getName();
                    } else if (projectId.equals(Const.PROJECT_ADOPTED_CHILD)) {
                        Character adoptedChild = new Character();

                        if (character.getSex().equals("male")) {
                            adoptedChild.setFather(character);
                        } else {
                            adoptedChild.setFather(character.getSpouse());
                        }

                        if (Math.random() < 0.5) {
                            adoptedChild.setSex("male");
                            adoptedChild.setName(characterRepository.getRandomNameMale());
                        } else {
                            adoptedChild.setSex("female");
                            adoptedChild.setName(characterRepository.getRandomNameFemale());
                        }

                        adoptedChild.setLevel(character.getLevel() + 1);

                        adoptedChild.setCareer(careerService.generateRandomCareer());

                        adoptedChild.setBody(app.getRandomBody(app.ALL));
                        adoptedChild.setEars(app.getRandomEars(app.ALL));
                        adoptedChild.setEyebrows(app.getRandomEyeBrows(app.ALL));
                        adoptedChild.setEyeColor(app.getRandomEyeColor(app.ALL));
                        adoptedChild.setEyes(app.getRandomEyes(app.ALL));
                        adoptedChild.setHairColor(app.getRandomHairColor(app.ALL));
                        adoptedChild.setHairType(app.getRandomHairType(app.ALL));
                        adoptedChild.setHairStyle(app.getRandomHairStyle(adoptedChild.getSex(), adoptedChild.getHairType()));
                        adoptedChild.setHead(app.getRandomHead(app.ALL));
                        adoptedChild.setHeight(app.getRandomHeight(app.ALL));
                        adoptedChild.setMouth(app.getRandomMouth(app.ALL));
                        adoptedChild.setNose(app.getRandomNose(app.ALL));
                        adoptedChild.setSkinColor(app.getRandomSkinColor(app.ALL));

                        Race race = raceService.defineRace(adoptedChild);
                        adoptedChild.setRace(race);

                        adoptedChild.setFamily(family);
                        characterRepository.save(adoptedChild);
                        mess = "Вы приняли в семью приемного ребенка: " + adoptedChild.getName();

                    } else if (projectId.equals(Const.PROJECT_ONE_MORE_CHILD)) {
                        Character father;
                        Character mother;
                        if (character.getSex().equals("male")) {
                            father = character;
                            mother = character.getSpouse();
                        } else {
                            father = character.getSpouse();
                            mother = character;
                        }
                        boolean firstTurn = false;

                        double fatherFeaturePercent = 0.5; // whose feature is inherited, father or mother
                        if (father.isBuffedBy(Buff.DOMINANT_MOTHER)) {
                            fatherFeaturePercent = 0.2;
                        }
                        if (father.isBuffedBy(Buff.DOMINANT_FATHER)) {
                            fatherFeaturePercent = 0.8;
                        }

                        double sonOrDaughterPercent = 0.5; // male or female child
                        if (father.isBuffedBy(Buff.MANY_SONS)) {
                            sonOrDaughterPercent = 0.8;
                        }
                        if (father.isBuffedBy(Buff.MANY_DAUGHTERS)) {
                            sonOrDaughterPercent = 0.2;
                        }

                        double genModPercent = Const.PERCENTAGE_GEN_MOD;// 0.05; // percentage of genetic modification
                        if (father.isBuffedBy(Buff.GENETIC_MOD)) {
                            genModPercent = Const.PERCENTAGE_GEN_MOD_BUFFED;//0.40;
                        }

                        StringBuilder log = new StringBuilder("У вас появился еще один ребенок: ");
                        StringBuilder logAchievements = new StringBuilder();
                        Character child = gameController.generateChild(user, family, father, mother, firstTurn, fatherFeaturePercent, sonOrDaughterPercent, genModPercent, 1, log, logAchievements);

                        child.setLevel(family.getLevel());
                        characterRepository.save(child);

                        mess = log.toString();
                    } else {
                        logger.error(family.familyNameAndUserName() + "want to apply item of project with no rule: " + projectId);
                        redirectAttributes.addFlashAttribute("mess", "Проект этого предмета еще не описан");
                        return "redirect:/game";
                    }

                    houseService.deleteItem(item);

                    familyLogService.addToLog(family, mess);
                    redirectAttributes.addFlashAttribute("mess", mess);
                    logger.info(family.familyNameAndUserName() + " apply item of project: '" + item.getProject().getName());
                    return "redirect:/game#char" + (character.getSex() == "male" ? character.getId() : character.getSpouse().getId());
                } else {
                    logger.error(family.familyNameAndUserName() + "want to apply item to not right character: " + character.getMainDetails() + ". Условия: " + levelCheck + "," + familyCheck + "," + hasChildren);
                    redirectAttributes.addFlashAttribute("mess", "Условия применения не подходят к выбранному персонажу " + character.getFullName());
                    return "redirect:/game";
                }
            } else {
                logger.error(family.familyNameAndUserName() + "want to apply item to nonexisting character: " + characterId);
                redirectAttributes.addFlashAttribute("mess", "Нет такого персонажа");
                return "redirect:/game";
            }
        }
        logger.error(family.familyNameAndUserName() + "want to apply nonexisting item: " + itemId);
        redirectAttributes.addFlashAttribute("mess", "Нет такого предмета или он вам не принадлежит");
        return "redirect:/game";
    }

    @RequestMapping(value = "/game/applyItemFamily", method = RequestMethod.POST)
    public String applyItemFamily(ModelMap model, RedirectAttributes redirectAttributes,
                                  @RequestParam(value = "itemId") Long itemId) {
        User user = userRepository.findByUserName(getAuthUser().getUsername());
        Family family = user.getCurrentFamily();

        String mess = "";
        Item item = houseService.getItemByFamilyAndItemId(family, itemId);
        if (item != null) {
            Long projectId = item.getProject().getId();
            if (projectId.equals(Const.PROJECT_PAIR_ONE_MORE)) {
                family.setPairsNum(family.getPairsNum() - 1);
                mess = "Теперь вы можете создать еще одну пару";
            } else if (projectId.equals(Const.PROJECT_BRIDE_ONE_MORE)) {
                family.setFianceeNum(family.getFianceeNum() - 1);
                mess = "Теперь вы можете опубликовать еще одну анкету невесты";
            } else {
                logger.error(family.familyNameAndUserName() + "want to apply item of project with no rule: " + projectId);
                redirectAttributes.addFlashAttribute("mess", "Проект этого предмета еще не описан");
                return "redirect:/game";
            }

            familyRepository.save(family);
            houseService.deleteItem(item);

            familyLogService.addToLog(family, mess);
            redirectAttributes.addFlashAttribute("mess", mess);
            logger.info(family.familyNameAndUserName() + " apply item of project: '" + item.getProject().getName());
            return "redirect:/game";
        }
        logger.error(family.familyNameAndUserName() + "want to apply nonexisting item: " + itemId);
        redirectAttributes.addFlashAttribute("mess", "Нет такого предмета или он вам не принадлежит");
        return "redirect:/game";
    }

    @RequestMapping(value = "/game/applyItemChangeSomething", method = RequestMethod.POST)
    public String applyItemChangeSomething(ModelMap model, RedirectAttributes redirectAttributes,
                                           @RequestParam(value = "itemId") Long itemId,
                                           @RequestParam(value = "characterId") Long characterId,
                                           @RequestParam(value = "param") String param) {
        User user = userRepository.findByUserName(getAuthUser().getUsername());
        Family family = user.getCurrentFamily();

        String mess = "";
        Item item = houseService.getItemByFamilyAndItemId(family, itemId);
        if (item != null) {
            Character character = characterRepository.findOne(characterId);
            if (character != null) {
                boolean levelCheck = character.getLevel() == family.getLevel();
                boolean sonCheck = character.getSex().equals("male") && character.getFamily() == family;
                boolean wifeOfSonCheck = character.getSex().equals("female") && character.getFamily() != family && character.getSpouse().getFamily() == family;
                boolean daughterCheck = character.getSex().equals("female") && character.getFamily() == family && character.getSpouse() == null && !character.isFiancee();
                if ((levelCheck && (sonCheck || daughterCheck)) || (wifeOfSonCheck && (character.getSpouse().getLevel() == family.getLevel()))) {
                    Long projectId = item.getProject().getId();
                    if (projectId.equals(Const.PROJECT_VOCATION_CHANGE)) {
                        Vocation vocation = careerService.getVocation(Long.parseLong(param));
                        if (vocation != null) {
                            character.getCareer().setVocation(vocation);
                            characterRepository.save(character);
                            mess = "Призвание персонажа изменено: " + vocation.getName();
                        } else {
                            logger.error(family.familyNameAndUserName() + "want to change vocation to nonexisting vocation: " + param);
                            redirectAttributes.addFlashAttribute("mess", "Такого призвания нет");
                            return "redirect:/game";
                        }
                    } else if (projectId.equals(Const.PROJECT_BODY_PART_CHANGE)) {
                        String part = "";
                        switch (param) {
                            case "body":
                                character.setBody(app.getRandomBody(AppearanceService.USUAL));
                                part = "тело";
                                break;
                            case "ears":
                                character.setEars(app.getRandomEars(AppearanceService.USUAL));
                                part = "уши";
                                break;
                            case "eyebrows":
                                character.setEyebrows(app.getRandomEyeBrows(AppearanceService.USUAL));
                                part = "брови";
                                break;
                            case "eyeColor":
                                character.setEyeColor(app.getRandomEyeColor(AppearanceService.USUAL));
                                part = "цвет глаз";
                                break;
                            case "eyes":
                                character.setEyes(app.getRandomEyes(AppearanceService.USUAL));
                                part = "глаза";
                                break;
                            case "hairColor":
                                character.setHairColor(app.getRandomHairColor(AppearanceService.USUAL));
                                part = "цвет волос";
                                break;
                            case "hairType":
                                character.setHairType(app.getRandomHairType(AppearanceService.USUAL));
                                part = "тип волос";
                                break;
                            case "head":
                                character.setHead(app.getRandomHead(AppearanceService.USUAL));
                                part = "голова";
                                break;
                            case "height":
                                character.setHeight(app.getRandomHeight(AppearanceService.USUAL));
                                part = "рост";
                                break;
                            case "mouth":
                                character.setMouth(app.getRandomMouth(AppearanceService.USUAL));
                                part = "рот";
                                break;
                            case "nose":
                                character.setNose(app.getRandomNose(AppearanceService.USUAL));
                                part = "нос";
                                break;
                            case "skinColor":
                                character.setSkinColor(app.getRandomSkinColor(AppearanceService.USUAL));
                                part = "цвет кожи";
                                break;
                        }
                        Race race = raceService.defineRace(character);
                        character.setRace(race);
                        characterRepository.save(character);
                        mess = "У персонажа " + character.getName() + " проведена пластическая операция: " + part;
                    } else {
                        logger.error(family.familyNameAndUserName() + "want to apply item of project with no rule: " + projectId);
                        redirectAttributes.addFlashAttribute("mess", "Проект этого предмета еще не описан");
                        return "redirect:/game";
                    }

                    houseService.deleteItem(item);

                    familyLogService.addToLog(family, mess);
                    redirectAttributes.addFlashAttribute("mess", mess);
                    logger.info(family.familyNameAndUserName() + " apply item of project: '" + item.getProject().getName());
                    return "redirect:/game#char" + character.getFather().getId();
                } else {
                    logger.error(family.familyNameAndUserName() + "want to apply item to not right character: " + character.getMainDetails());
                    redirectAttributes.addFlashAttribute("mess", "Условия применения не подходят к выбранному персонажу " + character.getFullName());
                    return "redirect:/game";
                }
            } else {
                logger.error(family.familyNameAndUserName() + "want to apply item to nonexisting character: " + characterId);
                redirectAttributes.addFlashAttribute("mess", "Нет такого персонажа");
                return "redirect:/game";
            }
        }
        logger.error(family.familyNameAndUserName() + "want to apply nonexisting item: " + itemId);
        redirectAttributes.addFlashAttribute("mess", "Нет такого предмета или он вам не принадлежит");
        return "redirect:/game";
    }
}
