package dyn.controllers;

/**
 * Created by OM on 17.02.2017.
 */


import dyn.model.Character;
import dyn.model.Family;
import dyn.model.User;
import dyn.repository.CharacterRepository;
import dyn.repository.FamilyRepository;
import dyn.repository.RaceRepository;
import dyn.repository.UserRepository;
import dyn.repository.appearance.EyesRepository;
import dyn.repository.appearance.HeadRepository;
import dyn.repository.appearance.HeightRepository;
import dyn.repository.appearance.SkinColorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
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
    private UserRepository userRepository;
    @Autowired
    private FamilyRepository familyRepository;
    @Autowired
    private CharacterRepository characterRepository;
    @Autowired
    private RaceRepository raceRepository;

    @RequestMapping("/game")
    public String main(ModelMap model, RedirectAttributes redirectAttributes) {
        User user = userRepository.findByUserName(getAuthUser().getUsername());
        model.addAttribute("user", user);
        System.out.println(user.getUserName());

        List<Family> families = user.getFamilies();
        if (families.size() == 0) {
            System.out.println("User doesn't have any family, redirect");
            redirectAttributes.addFlashAttribute("mess", "You have no families, create please!");
            return "redirect:/game/addNewFamily";
        } else {
            Family family = user.getCurrentFamily();
            System.out.println("Current family: " + family.getFamilyName() + ", level: " + family.getLevel());
            model.addAttribute("currentFamily", family);

            List<Character> characters = characterRepository.findByFamilyAndLevel(family, family.getLevel());
            model.addAttribute("characters", characters);
        }

        return "game";
    }

    @RequestMapping(value = "/game/turn", method = RequestMethod.POST)
    public String turn() {
        User user = userRepository.findByUserName(getAuthUser().getUsername());
        System.out.println(user.getUserName() + " makes a turn!");

        Family family = user.getCurrentFamily();
        // TODO: generate children
        List<Character> characters = characterRepository.findByFamilyAndLevelAndSexAndSpouseIsNotNull(family, family.getLevel(), "male");
        int newLevel = family.getLevel() + 1;
        for (Character character : characters) {
            // percents
            int min = 1; // amount of children
            int max = 7; // amount of children
            double dominantPercent = 0.5; // whose feature is inherited, father or mother

            Character wife = character.getSpouse();
            int childAmount = min + (int) (Math.random() * max);
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
                // TODO: ask how to make it correct:
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
        UserDetails userDetail = (UserDetails) auth.getPrincipal();
        return userDetail;
    }

}
