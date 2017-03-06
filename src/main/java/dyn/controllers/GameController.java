package dyn.controllers;

/**
 * Created by OM on 17.02.2017.
 */


import dyn.model.Character;
import dyn.model.Family;
import dyn.model.Fiancee;
import dyn.model.User;
import dyn.repository.*;
import dyn.repository.appearance.EyesRepository;
import dyn.repository.appearance.HeadRepository;
import dyn.repository.appearance.HeightRepository;
import dyn.repository.appearance.SkinColorRepository;
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
import java.util.List;

@Controller
public class GameController {
    @Autowired
    EyesRepository eyesRepository;
    @Autowired
    HeadRepository headRepository;
    @Autowired
    HeightRepository heightRepository;
    @Autowired
    SkinColorRepository skinColorRepository;
    @Autowired
    MessageSource messageSource;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private FamilyRepository familyRepository;
    @Autowired
    private CharacterRepository characterRepository;
    @Autowired
    private RaceRepository raceRepository;
    @Autowired
    private FianceeRepository fianceeRepository;

    @RequestMapping("/game")
    public String main(ModelMap model, RedirectAttributes redirectAttributes) {
        User user = userRepository.findByUserName(getAuthUser().getUsername());
        model.addAttribute("user", user);
        System.out.println(user.getUserName());

        List<Family> families = user.getFamilies();
        if (families.size() == 0) {
            System.out.println("User doesn't have any family, redirect");
            redirectAttributes.addFlashAttribute("mess", messageSource.getMessage("new.user", null, LocaleContextHolder.getLocale()));
            return "redirect:/game/addNewFamily";
        }
        Family family = user.getCurrentFamily();
        System.out.println("Current family: " + family.getFamilyName() + ", level: " + family.getLevel());
        model.addAttribute("currentFamily", family);

        List<Character> fathers;
        if (family.getLevel() > 0) {
            fathers = characterRepository.findByFamilyAndLevelAndSexAndSpouseIsNotNull(family, family.getLevel() - 1, "male");
        } else {
            fathers = characterRepository.findByFamilyAndLevel(family, family.getLevel());
        }
        model.addAttribute("fathers", fathers);

        return "game";
    }

    @RequestMapping("/game/character")
    public String characterView(ModelMap model, RedirectAttributes redirectAttributes,
                                @RequestParam(value = "characterId") long characterId) {
        System.out.println("GameController.characterView GET characterId=" + characterId);
        Character character = characterRepository.findOne(characterId);
        model.addAttribute("character", character);

        return "/game/character";
    }


    @RequestMapping(value = "/game/turn", method = RequestMethod.POST)
    public String turn() {
        User user = userRepository.findByUserName(getAuthUser().getUsername());
        System.out.println(user.getUserName() + " makes a turn!");

        Family family = user.getCurrentFamily();

        List<Character> characters = characterRepository.findByFamilyAndLevelAndSexAndSpouseIsNotNull(family, family.getLevel(), "male");
        int newLevel = family.getLevel() + 1;
        for (Character character : characters) {
            double dominantPercent = 0.5; // whose feature is inherited, father or mother

            Character wife = character.getSpouse();
            int childAmount = getAmountOfChildren();
            System.out.println(character.getName() + " marries " + wife.getName() + " and they have " + childAmount + " children");

            for (int i = 0; i < childAmount; i++) {
                Character child = new Character();
                if (Math.random() < 0.5) {
                    child.setSex("male");
                    child.setName(characterRepository.getRandomNameMale());
                } else {
                    child.setSex("female");
                    child.setName(characterRepository.getRandomNameFemale());
                }
                child.setFather(character);
                child.setFamily(family);
                child.setLevel(newLevel);

                child.setHeight(Math.random() < dominantPercent ? character.getHeight() : wife.getHeight());
                child.setHead(Math.random() < dominantPercent ? character.getHead() : wife.getHead());
                child.setEyes(Math.random() < dominantPercent ? character.getEyes() : wife.getEyes());
                child.setSkinColor(Math.random() < dominantPercent ? character.getSkinColor() : wife.getSkinColor());
                child.setRace(raceRepository.findByName("race.human"));

                child.generateView();
                characterRepository.save(child);
            }

            family.setLevel(newLevel);
            familyRepository.save(family);
        }

        return "redirect:/game";
    }

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
        redirectAttributes.addFlashAttribute("mess", messageSource.getMessage("characterCantChooseFiancee", null, LocaleContextHolder.getLocale()));
        System.out.println("Character " + characterId + " is not belongs to user's current family singles");
        return "redirect:/game";
    }

    @RequestMapping(value = "/game/chooseFiancee", params = "characterId", method = RequestMethod.POST)
    public String makeFiancee(ModelMap model, RedirectAttributes redirectAttributes,
                              @RequestParam(value = "fiancee") Long fianceeId,
                              @RequestParam(value = "characterId") Long characterId) {

        Character character = characterRepository.findOne(characterId);
        Fiancee fiancee = fianceeRepository.findOne(fianceeId);

        Family family = character.getFamily();

        if (family.getMoney() < fiancee.getCost()) {
            redirectAttributes.addFlashAttribute("mess", messageSource.getMessage("chooseFiancee.notEnoughMoney", null, LocaleContextHolder.getLocale()));
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

        redirectAttributes.addFlashAttribute("mess", messageSource.getMessage("chooseFiancee.success", new Object[]{character.getName(), wife.getName(), fiancee.getCost()}, LocaleContextHolder.getLocale()));
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

                redirectAttributes.addFlashAttribute("mess", messageSource.getMessage("characterBecomeFiancee", null, LocaleContextHolder.getLocale()));
                System.out.println("Character " + characterId + " become fiancee");
                return "redirect:/game";
            }
        }

        redirectAttributes.addFlashAttribute("mess", messageSource.getMessage("characterCantBecomeFiancee", null, LocaleContextHolder.getLocale()));
        System.out.println("Character " + characterId + " can not belongs to user's current family female singles or is already feiancee");
        return "redirect:/game";
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


    private UserDetails getAuthUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return (UserDetails) auth.getPrincipal();
    }

    private int getAmountOfChildren() {
        // TODO: make percentage depends on character's buffs (arguments?)
        double[] percentage = new double[]{0.25, 0.55, 0.80, 0.90, 0.95, 0.98, 1.00}; // normal
        //double[] percentage = new double[] {0.05, 0.15, 0.30, 0.50, 0.75, 0.90, 1.00}; // buff fertility
        //double[] percentage = new double[] {0.00, 0.00, 0.00, 0.00, 1.00, 0.00, 0.00}; // buff 5 children

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
}
