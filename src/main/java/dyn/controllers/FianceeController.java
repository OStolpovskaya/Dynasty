package dyn.controllers;

/**
 * Created by OM on 17.02.2017.
 */


import dyn.form.FianceeFilter;
import dyn.model.Character;
import dyn.model.*;
import dyn.model.appearance.*;
import dyn.model.career.Career;
import dyn.model.career.Career_;
import dyn.repository.*;
import dyn.service.Const;
import dyn.service.FamilyLogService;
import dyn.service.RaceService;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;

import static dyn.controllers.GameController.getAuthUser;
import static dyn.controllers.GameController.loc;

@Controller
public class FianceeController {
    private static final Logger logger = LogManager.getLogger(FianceeController.class);
    @Autowired
    MessageSource messageSource;
    @Autowired
    AdminController adminController;
    @Autowired
    FamilyLogService familyLogService;
    @Autowired
    RaceService raceService;

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RaceRepository raceRepository;
    @Autowired
    private FamilyRepository familyRepository;
    @Autowired
    private CharacterRepository characterRepository;
    @Autowired
    private FianceeRepository fianceeRepository;
    @PersistenceContext
    private EntityManager em;


    @RequestMapping(value = "/game/chooseFiancee", params = "characterId", method = RequestMethod.GET)
    public String chooseFiancee(ModelMap model, RedirectAttributes redirectAttributes,
                                @RequestParam(value = "characterId") long characterId,
                                @ModelAttribute(value = "fianceeFilter") FianceeFilter fianceeFilter) {
        User user = userRepository.findByUserName(getAuthUser().getUsername());
        Family family = user.getCurrentFamily();

        Character character = characterRepository.findByIdAndFamilyAndLevelAndSexAndSpouseIsNull(characterId, family, family.getLevel(), "male");
        if (character != null) {

            /* // генерация невест, если в базе пусто
            int size = fianceeRepository.findByCharacterFamilyNotAndCharacterLevel(family, family.getLevel()).size();
            if (size == 0) {
                String fiancees = adminController.genFiancees(family.getLevel(), familyRepository.findOne(1L));
                logger.info(user.getUserName() + " want to choose fiancee, but there was no fiancees in the database. Generated: " + fiancees);
            }
            */

            CriteriaBuilder cb = em.getCriteriaBuilder();
            CriteriaQuery<Fiancee> cq = cb.createQuery(Fiancee.class);

            Root<Fiancee> fianceeRoot = cq.from(Fiancee.class);
            Join<Fiancee, Character> fianceeCharacter = fianceeRoot.join(Fiancee_.character);
            Join<Character, Career> fianceeCharacterCareer = fianceeCharacter.join(Character_.career);

            Join<Character, SkinColor> fianceeCharacterSkinColor = fianceeCharacter.join(Character_.skinColor);
            Join<Character, Height> fianceeCharacterHeight = fianceeCharacter.join(Character_.height);
            Join<Character, Body> fianceeCharacterBody = fianceeCharacter.join(Character_.body);
            Join<Character, Head> fianceeCharacterHead = fianceeCharacter.join(Character_.head);
            Join<Character, Ears> fianceeCharacterEars = fianceeCharacter.join(Character_.ears);
            Join<Character, Eyebrows> fianceeCharacterEyebrows = fianceeCharacter.join(Character_.eyebrows);
            Join<Character, Eyes> fianceeCharacterEyes = fianceeCharacter.join(Character_.eyes);
            Join<Character, EyeColor> fianceeCharacterEyeColor = fianceeCharacter.join(Character_.eyeColor);
            Join<Character, Nose> fianceeCharacterNose = fianceeCharacter.join(Character_.nose);
            Join<Character, Mouth> fianceeCharacterMouth = fianceeCharacter.join(Character_.mouth);
            Join<Character, HairType> fianceeCharacterHairType = fianceeCharacter.join(Character_.hairType);
            Join<Character, HairColor> fianceeCharacterHairColor = fianceeCharacter.join(Character_.hairColor);

            List<Predicate> predicates = new ArrayList<>();

            predicates.add(cb.lessThanOrEqualTo(fianceeCharacter.get(Character_.level), family.getLevel()));
            predicates.add(cb.notEqual(fianceeCharacter.get(Character_.family), family));
            predicates.add(cb.notEqual(fianceeCharacter.get(Character_.family), character.getFather().getSpouse().getFamily()));

            if (!fianceeFilter.isEmpty()) {
                if (fianceeFilter.getRace() != null) {
                    predicates.add(cb.equal(fianceeCharacter.get(Character_.race), fianceeFilter.getRace()));
                }
                if (fianceeFilter.getVocation() != null) {
                    predicates.add(cb.equal(fianceeCharacterCareer.get(Career_.vocation), fianceeFilter.getVocation()));
                }
                List<String> appearance = fianceeFilter.getAppearance();
                if (appearance.size() != 0) {
                    if (appearance.contains("skinColor")) {
                        predicates.add(cb.equal(fianceeCharacterSkinColor.get(SkinColor_.type), AppearanceType.rare));
                    }
                    if (appearance.contains("height")) {
                        predicates.add(cb.equal(fianceeCharacterHeight.get(Height_.type), AppearanceType.rare));
                    }
                    if (appearance.contains("body")) {
                        predicates.add(cb.equal(fianceeCharacterBody.get(Body_.type), AppearanceType.rare));
                    }
                    if (appearance.contains("head")) {
                        predicates.add(cb.equal(fianceeCharacterHead.get(Head_.type), AppearanceType.rare));
                    }
                    if (appearance.contains("ears")) {
                        predicates.add(cb.equal(fianceeCharacterEars.get(Ears_.type), AppearanceType.rare));
                    }
                    if (appearance.contains("eyebrows")) {
                        predicates.add(cb.equal(fianceeCharacterEyebrows.get(Eyebrows_.type), AppearanceType.rare));
                    }
                    if (appearance.contains("eyes")) {
                        predicates.add(cb.equal(fianceeCharacterEyes.get(Eyes_.type), AppearanceType.rare));
                    }
                    if (appearance.contains("eyeColor")) {
                        predicates.add(cb.equal(fianceeCharacterEyeColor.get(EyeColor_.type), AppearanceType.rare));
                    }
                    if (appearance.contains("nose")) {
                        predicates.add(cb.equal(fianceeCharacterNose.get(Nose_.type), AppearanceType.rare));
                    }
                    if (appearance.contains("mouth")) {
                        predicates.add(cb.equal(fianceeCharacterMouth.get(Mouth_.type), AppearanceType.rare));
                    }
                    if (appearance.contains("hairType")) {
                        predicates.add(cb.equal(fianceeCharacterHairType.get(HairType_.type), AppearanceType.rare));
                    }
                    if (appearance.contains("hairColor")) {
                        predicates.add(cb.equal(fianceeCharacterHairColor.get(HairColor_.type), AppearanceType.rare));
                    }
                }
            }
            cq.where(cb.and(predicates.toArray(new Predicate[predicates.size()])));
            cq.orderBy(cb.desc(fianceeCharacter.get(Character_.level)), cb.asc(fianceeCharacter.get(Character_.race)),
                    cb.asc(fianceeRoot.get(Fiancee_.cost)), cb.asc(fianceeCharacterCareer.get(Career_.vocation)));

            TypedQuery<Fiancee> q = em.createQuery(cq);
            List<Fiancee> fianceeList = q.getResultList();
            /*брак запрещен
            c сестрой
            с двоюродной сестрой
            с высшей расой, если ты человек или гм-человек
            с высшей расой, если ты сам из высших рас, но другой
            */
            List<Fiancee> available = new ArrayList<>();
            List<Fiancee> disabled = new ArrayList<>();
            List<Fiancee> lowerLevel = new ArrayList<>();
            for (Fiancee fiancee : fianceeList) {
                if ((character.getRace().getId() == Race.RACE_HUMAN || character.getRace().getId() == Race.RACE_GM_HUMAN) && fiancee.getCharacter().getRace().getId() >= Race.RACE_HIGH) {
                    fiancee.isDisabled = true;
                    fiancee.disableReason = messageSource.getMessage("fiancee.raceMismatchForFiancee", null, loc());
                } else if (character.getRace().getId() >= Race.RACE_HIGH && fiancee.getCharacter().getRace().getId() >= Race.RACE_HIGH && character.getRace().getId() != fiancee.getCharacter().getRace().getId()) {
                    fiancee.isDisabled = true;
                    fiancee.disableReason = messageSource.getMessage("fiancee.raceMismatchForCharacter", null, loc());
                } else if (character.getFamily().getMoney() < fiancee.getCost()) {
                    fiancee.isDisabled = true;
                    fiancee.disableReason = messageSource.getMessage("fiancee.notEnoughMoney", null, loc());
                }

                if (fiancee.isDisabled) {
                    disabled.add(fiancee);
                } else {
                    if (fiancee.getCharacter().getLevel() < character.getLevel()) {
                        lowerLevel.add(fiancee);
                    } else {
                        available.add(fiancee);
                    }

                }
            }

            model.addAttribute("family", family);
            model.addAttribute("character", character);
            model.addAttribute("fianceeList", available);
            model.addAttribute("disabledFianceeList", disabled);
            model.addAttribute("lowerLevel", lowerLevel);
            return "/game/chooseFiancee";
        }

        redirectAttributes.addFlashAttribute("mess", messageSource.getMessage("chooseFiancee.characterCantChooseFiancee", null, loc()));
        logger.error(user.getUserName() + "'s character " + characterId + " is not belongs to user's current family singles");
        return "redirect:/game";
    }

    @RequestMapping(value = "/game/makeFiancee", params = "characterId", method = RequestMethod.POST)
    public String makeFiancee(ModelMap model, RedirectAttributes redirectAttributes,
                              @RequestParam(value = "fiancee") Long fianceeId,
                              @RequestParam(value = "characterId") Long characterId) {
        User user = userRepository.findByUserName(getAuthUser().getUsername());
        Family family = user.getCurrentFamily();

        if (family.getPairsNum() >= family.getHouse().getPairsNum()) {
            redirectAttributes.addFlashAttribute("mess", "Вы не можете создать еще одну пару, ваш дом слишком мал. Вы можете разместить пар: " + family.getHouse().getPairsNum());
            logger.error(user.getUserName() + " want to make a pair, but max pairs num is already reached");
            return "redirect:/game";
        }

        Character character = characterRepository.findByIdAndFamilyAndLevelAndSexAndSpouseIsNull(characterId, family, family.getLevel(), "male");
        Fiancee fiancee = fianceeRepository.findOne(fianceeId);

        if (fiancee == null) { // другой игрок выкупил
            logger.error(user.getUserName() + " try to choose fiancee, but no such character in fiancee database");
            redirectAttributes.addFlashAttribute("mess", messageSource.getMessage("chooseFiancee.noSuchFiancee", null, loc()));
            return "redirect:/game/chooseFiancee?characterId=" + characterId;
        }

        if (character != null) {
            Character wife = fiancee.getCharacter();
            if (family.getMoney() >= fiancee.getCost()) {

                character.setSpouse(wife);
                characterRepository.save(character);

                wife.setSpouse(character);
                characterRepository.save(wife);

                fianceeRepository.delete(fiancee);

                family.setMoney(family.getMoney() - fiancee.getCost());
                family.setPairsNum(family.getPairsNum() + 1);
                familyRepository.save(family);

                familyLogService.addToLog(family, "Вы выкупили невесту " + wife.getName() + " из семьи " + wife.getFamily().getFamilyName() + " для своего персонажа " + character.getName() + ". Потрачено: " + fiancee.getCost() + " д.");
                logger.info(user.getUserName() + " has chosen fiancee " + wife.getName());
                redirectAttributes.addFlashAttribute("mess", messageSource.getMessage("chooseFiancee.success", new Object[]{character.getName(), wife.getName(), fiancee.getCost()}, loc()));
                return "redirect:/game#char" + character.getFather().getId();
            } else {
                logger.error(user.getUserName() + " hasn't enough money to choose fiancee " + wife.getName());
                redirectAttributes.addFlashAttribute("mess", messageSource.getMessage("chooseFiancee.notEnoughMoney", null, loc()));
                return "redirect:/game/chooseFiancee?characterId=" + characterId;
            }
        }

        redirectAttributes.addFlashAttribute("mess", messageSource.getMessage("chooseFiancee.characterCantChooseFiancee", null, loc()));
        logger.error(user.getUserName() + "'s character " + characterId + " is not belongs to user's current family singles");
        return "redirect:/game";

    }

    @RequestMapping(value = "/game/putUpForFiancee", method = RequestMethod.POST)
    public String setToFiancee(ModelMap model, RedirectAttributes redirectAttributes,
                               @RequestParam(value = "female") long characterId) {
        User user = userRepository.findByUserName(getAuthUser().getUsername());
        Family family = user.getCurrentFamily();

        if (family.getFianceeNum() >= family.getHouse().getFianceeNum()) {
            redirectAttributes.addFlashAttribute("mess", "Вы не можете опубликовать анкету невесты, ваш дом слишком мал. Вы можете разместить анкет: " + family.getHouse().getFianceeNum());
            logger.error(user.getUserName() + " want to put up for fiancee, but max fiancee num is already reached");
            return "redirect:/game";
        }

        Character character = characterRepository.findByIdAndFamilyAndLevelAndSexAndSpouseIsNull(characterId, family, family.getLevel(), "female");

        if (character != null && !character.isFiancee()) {
            Fiancee fiancee = new Fiancee();
            fiancee.setCharacter(character);

            int cost = Const.FIANCEE_COST * character.getRaceCoefficient() * family.getLevel();

            fiancee.setCost(cost);
            fianceeRepository.save(fiancee);

            family.setFianceeNum(family.getFianceeNum() + 1);
            family.setMoney(family.getMoney() + cost);
            familyRepository.save(family);

            familyLogService.addToLog(family, "Вы опубликовали анкету невесты " + character.getName() + ". Получено: " + cost + " д.");
            redirectAttributes.addFlashAttribute("mess", messageSource.getMessage("becomeFiancee.characterBecomeFiancee", new Object[]{character.getName(), cost}, loc()));
            logger.info(user.getUserName() + "'s character " + character.getName() + " become fiancee");
            return "redirect:/game#char" + character.getFather().getId();
        }


        redirectAttributes.addFlashAttribute("mess", messageSource.getMessage("becomeFiancee.characterCantBecomeFiancee", null, loc()));
        logger.error(user.getUserName() + "'s character " + characterId + " can not belongs to user's current family female singles or is already feancee");
        return "redirect:/game";
    }


    //postAllHumanFiancees
    @RequestMapping(value = "/game/postAllHumanFiancees", method = RequestMethod.POST)
    public String postAllHumanFiancees(ModelMap model, RedirectAttributes redirectAttributes) {
        User user = userRepository.findByUserName(getAuthUser().getUsername());
        Family family = user.getCurrentFamily();
        model.addAttribute("family", family);

        int cost = 50 * family.getLevel();

        List<Character> fianceeList = characterRepository.findByFamilyAndLevelAndSexAndRaceAndSpouseIsNull(family, family.getLevel(), "female", raceRepository.findOne(1L));
        int size = fianceeList.size();
        if (size == 0) {
            redirectAttributes.addFlashAttribute("mess", "У вас нет дочерей, которых можно поместить в базу невест");
            logger.error(user.getUserName() + " has no daughters of race Human to become fiancee");
            return "redirect:/game";
        }
        int count = 0;
        for (Character character : fianceeList) {
            if (!character.isFiancee()) {
                Fiancee fiancee = new Fiancee();
                fiancee.setCharacter(character);
                fiancee.setCost(cost);
                fianceeRepository.save(fiancee);

                count += 1;
            }
        }

        if (count == 0) {
            redirectAttributes.addFlashAttribute("mess", "Все ваши дочери уже в базе невест!");
            logger.error(user.getUserName() + " daughters of race Human already in fiancee database");
            return "redirect:/game";
        }

        redirectAttributes.addFlashAttribute("mess", "Все дочери (" + count + ") расы Человек теперь в базе невест!");
        logger.debug(user.getUserName() + "'s daughters of race Human become fiancee");
        return "redirect:/game";
    }


}
