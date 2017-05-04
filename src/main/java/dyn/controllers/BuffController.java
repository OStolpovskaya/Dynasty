package dyn.controllers;

/**
 * Created by OM on 17.02.2017.
 */


import dyn.model.*;
import dyn.model.Character;
import dyn.repository.BuffRepository;
import dyn.repository.CharacterRepository;
import dyn.repository.FamilyRepository;
import dyn.repository.UserRepository;
import dyn.service.Const;
import dyn.service.FamilyLogService;
import dyn.service.HouseService;
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
    HouseService houseService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private FamilyRepository familyRepository;
    @Autowired
    private CharacterRepository characterRepository;

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
        logger.error(user.getUserName() + "'s character " + characterId + " is not belongs to user's current family fiances");
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

                    familyLogService.addToLog(family, "Вы применили бафф " + messageSource.getMessage(buff.getTitle(), null, loc()) + " к персонажу " + character.getName() + ". Потрачено: " + buff.getCost() + " р.");
                    Object[] messageArguments = {character.getName(), messageSource.getMessage(buff.getTitle(), null, loc()), buff.getCost()};
                    redirectAttributes.addFlashAttribute("mess", messageSource.getMessage("game.chooseBuffs.success", messageArguments, loc()));
                    logger.debug(user.getUserName() + "'s character " + characterId + " now has buff " + buff.getTitle());
                    return "redirect:/game#char" + character.getFather().getId();
                } else {
                    redirectAttributes.addFlashAttribute("mess", messageSource.getMessage("game.chooseBuffs.notEnoughMoney", null, loc()));
                    logger.error(user.getUserName() + ": not enough money for buff " + buff.getTitle());
                    return "redirect:/game";
                }

            } else {
                redirectAttributes.addFlashAttribute("mess", messageSource.getMessage("game.chooseBuffs.characterHasThisBuffOrContradictoryToThisBuff", null, loc()));
                logger.error(user.getUserName() + "'s character " + characterId + " has this buff or contradictory to this buff");
                return "redirect:/game";
            }

        }
        redirectAttributes.addFlashAttribute("mess", messageSource.getMessage("game.chooseBuffs.characterCantChooseBuffs", null, loc()));
        logger.error(user.getUserName() + "'s character " + characterId + " is not belongs to user's current family fiances");
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
                mess = "Добавлено дерево: 10 шт.";
            } else if (projectId.equals(Const.PROJECT_RES_FOOD)) {
                family.getFamilyResources().addFood(10);
                mess = "Добавлены продукты: 10 шт.";
            } else {
                logger.error(family.logName() + "want to apply item of project with no rule: " + projectId);
                redirectAttributes.addFlashAttribute("mess", "Проект этого предмета еще не описан");
                return "redirect:/storage";
            }

            familyRepository.save(family);
            houseService.deleteItem(item);

            familyLogService.addToLog(family, mess);
            redirectAttributes.addFlashAttribute("mess", mess);
            logger.info(family.logName() + " apply item of project: '" + item.getProject());
            return "redirect:/game/storage";
        }
        logger.error(family.logName() + "want to apply nonexisting item: " + itemId);
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
                if (levelCheck && (sonCheck || wifeOfSonCheck || daughterCheck)) {

                    Long projectId = item.getProject().getId();
                    if (projectId.equals(Const.PROJECT_SKILL_INTELLIGENCE)) {
                        character.getCareer().addToIntelligence(1);
                        mess = "Интеллект повышен у персонажа: " + character.getFullName();
                    } else {
                        logger.error(family.logName() + "want to apply item of project with no rule: " + projectId);
                        redirectAttributes.addFlashAttribute("mess", "Проект этого предмета еще не описан");
                        return "redirect:/game";
                    }

                    characterRepository.save(character);
                    houseService.deleteItem(item);

                    familyLogService.addToLog(family, mess);
                    redirectAttributes.addFlashAttribute("mess", mess);
                    logger.info(family.logName() + " apply item of project: '" + item.getProject());
                    return "redirect:/game#char" + character.getFather().getId();
                } else {
                    logger.error(family.logName() + "want to apply item to not right character: " + character.getMainDetails());
                    redirectAttributes.addFlashAttribute("mess", "Условия применения не подходят к выбранному персонажу " + character.getFullName());
                    return "redirect:/game";
                }
            } else {
                logger.error(family.logName() + "want to apply item to nonexisting character: " + characterId);
                redirectAttributes.addFlashAttribute("mess", "Нет такого персонажа");
                return "redirect:/game";
            }
        }
        logger.error(family.logName() + "want to apply nonexisting item: " + itemId);
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
                    } else if (projectId.equals(Const.PROJECT_ONE_MORE_CHILD)) {
                        buff = buffRepository.findOne(Const.BUFF_ONE_MORE_CHILD);
                    } else {
                        logger.error(family.logName() + "want to apply item of project with no rule: " + projectId);
                        redirectAttributes.addFlashAttribute("mess", "Проект этого предмета еще не описан");
                        return "redirect:/game";
                    }

                    if (!character.getBuffs().contains(buff) && !character.getBuffs().contains(buff.getContradictory())) {
                        character.getBuffs().add(buff);
                        mess = "Персонажу добавлен бафф: " + buff.getTitle();
                    } else {
                        logger.error(family.logName() + "want to apply buff, but character already has this buff or has contradictory buff: " + buff.getId());
                        redirectAttributes.addFlashAttribute("mess", "Персонаж уже имеет этот или противоположный ему бафф");
                        return "redirect:/game";
                    }

                    characterRepository.save(character);
                    houseService.deleteItem(item);

                    familyLogService.addToLog(family, mess);
                    redirectAttributes.addFlashAttribute("mess", mess);
                    logger.info(family.logName() + " apply item of project: '" + item.getProject());
                    return "redirect:/game#char" + character.getFather().getId();
                } else {
                    logger.error(family.logName() + "want to apply item to not right character: " + character.getMainDetails());
                    redirectAttributes.addFlashAttribute("mess", "Условия применения не подходят к выбранному персонажу " + character.getFullName());
                    return "redirect:/game";
                }
            } else {
                logger.error(family.logName() + "want to apply item to nonexisting character: " + characterId);
                redirectAttributes.addFlashAttribute("mess", "Нет такого персонажа");
                return "redirect:/game";
            }
        }
        logger.error(family.logName() + "want to apply nonexisting item: " + itemId);
        redirectAttributes.addFlashAttribute("mess", "Нет такого предмета или он вам не принадлежит");
        return "redirect:/game";
    }
}
