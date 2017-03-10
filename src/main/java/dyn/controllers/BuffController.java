package dyn.controllers;

/**
 * Created by OM on 17.02.2017.
 */


import dyn.model.*;
import dyn.model.Character;
import dyn.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@Controller
public class BuffController {
    @Autowired
    MessageSource messageSource;
    @Autowired
    BuffRepository buffRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private FamilyRepository familyRepository;
    @Autowired
    private CharacterRepository characterRepository;
    @Autowired
    private RaceRepository raceRepository;

    public static Locale loc() {
        return LocaleContextHolder.getLocale();
    }

    public static UserDetails getAuthUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return (UserDetails) auth.getPrincipal();
    }

    // ============ CHOOSING BUFFS ============
    @RequestMapping(value = "/game/chooseBuffs", params = "characterId", method = RequestMethod.GET)
    public String chooseBuffs(ModelMap model, RedirectAttributes redirectAttributes,
                              @RequestParam(value = "characterId") int characterId) {
        System.out.println("GameController.chooseBuffs GET characterId=" + characterId);

        User user = userRepository.findByUserName(getAuthUser().getUsername());
        Family family = user.getCurrentFamily();

        List<Character> characters = characterRepository.findByFamilyAndLevelAndSexAndSpouseIsNotNull(family, family.getLevel(), "male");
        for (Character character : characters) {
            if (character.getId() == characterId) {
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
        }
        redirectAttributes.addFlashAttribute("mess", messageSource.getMessage("game.chooseBuffs.characterCantChooseBuffs", null, loc()));
        System.out.println("Character " + characterId + " is not belongs to user's current family fiances");
        return "redirect:/game";
    }

    @RequestMapping(value = "/game/chooseBuffs", params = "characterId", method = RequestMethod.POST)
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

                    Object[] messageArguments = {character.getName(), messageSource.getMessage(buff.getTitle(), null, loc()), buff.getCost()};
                    redirectAttributes.addFlashAttribute("mess", messageSource.getMessage("game.chooseBuffs.success", messageArguments, loc()));
                    System.out.println("Character " + characterId + " now has buff " + buff.getTitle());
                    return "redirect:/game";
                } else {
                    redirectAttributes.addFlashAttribute("mess", messageSource.getMessage("game.chooseBuffs.notEnoughMoney", null, loc()));
                    System.out.println("Not enough money for this buff " + buff.getTitle());
                    return "redirect:/game";
                }

            } else {
                redirectAttributes.addFlashAttribute("mess", messageSource.getMessage("game.chooseBuffs.characterHasThisBuffOrContradictoryToThisBuff", null, loc()));
                System.out.println("Character " + characterId + " has this buff or contradictory to this buff");
                return "redirect:/game";
            }

        }
        redirectAttributes.addFlashAttribute("mess", messageSource.getMessage("game.chooseBuffs.characterCantChooseBuffs", null, loc()));
        System.out.println("Character " + characterId + " is not belongs to user's current family fiances");
        return "redirect:/game";
    }

}
