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
                                @RequestParam(value = "characterId") int characterId) {
        System.out.println("GameController.chooseFiancee GET characterId=" + characterId);

        User user = userRepository.findByUserName(getAuthUser().getUsername());
        Family family = user.getCurrentFamily();

        List<Character> characters = characterRepository.findByFamilyAndLevelAndSexAndSpouseIsNull(family, family.getLevel(), "male");
        for (Character character : characters) {
            if (character.getId() == characterId) {
                List<Fiancee> fianceeList = fianceeRepository.findByCharacterFamilyNotAndCharacterLevel(family, family.getLevel()); //TODO: only female?
                model.addAttribute("character", character);
                model.addAttribute("fianceeList", fianceeList);
                model.addAttribute("characterId", characterId);
                System.out.println("fianceeList.size() = " + fianceeList.size());
                return "/game/chooseFiancee";
            }
        }
        redirectAttributes.addFlashAttribute("mess", messageSource.getMessage("characterCantChooseFiancee", null, loc()));
        System.out.println("Character " + characterId + " is not belongs to user's current family singles");
        return "redirect:/game";
    }

    @RequestMapping(value = "/game/chooseFiancee", params = "characterId", method = RequestMethod.POST)
    public String makeFiancee(ModelMap model, RedirectAttributes redirectAttributes,
                              @RequestParam(value = "fiancee") Long fianceeId,
                              @RequestParam(value = "characterId") Long characterId) {
        // TODO: check valid user and caharacter and fiancee
        Character character = characterRepository.findOne(characterId);
        Fiancee fiancee = fianceeRepository.findOne(fianceeId);

        Family family = character.getFamily();

        if (family.getMoney() < fiancee.getCost()) {
            redirectAttributes.addFlashAttribute("mess", messageSource.getMessage("chooseFiancee.notEnoughMoney", null, loc()));
            return "redirect:/game/chooseFiancee";
        }

        Character wife = fiancee.getCharacter();

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

        redirectAttributes.addFlashAttribute("mess", messageSource.getMessage("chooseFiancee.success", new Object[]{character.getName(), wife.getName(), fiancee.getCost()}, loc()));
        return "redirect:/game";
    }

    @RequestMapping(value = "/game/putUpForFiancee", method = RequestMethod.POST)
    public String setToFiancee(ModelMap model, RedirectAttributes redirectAttributes,
                               @RequestParam(value = "cost") int cost,
                               @RequestParam(value = "female") long characterId) {
        System.out.println("GameController.setToFiancee POST cost=" + cost + ", characterId=" + characterId);
        User user = userRepository.findByUserName(getAuthUser().getUsername());
        Family family = user.getCurrentFamily();

        List<Character> characters = characterRepository.findByFamilyAndLevelAndSexAndSpouseIsNull(family, family.getLevel(), "female");
        for (Character character : characters) {
            if (character.getId() == characterId && character.isFiancee() == false) {
                Fiancee fiancee = new Fiancee();
                fiancee.setCharacter(character);
                fiancee.setCost(cost);
                fianceeRepository.save(fiancee);

                redirectAttributes.addFlashAttribute("mess", messageSource.getMessage("characterBecomeFiancee", null, loc()));
                System.out.println("Character " + characterId + " become fiancee");
                return "redirect:/game";
            }
        }

        redirectAttributes.addFlashAttribute("mess", messageSource.getMessage("characterCantBecomeFiancee", null, loc()));
        System.out.println("Character " + characterId + " can not belongs to user's current family female singles or is already feiancee");
        return "redirect:/game";
    }

}
