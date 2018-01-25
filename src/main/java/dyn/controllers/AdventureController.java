package dyn.controllers;

import dyn.model.Character;
import dyn.model.*;
import dyn.model.adventure.*;
import dyn.repository.CharacterRepository;
import dyn.repository.FamilyRepository;
import dyn.repository.UserRepository;
import dyn.service.*;
import dyn.utils.ResUtils;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static dyn.controllers.GameController.getAuthUser;
import static dyn.controllers.GameController.loc;

/**
 * Created by OM on 30.08.2017.
 */
@Controller
public class AdventureController {
    private static final Logger logger = LogManager.getLogger(AdventureController.class);
    @Autowired
    MessageSource messageSource;
    @Autowired
    CraftService craftService;
    @Autowired
    AppearanceService appService;
    @Autowired
    RaceService raceService;
    @Autowired
    CareerService careerService;
    @Autowired
    HouseService houseService;
    @Autowired
    CharacterService characterService;
    @Autowired
    FamilyLogService familyLogService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private AdventureService adventureService;
    @Autowired
    private FamilyRepository familyRepository;
    @Autowired
    private CharacterRepository characterRepository;

    @RequestMapping("/game/adventures")
    public String adventures(ModelMap model, RedirectAttributes redirectAttributes) {
        User user = userRepository.findByUserName(getAuthUser().getUsername());

        Family family = user.getCurrentFamily();
        if (family == null) {
            logger.debug(user.getUserName() + " doesn't have any family");
            redirectAttributes.addFlashAttribute("mess", messageSource.getMessage("new.user", null, loc()));
            return "redirect:/game/addNewFamily";
        }
        model.addAttribute("family", family);

        List<Adventure> adventuresCreatedBy = adventureService.getAdventureCreatedBy(family);
        model.addAttribute("adventuresCreatedBy", adventuresCreatedBy);

        FamilyAdventure currentAdventure = adventureService.getCurrentAdventure(family);
        model.addAttribute("currentAdventure", currentAdventure);

        List<Item> itemsInStorage = houseService.getItemsInStorage(family);
        model.addAttribute("itemsInStorage", itemsInStorage);

        List<Character> availableCharacters = new ArrayList<>();
        List<dyn.model.Character> charactersOnLevel = characterRepository.findByFamilyAndLevel(family, family.getLevel());
        for (Character character : charactersOnLevel) {
            if (character.getSex().equals("male")) {
                availableCharacters.add(character);
                if (character.getSpouse() != null) {
                    availableCharacters.add(character.getSpouse());
                }
            } else {
                if (character.getSpouse() == null && !character.isFiancee()) {
                    availableCharacters.add(character);
                }
            }
        }
        model.addAttribute("availableCharacters", availableCharacters);

        return "game/adventures";
    }

    @RequestMapping(value = "/game/startAdventure", method = RequestMethod.POST)
    public String startAdventure(ModelMap model, RedirectAttributes redirectAttributes) {
        User user = userRepository.findByUserName(getAuthUser().getUsername());
        Family family = user.getCurrentFamily();

        List<FamilyAdventure> familyAdventures = adventureService.getFamilyAdventures(family);
        if (familyAdventures.isEmpty() && family.getLevel() >= 10 && family.getMoney() >= Const.ADVENTURE_COST * family.getLevel()) {
            family.removeMoney(Const.ADVENTURE_COST * family.getLevel());
            familyRepository.save(family);
            adventureService.createFamilyAdventures(family);
        }

        return "redirect:/game/adventures";
    }

    @RequestMapping(value = "/game/answerAdventure", method = RequestMethod.POST)
    public String answerAdventure(ModelMap model, RedirectAttributes redirectAttributes,
                                  @RequestParam(value = "things", defaultValue = "false") boolean thingsCheckbox,
                                  @RequestParam(value = "char", defaultValue = "false") boolean charCheckbox,
                                  @RequestParam(value = "item1", defaultValue = "") Long item1,
                                  @RequestParam(value = "item2", defaultValue = "") Long item2,
                                  @RequestParam(value = "item3", defaultValue = "") Long item3,
                                  @RequestParam(value = "character", defaultValue = "") Long characterId) {
        if (!thingsCheckbox && !charCheckbox) {
            redirectAttributes.addFlashAttribute("mess", "Необходимо выбрать какой-нибудь предмет или персонажа.");
            return "redirect:/game/adventures";
        }

        if (thingsCheckbox && (item1 == null && item2 == null && item3 == null)) {
            redirectAttributes.addFlashAttribute("mess", "Если вы хотите предложить какой-нибудь предмет, то его надо выбрать.");
            return "redirect:/game/adventures";
        }

        if (charCheckbox && characterId == null) {
            redirectAttributes.addFlashAttribute("mess", "Если вы хотите отправить на задание какого-нибудь персонажа, то его надо выбрать.");
            return "redirect:/game/adventures";
        }

        User user = userRepository.findByUserName(getAuthUser().getUsername());
        Family family = user.getCurrentFamily();

        List<FamilyAdventure> familyAdventures = adventureService.getFamilyAdventures(family);
        FamilyAdventure currentAdventure = null;// = adventureService.getCurrentAdventure(family);

        for (FamilyAdventure familyAdventure : familyAdventures) {
            if (familyAdventure.isCurrent()) {
                currentAdventure = familyAdventure;
                break;
            }
        }

        List<Item> items = new ArrayList<>();
        if (item1 != null) {
            items.add(houseService.getItemOfFamilyInPlace(item1, family, ItemPlace.storage));
        }
        if (item2 != null) {
            items.add(houseService.getItemOfFamilyInPlace(item2, family, ItemPlace.storage));
        }
        if (item3 != null) {
            items.add(houseService.getItemOfFamilyInPlace(item3, family, ItemPlace.storage));
        }
        Character character = null;
        if (characterId != null) {
            Character formCharacter = characterRepository.findOne(characterId);
            if (formCharacter.getSex().equals("male")) {
                if (formCharacter.getFamily() == family && formCharacter.getLevel() == family.getLevel()) {
                    character = formCharacter;
                }
            } else {
                if (formCharacter.getFamily() == family) {
                    if (formCharacter.getSpouse() == null && !formCharacter.isFiancee() && formCharacter.getLevel() == family.getLevel()) {
                        character = formCharacter;
                    }
                } else {
                    if (formCharacter.getSpouse() != null && formCharacter.getSpouse().getFamily() == family && formCharacter.getSpouse().getLevel() == family.getLevel()) {
                        character = formCharacter;
                    }
                }
            }
        }


        Adventure adventure = currentAdventure.getAdventure();
        StringBuilder adventureLog = new StringBuilder();
        boolean adventureCompleted = checkAnswer(family, adventure, items, character, adventureLog);
        if (adventureCompleted) {
            awardFamilyForAdventure(family, adventure, adventureLog);

            family.incAdventuresCompleted();
            familyRepository.save(family);

            currentAdventure.setStatus(FamilyAdventureStatus.finished);
            adventureService.saveFamilyAdventure(currentAdventure);

            StringBuilder mess = new StringBuilder("Вы прошли квест " + adventure.fullTitle() + "!");
            mess.append(adventureLog);

            if (adventure.getCreatedBy().getId() > 1) {
                Family author = adventure.getCreatedBy();
                author.addMoney(Const.ADVENTURE_AUTHOR_AWARD);
                familyLogService.addToLog(author, "Ваш квест " + adventure.getTitle() + " пройден семьей " + family.link() + ". Получено: " + Const.ADVENTURE_AUTHOR_AWARD + " д.");
            }
            adventure.incCompletedTimes();
            adventureService.saveAdventure(adventure);

            logger.info(family.userNameAndFamilyName() + " completed the adventure: " + adventure.getTitle());

            // запрос adventureService.saveFamilyAdventure(currentAdventure); был не видим для adventureService.checkIfAllAdventuresCompleted(family);
            boolean allAdventuresCompleted = true;//adventureService.checkIfAllAdventuresCompleted(family);
            for (FamilyAdventure familyAdventure : familyAdventures) {
                if (familyAdventure.getStatus().equals(FamilyAdventureStatus.inprogress)) {
                    allAdventuresCompleted = false;
                    break;
                }
            }
            if (allAdventuresCompleted) {
                Project randomUniqueProject = craftService.getRandomUniqueProject();
                Item item = craftService.giveGift(family, randomUniqueProject.getId());
                item.setQuality(Const.ITEM_MAX_QUALITY);
                houseService.saveItem(item);
                /*
                // todo: добавить дарение проекта?
                if (!family.getCraftProjects().contains(randomUniqueProject)) {
                    family.getCraftProjects().add(randomUniqueProject);
                }
                */
                family.incGotUniqueItems();
                familyRepository.save(family);

                mess.append(" И завершили свое приключение. Подарок: " + item.getTitle());
                logger.info(family.userNameAndFamilyName() + " completed ALL adventures. Got gift: " + item.getTitle());
            }
            familyLogService.addToLog(family, mess.toString());
            redirectAttributes.addFlashAttribute("mess", mess.toString());
        } else {
            redirectAttributes.addFlashAttribute("mess", adventure.getTextFailed());
        }

        return "redirect:/game/adventures";
    }

    private boolean checkAnswer(Family family, Adventure adventure, List<Item> items, Character character, StringBuilder adventureLog) {
        System.out.println("checkAnswer adventure.getAnswerType() = " + adventure.getAnswerType());
        switch (adventure.getAnswerType()) {
            case oneItem:
                if (itemsMatchOr(adventure, items, adventureLog)) {
                    return true;
                }
                break;
            case manyItemsAnd:
                if (itemsMatchAnd(adventure, items, adventureLog)) {
                    return true;
                }
                break;
            case manyItemsOr:
                if (itemsMatchOr(adventure, items, adventureLog)) {
                    return true;
                }
                break;
            case character:
                if (characterMatches(character, adventure, family, adventureLog)) {
                    return true;
                }
                break;
            case characterAndOneItem:
                if (itemsMatchOr(adventure, items, adventureLog) && characterMatches(character, adventure, family, adventureLog)) {
                    return true;
                }
                break;
            case characterAndManyItemsAnd:
                if (itemsMatchAnd(adventure, items, adventureLog) && characterMatches(character, adventure, family, adventureLog)) {
                    return true;
                }
                break;
            case characterAndManyItemsOr:
                if (itemsMatchOr(adventure, items, adventureLog) && characterMatches(character, adventure, family, adventureLog)) {
                    return true;
                }
                break;
            case characterOrOneItem:
                if (itemsMatchOr(adventure, items, adventureLog) || characterMatches(character, adventure, family, adventureLog)) {
                    return true;
                }
                break;
            case characterOrManyItemsAnd:
                if (itemsMatchAnd(adventure, items, adventureLog) || characterMatches(character, adventure, family, adventureLog)) {
                    return true;
                }
                break;
            case characterOrManyItemsOr:
                if (itemsMatchOr(adventure, items, adventureLog) || characterMatches(character, adventure, family, adventureLog)) {
                    return true;
                }
                break;
        }
        return false;
    }

    private boolean characterMatches(Character character, Adventure adventure, Family family, StringBuilder adventureLog) {
        System.out.println("characterMatches ");
        if (character != null) {
            if (!adventure.getCharSex().isEmpty() && !adventure.getCharSex().equals(character.getSex())) {
                return false;
            }
            if (adventure.getCharIntelligence() != null) {
                switch (adventure.getCharIntelligence()) {
                    case gt30:
                        if (character.getCareer().getIntelligence() < 30) {
                            return false;
                        }
                        break;
                    case gt60:
                        if (character.getCareer().getIntelligence() < 60) {
                            return false;
                        }
                        break;
                    case gt90:
                        if (character.getCareer().getIntelligence() < 90) {
                            return false;
                        }
                        break;
                }
            }
            if (adventure.getCharCharisma() != null) {
                switch (adventure.getCharCharisma()) {
                    case gt30:
                        if (character.getCareer().getCharisma() < 30) {
                            return false;
                        }
                        break;
                    case gt60:
                        if (character.getCareer().getCharisma() < 60) {
                            return false;
                        }
                        break;
                    case gt90:
                        if (character.getCareer().getCharisma() < 90) {
                            return false;
                        }
                        break;
                }
            }
            if (adventure.getCharStrength() != null) {
                switch (adventure.getCharStrength()) {
                    case gt30:
                        if (character.getCareer().getStrength() < 30) {
                            return false;
                        }
                        break;
                    case gt60:
                        if (character.getCareer().getStrength() < 60) {
                            return false;
                        }
                        break;
                    case gt90:
                        if (character.getCareer().getStrength() < 90) {
                            return false;
                        }
                        break;
                }
            }
            if (adventure.getCharCreativity() != null) {
                switch (adventure.getCharCreativity()) {
                    case gt30:
                        if (character.getCareer().getCreativity() < 30) {
                            return false;
                        }
                        break;
                    case gt60:
                        if (character.getCareer().getCreativity() < 60) {
                            return false;
                        }
                        break;
                    case gt90:
                        if (character.getCareer().getCreativity() < 90) {
                            return false;
                        }
                        break;
                }
            }
            if (adventure.getCharRace() != null && !character.getRace().equals(adventure.getCharRace())) {
                return false;
            }
            if (adventure.getCharVocation() != null && !character.getCareer().getVocation().equals(adventure.getCharVocation())) {
                return false;
            }
            if (adventure.getCharAppBody() != null && !character.getBody().equals(adventure.getCharAppBody())) {
                return false;
            }
            if (adventure.getCharAppEars() != null && !character.getEars().equals(adventure.getCharAppEars())) {
                return false;
            }
            if (adventure.getCharAppEyebrows() != null && !character.getEyebrows().equals(adventure.getCharAppEyebrows())) {
                return false;
            }
            if (adventure.getCharAppEyes() != null && !character.getEyes().equals(adventure.getCharAppEyes())) {
                return false;
            }
            if (adventure.getCharAppEyeColor() != null && !character.getEyeColor().equals(adventure.getCharAppEyeColor())) {
                return false;
            }
            if (adventure.getCharAppHairColor() != null && !character.getHairColor().equals(adventure.getCharAppHairColor())) {
                return false;
            }
            if (adventure.getCharAppHairType() != null && !character.getHairType().equals(adventure.getCharAppHairType())) {
                return false;
            }
            if (adventure.getCharAppHead() != null && !character.getHead().equals(adventure.getCharAppHead())) {
                return false;
            }
            if (adventure.getCharAppHeight() != null && !character.getHeight().equals(adventure.getCharAppHeight())) {
                return false;
            }
            if (adventure.getCharAppMouth() != null && !character.getMouth().equals(adventure.getCharAppMouth())) {
                return false;
            }
            if (adventure.getCharAppNose() != null && !character.getNose().equals(adventure.getCharAppNose())) {
                return false;
            }
            if (adventure.getCharAppSkinColor() != null && !character.getSkinColor().equals(adventure.getCharAppSkinColor())) {
                return false;
            }
            applyActionChar(family, adventure, character, adventureLog);
            return true;
        }
        return false;
    }

    private void applyActionChar(Family family, Adventure adventure, Character character, StringBuilder adventureLog) {
        if (adventure.getActionChar() != null) {
            switch (adventure.getActionChar()) {
                case kill:
                    adventureLog.append(" Персонаж удален из семьи: ").append(character.getFullName()).append(".");
                    if (character.getSex().equals("male")) {
                        if (character.getSpouse() != null) {
                            adventureLog.append(" Его жена перемещена к вам в семью: ").append(character.getSpouse().getFullName()).append(".");
                            Character wife = character.getSpouse();
                            wife.setSpouse(null);
                            wife.setLevel(character.getLevel());
                            wife.setFather(character.getFather());
                            wife.setFamily(family);
                            characterRepository.save(wife);
                        }
                    } else {
                        if (character.getSpouse() != null) {
                            Character husband = character.getSpouse();
                            husband.setSpouse(null);
                            characterRepository.save(husband);
                            adventureLog.append(" Ее муж снова свободен: ").append(character.getSpouse().getFullName()).append(".");
                        }
                    }
                    characterRepository.delete(character);
                    return;
                case clone:
                    Character cloneCharacter = characterService.cloneCharacter(family, character);
                    characterRepository.save(cloneCharacter);
                    if (character.getSex().equals("female") && character.getFamily() != family) { // жена сына
                        cloneCharacter.setLevel(family.getLevel());
                        cloneCharacter.setFather(character.getSpouse().getFather());
                        characterRepository.save(cloneCharacter);
                    }
                    adventureLog.append(" Персонаж клонирован, новый персонаж: ").append(cloneCharacter.getFullName()).append(".");
                    break;
                case changeSex:
                    adventureLog.append(" Персонаж ").append(character.getName()).append(" сменил пол, теперь это ");
                    if (character.getSex().equals("male")) {
                        character.setSex("female");
                        characterService.generateName(character);
                        characterService.generateHairStyle(character);
                        adventureLog.append(character.getFullName()).append(".");
                        if (character.getSpouse() != null) {
                            adventureLog.append(" Его жена перемещена к вам в семью: ").append(character.getSpouse().getFullName()).append(".");
                            Character wife = character.getSpouse();
                            wife.setSpouse(null);
                            wife.setLevel(character.getLevel());
                            wife.setFather(character.getFather());
                            wife.setFamily(family);
                            characterRepository.save(wife);

                            character.setSpouse(null);
                            characterRepository.save(character);
                        }
                    } else {
                        character.setSex("male");
                        characterService.generateName(character);
                        characterService.generateHairStyle(character);
                        adventureLog.append(character.getFullName()).append(".");
                        if (character.getSpouse() != null) {
                            adventureLog.append(" Она перенесена в вашу семью, а ее муж снова свободен: ").append(character.getSpouse().getFullName()).append(".");
                            Character husband = character.getSpouse();
                            husband.setSpouse(null);
                            characterRepository.save(husband);

                            character.setLevel(family.getLevel());
                            character.setFather(character.getSpouse().getFather());
                            character.setFamily(family);
                            character.setSpouse(null);
                            characterRepository.save(character);
                        }
                    }
                    break;
            }
        }
        StringBuilder actionCharLog = new StringBuilder();
        if (adventure.getActionCharIntelligence() != null) {
            switch (adventure.getActionCharIntelligence()) {
                case inc5:
                    actionCharLog.append(" Интеллект +5.");
                    character.getCareer().incIntelligence(5);
                    break;
                case inc10:
                    actionCharLog.append(" Интеллект +10.");
                    character.getCareer().incIntelligence(10);
                    break;
                case inc25:
                    actionCharLog.append(" Интеллект +25.");
                    character.getCareer().incIntelligence(25);
                    break;
                case dec5:
                    actionCharLog.append(" Интеллект -5.");
                    character.getCareer().decIntelligence(5);
                    break;
                case dec10:
                    actionCharLog.append(" Интеллект -10.");
                    character.getCareer().decIntelligence(10);
                    break;
                case dec25:
                    actionCharLog.append(" Интеллект -25.");
                    character.getCareer().decIntelligence(25);
                    break;
                case set0:
                    actionCharLog.append(" Интеллект понижен до минимума: 0.");
                    character.getCareer().setIntelligence(0);
                    break;
            }
            characterRepository.save(character);
        }
        if (adventure.getActionCharCharisma() != null) {
            switch (adventure.getActionCharCharisma()) {
                case inc5:
                    actionCharLog.append(" Харизма +5.");
                    character.getCareer().incCharisma(5);
                    break;
                case inc10:
                    actionCharLog.append(" Харизма +10.");
                    character.getCareer().incCharisma(10);
                    break;
                case inc25:
                    actionCharLog.append(" Харизма +25.");
                    character.getCareer().incCharisma(25);
                    break;
                case dec5:
                    actionCharLog.append(" Харизма -5.");
                    character.getCareer().decCharisma(5);
                    break;
                case dec10:
                    actionCharLog.append(" Харизма -10.");
                    character.getCareer().decCharisma(10);
                    break;
                case dec25:
                    actionCharLog.append(" Харизма -25.");
                    character.getCareer().decCharisma(25);
                    break;
                case set0:
                    actionCharLog.append(" Харизма понижена до минимума: 0.");
                    character.getCareer().setCharisma(0);
                    break;
            }
            characterRepository.save(character);
        }
        if (adventure.getActionCharStrength() != null) {
            switch (adventure.getActionCharStrength()) {
                case inc5:
                    actionCharLog.append(" Сила +5.");
                    character.getCareer().incStrength(5);
                    break;
                case inc10:
                    actionCharLog.append(" Сила +10.");
                    character.getCareer().incStrength(10);
                    break;
                case inc25:
                    actionCharLog.append(" Сила +25.");
                    character.getCareer().incStrength(25);
                    break;
                case dec5:
                    actionCharLog.append(" Сила -5.");
                    character.getCareer().decStrength(5);
                    break;
                case dec10:
                    actionCharLog.append(" Сила -10.");
                    character.getCareer().decStrength(10);
                    break;
                case dec25:
                    actionCharLog.append(" Сила -25.");
                    character.getCareer().decStrength(25);
                    break;
                case set0:
                    actionCharLog.append(" Сила понижена до минимума: 0.");
                    character.getCareer().setStrength(0);
                    break;
            }
            characterRepository.save(character);
        }
        if (adventure.getActionCharCreativity() != null) {
            switch (adventure.getActionCharCreativity()) {
                case inc5:
                    actionCharLog.append(" Творчество +5.");
                    character.getCareer().incCreativity(5);
                    break;
                case inc10:
                    actionCharLog.append(" Творчество +10.");
                    character.getCareer().incCreativity(10);
                    break;
                case inc25:
                    actionCharLog.append(" Творчество +25.");
                    character.getCareer().incCreativity(25);
                    break;
                case dec5:
                    actionCharLog.append(" Творчество -5.");
                    character.getCareer().decCreativity(5);
                    break;
                case dec10:
                    actionCharLog.append(" Творчество -10.");
                    character.getCareer().decCreativity(10);
                    break;
                case dec25:
                    actionCharLog.append(" Творчество -25.");
                    character.getCareer().decCreativity(25);
                    break;
                case set0:
                    actionCharLog.append(" Творчество понижено до минимума: 0.");
                    character.getCareer().setCreativity(0);
                    break;
            }
            characterRepository.save(character);
        }
        if (adventure.getActionCharRace() != null) {
            actionCharLog.append(" Новая раса ").append(adventure.getActionCharRace().getName()).append(".");
            raceService.turnCharacterToRace(character, adventure.getActionCharRace());
        }
        if (adventure.getActionCharVocation() != null) {
            actionCharLog.append(" Новое призвание '").append(adventure.getActionCharVocation().getName()).append("'.");
            character.getCareer().setVocation(adventure.getActionCharVocation());
            characterRepository.save(character);
        }
        boolean appearanceChange = false;
        if (adventure.getActionCharAppBody() != null) {
            actionCharLog.append(" Новое тело: ").append(adventure.getActionCharAppBody().getTitle()).append(".");
            character.setBody(adventure.getActionCharAppBody());
            appearanceChange = true;
        }
        if (adventure.getActionCharAppEars() != null) {
            actionCharLog.append(" Новые уши: ").append(adventure.getActionCharAppEars().getTitle()).append(".");
            character.setEars(adventure.getActionCharAppEars());
            appearanceChange = true;
        }
        if (adventure.getActionCharAppEyebrows() != null) {
            actionCharLog.append(" Новые брови: ").append(adventure.getActionCharAppEyebrows().getTitle()).append(".");
            character.setEyebrows(adventure.getActionCharAppEyebrows());
            appearanceChange = true;
        }
        if (adventure.getActionCharAppEyes() != null) {
            actionCharLog.append(" Новые глаза: ").append(adventure.getActionCharAppEyes().getTitle()).append(".");
            character.setEyes(adventure.getActionCharAppEyes());
            appearanceChange = true;
        }
        if (adventure.getActionCharAppEyeColor() != null) {
            actionCharLog.append(" Новый цвет глаз: ").append(adventure.getActionCharAppEyeColor().getTitle()).append(".");
            character.setEyeColor(adventure.getActionCharAppEyeColor());
            appearanceChange = true;
        }
        if (adventure.getActionCharAppHairColor() != null) {
            actionCharLog.append(" Новый цвет волос: ").append(adventure.getActionCharAppHairColor().getTitle()).append(".");
            character.setHairColor(adventure.getActionCharAppHairColor());
            appearanceChange = true;
        }
        if (adventure.getActionCharAppHairType() != null) {
            actionCharLog.append(" Новый тип волос: ").append(adventure.getActionCharAppHairType().getTitle()).append(".");
            character.setHairType(adventure.getActionCharAppHairType());
            characterService.generateHairStyle(character);
            appearanceChange = true;
        }
        if (adventure.getActionCharAppHead() != null) {
            actionCharLog.append(" Новая голова: ").append(adventure.getActionCharAppHead().getTitle()).append(".");
            character.setHead(adventure.getActionCharAppHead());
            appearanceChange = true;
        }
        if (adventure.getActionCharAppHeight() != null) {
            actionCharLog.append(" Новый рост: ").append(adventure.getActionCharAppHeight().getTitle()).append(".");
            character.setHeight(adventure.getActionCharAppHeight());
            appearanceChange = true;
        }
        if (adventure.getActionCharAppMouth() != null) {
            actionCharLog.append(" Новый рот: ").append(adventure.getActionCharAppMouth().getTitle()).append(".");
            character.setMouth(adventure.getActionCharAppMouth());
            appearanceChange = true;
        }
        if (adventure.getActionCharAppNose() != null) {
            actionCharLog.append(" Новый нос: ").append(adventure.getActionCharAppNose().getTitle()).append(".");
            character.setNose(adventure.getActionCharAppNose());
            appearanceChange = true;
        }
        if (adventure.getActionCharAppSkinColor() != null) {
            actionCharLog.append(" Новый цвет кожи: ").append(adventure.getActionCharAppSkinColor().getTitle()).append(".");
            character.setSkinColor(adventure.getActionCharAppSkinColor());
            appearanceChange = true;
        }
        if (appearanceChange) {
            Race race = raceService.defineRace(character);
            if (race != character.getRace()) {
                actionCharLog.append(" Обновилась раса: ").append(race.getName()).append(".");
                character.setRace(race);
            }
            characterRepository.save(character);
        }
        if (actionCharLog.length() > 0) {
            adventureLog.append(" Персонаж ").append(character.getName()).append(" изменился: <br>").append(actionCharLog);
        }
    }

    private boolean itemsMatchAnd(Adventure adventure, List<Item> items, StringBuilder adventureLog) {
        System.out.println("itemsMatchAnd");
        boolean thing1Match = adventure.getThing1() == null;
        boolean thing2Match = adventure.getThing2() == null;
        boolean thing3Match = adventure.getThing3() == null;
        for (Item item : items) {
            if (item.getProject().getThing() == adventure.getThing1()) {
                applyActionThing(adventure.getActionThing1(), item, adventureLog);
                thing1Match = true;
            }
            if (item.getProject().getThing() == adventure.getThing2()) {
                applyActionThing(adventure.getActionThing2(), item, adventureLog);
                thing2Match = true;
            }
            if (item.getProject().getThing() == adventure.getThing3()) {
                applyActionThing(adventure.getActionThing3(), item, adventureLog);
                thing3Match = true;
            }
        }
        return thing1Match && thing2Match && thing3Match;
    }

    private boolean itemsMatchOr(Adventure adventure, List<Item> items, StringBuilder adventureLog) {
        System.out.println("itemsMatchOr");
        for (Item item : items) {
            if (item.getProject().getThing() == adventure.getThing1()) {
                applyActionThing(adventure.getActionThing1(), item, adventureLog);
                return true;
            }
            if (item.getProject().getThing() == adventure.getThing2()) {
                applyActionThing(adventure.getActionThing2(), item, adventureLog);
                return true;
            }
            if (item.getProject().getThing() == adventure.getThing3()) {
                applyActionThing(adventure.getActionThing3(), item, adventureLog);
                return true;
            }
        }
        return false;
    }

    private void awardFamilyForAdventure(Family family, Adventure adventure, StringBuilder adventureLog) {
        if (adventure.getAwardResType() != null) {
            adventureLog.append(" Получено: ");
            switch (adventure.getAwardResType()) {
                case FOOD:
                    family.getFamilyResources().addFood(adventure.getAwardResAmount());
                    adventureLog.append(ResUtils.foodImg(adventure.getAwardResAmount())).append(".");
                    break;
                case WOOD:
                    family.getFamilyResources().addWood(adventure.getAwardResAmount());
                    adventureLog.append(ResUtils.woodImg(adventure.getAwardResAmount())).append(".");
                    break;
                case METALL:
                    family.getFamilyResources().addMetall(adventure.getAwardResAmount());
                    adventureLog.append(ResUtils.metallImg(adventure.getAwardResAmount())).append(".");
                    break;
                case PLASTIC:
                    family.getFamilyResources().addPlastic(adventure.getAwardResAmount());
                    adventureLog.append(ResUtils.plasticImg(adventure.getAwardResAmount())).append(".");
                    break;
                case MICROELECTRONICS:
                    family.getFamilyResources().addMicroelectronics(adventure.getAwardResAmount());
                    adventureLog.append(ResUtils.microelectronicsImg(adventure.getAwardResAmount())).append(".");
                    break;
                case CLOTH:
                    family.getFamilyResources().addCloth(adventure.getAwardResAmount());
                    adventureLog.append(ResUtils.clothImg(adventure.getAwardResAmount())).append(".");
                    break;
                case STONE:
                    family.getFamilyResources().addStone(adventure.getAwardResAmount());
                    adventureLog.append(ResUtils.stoneImg(adventure.getAwardResAmount())).append(".");
                    break;
                case CHEMICAL:
                    family.getFamilyResources().addChemical(adventure.getAwardResAmount());
                    adventureLog.append(ResUtils.chemicalImg(adventure.getAwardResAmount())).append(".");
                    break;
            }
            familyRepository.save(family);
        }
        if (adventure.getAwardCraftPoint() > 0) {
            family.addCraftPoints(adventure.getAwardCraftPoint());
            familyRepository.save(family);
            adventureLog.append(" Получены крафтбаллы: ").append(adventure.getAwardCraftPoint()).append(" шт.");
        }
        if (adventure.getAwardMoney() > 0) {
            family.addMoney(adventure.getAwardMoney());
            familyRepository.save(family);
            adventureLog.append(" Получены деньги: ").append(adventure.getAwardMoney()).append(" д.");
        }
        if (adventure.getAwardThing() != null) {
            Project randomProjectOfThing = craftService.getRandomProjectOfThing(adventure.getAwardThing());
            Item item = craftService.giveGift(family, randomProjectOfThing.getId());
            item.setQuality(Const.ITEM_MAX_QUALITY);
            houseService.saveItem(item);
            adventureLog.append(" Получен предмет: ").append(item.getTitle());
        }
        if (adventure.getAwardBuff() != null) {
            Item item = craftService.giveGift(family, adventure.getAwardBuff());
            houseService.saveItem(item);
            adventureLog.append(" Получен бафф: ").append(item.getTitle());
        }
    }

    private void applyActionThing(ActionThing actionThing, Item item, StringBuilder adventureLog) {
        switch (actionThing) {
            case nothing:
                break;
            case destroy:
                adventureLog.append(" Отдан предмет: ").append(item.getTitle()).append(".");
                houseService.deleteItem(item);
                break;
            case qualityInc1:
                adventureLog.append(" Качество предмета +1: ").append(item.getTitle()).append(".");
                item.incQuality();
                houseService.saveItem(item);
                break;
            case qualityDec1:
                adventureLog.append(" Качество предмета -1: ").append(item.getTitle()).append(".");
                item.decQuality();
                houseService.saveItem(item);
                break;
            case quality0:
                adventureLog.append(" Качество предмета 0: ").append(item.getTitle()).append(".");
                item.setQuality(0);
                houseService.saveItem(item);
                break;
            case qualityMax:
                adventureLog.append(" Качество предмета " + Const.ITEM_MAX_QUALITY + ": ").append(item.getTitle()).append(".");
                item.setQuality(Const.ITEM_MAX_QUALITY);
                houseService.saveItem(item);
                break;
            default:
                logger.error("Unhandled ActionThing!");
        }
    }

    @RequestMapping(value = "/game/setCurrentFamilyAdventure", method = RequestMethod.POST)
    public String setCurrentFamilyAdventure(ModelMap model, RedirectAttributes redirectAttributes,
                                            @RequestParam(value = "num") int num) {
        User user = userRepository.findByUserName(getAuthUser().getUsername());
        Family family = user.getCurrentFamily();


        adventureService.setCurrentFamilyAdventure(family, num);


        return "redirect:/game/adventures";
    }

    // === ADMIN ===

    @RequestMapping("/admin/adventures")
    public String adminAdventures(ModelMap model, RedirectAttributes redirectAttributes) {
        List<Adventure> adventureList = adventureService.getAllAdventures();
        model.addAttribute("adventureList", adventureList);
        return "admin/adventures";
    }

    @RequestMapping(value = "/admin/actionsWithAdventure", method = RequestMethod.POST)
    public String actionsWithAdventure(ModelMap model, RedirectAttributes redirectAttributes,
                                       @RequestParam(value = "adventureId") Long adventureId,
                                       @RequestParam(value = "btn") String action) {
        Adventure adventure = adventureService.getAdventure(adventureId);
        if (adventure != null) {
            switch (action) {
                case "setApproved":
                    adventure.setStatus(AdventureStatus.approved);
                    adventureService.saveAdventure(adventure);
                    break;
                case "edit":
                    model.addAttribute("newAdventure", adventure);
                    fillAdventureCreationForm(model);
                    return "admin/adventureForm";
                default:
            }
        }


        return "redirect:/admin/adventures";
    }

    @RequestMapping("/admin/adventureForm")
    public String adventureForm(ModelMap model, RedirectAttributes redirectAttributes) {
        model.addAttribute("newAdventure", new Adventure());
        fillAdventureCreationForm(model);
        return "admin/adventureForm";
    }

    @RequestMapping(value = "/admin/createAdventure", method = RequestMethod.POST)
    public String createAdventure(ModelMap model, RedirectAttributes redirectAttributes,
                                  @ModelAttribute(value = "newAdventure") @Valid Adventure newAdventure, BindingResult result) {
        checkNewAdventureForm(newAdventure, result);
        if (result.hasErrors()) {
            fillAdventureCreationForm(model);
            return "admin/adventureForm";
        }

        newAdventure.setCreatedBy(familyRepository.findOne(1L));
        newAdventure.setStatus(AdventureStatus.approved);
        adventureService.saveAdventure(newAdventure);


        return "redirect:/admin/adventures";
    }

    // === USER ===

    @RequestMapping("/game/adventureForm")
    public String adventureUserForm(ModelMap model, RedirectAttributes redirectAttributes) {
        User user = userRepository.findByUserName(getAuthUser().getUsername());
        Family family = user.getCurrentFamily();
        model.addAttribute("family", family);

        model.addAttribute("newAdventure", new Adventure());

        fillAdventureCreationForm(model);
        return "game/adventureUserForm";
    }

    @RequestMapping(value = "/game/createAdventure", method = RequestMethod.POST)
    public String createUserAdventure(ModelMap model, RedirectAttributes redirectAttributes,
                                      @ModelAttribute(value = "newAdventure") @Valid Adventure newAdventure, BindingResult result) {
        User user = userRepository.findByUserName(getAuthUser().getUsername());
        Family family = user.getCurrentFamily();

        if (family.getAdventuresCompleted() >= Const.ADVENTURE_AMOUNT_PER_TURN && family.getMoney() >= Const.ADVENTURE_CREATION_COST) {
            checkNewAdventureForm(newAdventure, result);

            if (result.hasErrors()) {
                System.out.println("result = " + Arrays.toString(result.resolveMessageCodes("*")));
                model.addAttribute("family", family);
                fillAdventureCreationForm(model);
                return "game/adventureUserForm";
            }
            family.removeMoney(Const.ADVENTURE_CREATION_COST);
            familyRepository.save(family);

            newAdventure.setCreatedBy(family);
            newAdventure.setStatus(AdventureStatus.newAdventure);
            adventureService.saveAdventure(newAdventure);

            redirectAttributes.addFlashAttribute("mess", "Квест успешно создан. Ожидайте проверки модератором.");
            return "redirect:/game/adventures";
        }

        redirectAttributes.addFlashAttribute("mess", "Вашей семье необходимо пройти хотя бы " + Const.ADVENTURE_AMOUNT_PER_TURN +
                " квестов и иметь в распоряжении " + Const.ADVENTURE_CREATION_COST + " д. для оплаты нового квеста");
        return "redirect:/game/adventures";
    }

    private void checkNewAdventureForm(Adventure adventure, BindingResult result) {
        if (!adventure.getAnswerType().equals(AnswerType.character) && (adventure.getThing1() == null && adventure.getThing2() == null && adventure.getThing1() == null)) {
            result.rejectValue("thing1", "adventure.creation.thingsIsEmpty");
        }
        if (!(adventure.getAnswerType().equals(AnswerType.manyItemsAnd) || adventure.getAnswerType().equals(AnswerType.manyItemsAnd) || adventure.getAnswerType().equals(AnswerType.oneItem))) {
            if (adventure.getCharSex().equals("") && adventure.getCharRace() == null && adventure.getCharVocation() == null &&
                    adventure.getCharIntelligence() == null && adventure.getCharCharisma() == null && adventure.getCharStrength() == null && adventure.getCharCreativity() == null &&
                    adventure.getCharAppBody() == null && adventure.getCharAppEars() == null && adventure.getCharAppEyebrows() == null &&
                    adventure.getCharAppEyes() == null && adventure.getCharAppEyeColor() == null && adventure.getCharAppHairColor() == null &&
                    adventure.getCharAppHairType() == null && adventure.getCharAppHead() == null && adventure.getCharAppHeight() == null &&
                    adventure.getCharAppMouth() == null && adventure.getCharAppNose() == null && adventure.getCharAppSkinColor() == null) {
                result.rejectValue("charSex", "adventure.creation.charIsEmpty");
            }
        }

        // awards
        if (adventure.getAwardThing() == null && adventure.getAwardMoney() == 0 && adventure.getAwardCraftPoint() == 0 && adventure.getAwardResType() == null && adventure.getAwardBuff() == null) {
            result.rejectValue("awardResType", "adventure.creation.awardIsEmpty");
        }
        if (adventure.getAwardResType() != null && adventure.getAwardResAmount() == 0) {
            result.rejectValue("awardResType", "adventure.creation.awardResAmountIsEmpty");
        }
        // TODO: обработать все возможные ошибки формы
    }

    private void fillAdventureCreationForm(ModelMap model) {
        model.addAttribute("landscapes", adventureService.getLandscapes());
        model.addAttribute("subjects", adventureService.getSubjects());
        model.addAttribute("answerTypes", AnswerType.values());

        model.addAttribute("thingList", craftService.getAllUsualThings());
        model.addAttribute("charSkills", CharSkill.values());

        model.addAttribute("raceList", raceService.getRaces());
        model.addAttribute("vocationList", careerService.getVocationList());

        model.addAttribute("bodyList", appService.getBodyList(appService.ALL));
        model.addAttribute("earsList", appService.getEarsList(appService.ALL));
        model.addAttribute("eyebrowsList", appService.getEyebrowsList(appService.ALL));
        model.addAttribute("eyeColorList", appService.getEyeColorList(appService.ALL));
        model.addAttribute("eyesList", appService.getEyesList(appService.ALL));
        model.addAttribute("hairColorList", appService.getHairColorList(appService.ALL));
        model.addAttribute("hairTypeList", appService.getHairTypeList(appService.ALL));
        model.addAttribute("headList", appService.getHeadList(appService.ALL));
        model.addAttribute("heightList", appService.getHeightList(appService.ALL));
        model.addAttribute("mouthList", appService.getMouthList(appService.ALL));
        model.addAttribute("noseList", appService.getNoseList(appService.ALL));
        model.addAttribute("skinColorList", appService.getSkinColorList(appService.ALL));

        model.addAttribute("actionThingValues", ActionThing.values());
        model.addAttribute("actionCharValues", ActionChar.values());
        model.addAttribute("actionCharSkillValues", ActionCharSkill.values());

        model.addAttribute("resTypeValues", ResType.values());
        model.addAttribute("buffList", craftService.getAvailableBuffs());
    }


}
