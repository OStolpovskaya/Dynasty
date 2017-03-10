package dyn.controllers;

/**
 * Created by OM on 17.02.2017.
 */


import dyn.model.Character;
import dyn.model.Family;
import dyn.model.Fiancee;
import dyn.model.User;
import dyn.repository.CharacterRepository;
import dyn.repository.FamilyRepository;
import dyn.repository.FianceeRepository;
import dyn.repository.UserRepository;
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

import java.util.List;

import static dyn.controllers.GameController.getAuthUser;
import static dyn.controllers.GameController.loc;

@Controller
public class FianceeController {
    private static final Logger logger = LogManager.getLogger(FianceeController.class);
    @Autowired
    MessageSource messageSource;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private FamilyRepository familyRepository;
    @Autowired
    private CharacterRepository characterRepository;
    @Autowired
    private FianceeRepository fianceeRepository;

    @RequestMapping(value = "/game/chooseFiancee", params = "characterId", method = RequestMethod.GET)
    public String chooseFiancee(ModelMap model, RedirectAttributes redirectAttributes,
                                @RequestParam(value = "characterId") long characterId) {
        User user = userRepository.findByUserName(getAuthUser().getUsername());
        Family family = user.getCurrentFamily();

        Character character = characterRepository.findByIdAndFamilyAndLevelAndSexAndSpouseIsNull(characterId, family, family.getLevel(), "male");
        if (character != null) {
            List<Fiancee> fianceeList = fianceeRepository.findByCharacterFamilyNotAndCharacterLevel(family, family.getLevel()); //TODO: only female?
            model.addAttribute("character", character);
            model.addAttribute("fianceeList", fianceeList);
            model.addAttribute("characterId", characterId);
            return "/game/chooseFiancee";
        }

        redirectAttributes.addFlashAttribute("mess", messageSource.getMessage("chooseFiancee.characterCantChooseFiancee", null, loc()));
        logger.error(user.getUserName() + "'s character " + characterId + " is not belongs to user's current family singles");
        return "redirect:/game";
    }

    @RequestMapping(value = "/game/chooseFiancee", params = "characterId", method = RequestMethod.POST)
    public String makeFiancee(ModelMap model, RedirectAttributes redirectAttributes,
                              @RequestParam(value = "fiancee") Long fianceeId,
                              @RequestParam(value = "characterId") Long characterId) {
        User user = userRepository.findByUserName(getAuthUser().getUsername());
        Family family = user.getCurrentFamily();

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
                familyRepository.save(family);

                Family wifeFamily = wife.getFamily();
                wifeFamily.setMoney(wifeFamily.getMoney() + fiancee.getCost());
                familyRepository.save(wifeFamily);

                logger.info(user.getUserName() + " has chosen fiancee " + wife.getName());
                redirectAttributes.addFlashAttribute("mess", messageSource.getMessage("chooseFiancee.success", new Object[]{character.getName(), wife.getName(), fiancee.getCost()}, loc()));
                return "redirect:/game";
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

        Character character = characterRepository.findByIdAndFamilyAndLevelAndSexAndSpouseIsNull(characterId, family, family.getLevel(), "female");

        if (character != null && character.isFiancee() == false) {
            Fiancee fiancee = new Fiancee();
            fiancee.setCharacter(character);
            fiancee.setCost(cost);
            fianceeRepository.save(fiancee);

            redirectAttributes.addFlashAttribute("mess", messageSource.getMessage("becomeFiancee.characterBecomeFiancee", null, loc()));
            logger.info(user.getUserName() + "'s character " + character.getName() + " become fiancee");
            return "redirect:/game";
        }


        redirectAttributes.addFlashAttribute("mess", messageSource.getMessage("becomeFiancee.characterCantBecomeFiancee", null, loc()));
        logger.error(user.getUserName() + "'s character " + characterId + " can not belongs to user's current family female singles or is already feancee");
        return "redirect:/game";
    }

}
