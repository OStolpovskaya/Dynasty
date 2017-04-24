package dyn.controllers;

/**
 * Created by OM on 17.02.2017.
 */


import dyn.form.FianceeFilter;
import dyn.model.Character;
import dyn.model.*;
import dyn.model.career.Career;
import dyn.model.career.Career_;
import dyn.repository.*;
import dyn.service.FamilyLogService;
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


    @RequestMapping(value = "/game/chooseFiancee", params = "characterId", method = RequestMethod.POST)
    public String chooseFiancee(ModelMap model, RedirectAttributes redirectAttributes,
                                @RequestParam(value = "characterId") long characterId,
                                @ModelAttribute(value = "fianceeFilter") FianceeFilter fianceeFilter) {
        User user = userRepository.findByUserName(getAuthUser().getUsername());
        Family family = user.getCurrentFamily();

        Character character = characterRepository.findByIdAndFamilyAndLevelAndSexAndSpouseIsNull(characterId, family, family.getLevel(), "male");
        if (character != null) {

            int size = fianceeRepository.findByCharacterFamilyNotAndCharacterLevel(family, family.getLevel()).size();
            if (size == 0) {
                String fiancees = adminController.genFiancees(family.getLevel(), familyRepository.findOne(1L));
                logger.info(user.getUserName() + " want to choose fiancee, but there was no fiancees in the database. Generated: " + fiancees);
            }

            CriteriaBuilder cb = em.getCriteriaBuilder();
            CriteriaQuery<Fiancee> cq = cb.createQuery(Fiancee.class);

            Root<Fiancee> fianceeRoot = cq.from(Fiancee.class);
            Join<Fiancee, Character> fianceeCharacter = fianceeRoot.join(Fiancee_.character);
            Join<Character, Career> fianceeCharacterCareer = fianceeCharacter.join(Character_.career);

            List<Predicate> predicates = new ArrayList<>();

            predicates.add(cb.equal(fianceeCharacter.get(Character_.level), family.getLevel()));
            predicates.add(cb.notEqual(fianceeCharacter.get(Character_.family), family));

            if (!fianceeFilter.isEmpty()) {
                if (fianceeFilter.getRace() != null) {
                    predicates.add(cb.equal(fianceeCharacter.get(Character_.race), fianceeFilter.getRace()));
                }
                if (fianceeFilter.getVocation() != null) {
                    predicates.add(cb.equal(fianceeCharacterCareer.get(Career_.vocation), fianceeFilter.getVocation()));
                }
            }
            cq.where(cb.and(predicates.toArray(new Predicate[predicates.size()])));
            cq.orderBy(cb.asc(fianceeRoot.get(Fiancee_.cost)), cb.asc(fianceeCharacter.get(Character_.race)), cb.asc(fianceeCharacterCareer.get(Career_.vocation)));

            TypedQuery<Fiancee> q = em.createQuery(cq);
            List<Fiancee> fianceeList = q.getResultList();

            model.addAttribute("family", family);
            model.addAttribute("character", character);
            model.addAttribute("fianceeList", fianceeList);
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

        if (character != null && fiancee != null) {
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

                Family wifeFamily = wife.getFamily();
                wifeFamily.setMoney(wifeFamily.getMoney() + fiancee.getCost());
                familyRepository.save(wifeFamily);

                familyLogService.addToLog(family, "Вы выкупили невесту " + wife.getName() + " из семьи " + wife.getFamily().getFamilyName() + " для своего персонажа " + character.getName() + ". Потрачено: " + fiancee.getCost() + " р.");
                familyLogService.addToLog(wifeFamily, "Семья " + family.getFamilyName() + " выкупила одну из ваших невест: " + wife.getName() + " (уровень: " + wife.getLevel() + "). Получено: " + fiancee.getCost() + " р.");
                logger.info(user.getUserName() + " has chosen fiancee " + wife.getName());
                redirectAttributes.addFlashAttribute("mess", messageSource.getMessage("chooseFiancee.success", new Object[]{character.getName(), wife.getName(), fiancee.getCost()}, loc()));
                return "redirect:/game#char" + character.getFather().getId();
            } else {
                logger.error(user.getUserName() + " hasn't enough money to choose fiancee " + wife.getName());
                redirectAttributes.addFlashAttribute("mess", messageSource.getMessage("chooseFiancee.notEnoughMoney", null, loc()));
                return "redirect:/game/chooseFiancee";
            }
        }

        redirectAttributes.addFlashAttribute("mess", messageSource.getMessage("chooseFiancee.characterCantChooseFiancee", null, loc()));
        logger.error(user.getUserName() + "'s character " + characterId + " is not belongs to user's current family singles");
        return "redirect:/game";

    }

    @RequestMapping(value = "/game/putUpForFiancee", method = RequestMethod.POST)
    public String setToFiancee(ModelMap model, RedirectAttributes redirectAttributes,
                               @RequestParam(value = "cost") int cost,
                               @RequestParam(value = "female") long characterId) {
        User user = userRepository.findByUserName(getAuthUser().getUsername());
        Family family = user.getCurrentFamily();

        if (family.getFianceeNum() >= family.getHouse().getFianceeNum()) {
            redirectAttributes.addFlashAttribute("mess", "Вы не можете опубликовать анкету невесты, ваш дом слишком мал. Вы можете разместить анкет: " + family.getHouse().getFianceeNum());
            logger.error(user.getUserName() + " want to put up for fiancee, but max fiancee num is already reached");
            return "redirect:/game";
        }

        if (cost < 50 * family.getLevel()) {
            redirectAttributes.addFlashAttribute("mess", messageSource.getMessage("becomeFiancee.costMustBeGreater", null, loc()));
            logger.error(user.getUserName() + "'s character " + characterId + " can not become fiancee because small cost");
            return "redirect:/game";
        }

        Character character = characterRepository.findByIdAndFamilyAndLevelAndSexAndSpouseIsNull(characterId, family, family.getLevel(), "female");

        if (character != null && !character.isFiancee()) {
            Fiancee fiancee = new Fiancee();
            fiancee.setCharacter(character);
            fiancee.setCost(cost);
            fianceeRepository.save(fiancee);

            family.setFianceeNum(family.getFianceeNum() + 1);
            familyRepository.save(family);

            familyLogService.addToLog(family, "Вы опубликовали анкету невесты " + character.getName() + ". Стоимость выкупа: " + cost + " р.");
            redirectAttributes.addFlashAttribute("mess", messageSource.getMessage("becomeFiancee.characterBecomeFiancee", null, loc()));
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
