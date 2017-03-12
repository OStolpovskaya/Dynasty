package dyn.controllers;

/**
 * Created by OM on 17.02.2017.
 */


import dyn.model.Buff;
import dyn.model.Character;
import dyn.model.Family;
import dyn.model.User;
import dyn.repository.*;
import dyn.service.AppearanceService;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
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
import java.util.Locale;
import java.util.Set;

@Controller
public class GameController {
    private static final Logger logger = LogManager.getLogger(GameController.class);
    @Autowired
    MessageSource messageSource;
    @Autowired
    BuffRepository buffRepository;
    @Autowired
    AppearanceService app;
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

    @RequestMapping("/game")
    public String main(ModelMap model, RedirectAttributes redirectAttributes) {
        User user = userRepository.findByUserName(getAuthUser().getUsername());
        model.addAttribute("user", user);

        List<Family> families = user.getFamilies();
        if (families.size() == 0) {
            logger.info(user.getUserName() + " doesn't have any family, redirect to create the first family");
            redirectAttributes.addFlashAttribute("mess", messageSource.getMessage("new.user", null, loc()));
            return "redirect:/game/addNewFamily";
        }
        Family family = user.getCurrentFamily();
        logger.debug(user.getUserName() + " Current family: " + family.getFamilyName() + ", level: " + family.getLevel());
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
        Character character = characterRepository.findOne(characterId);
        model.addAttribute("character", character);

        return "/game/character";
    }

    @RequestMapping(value = "/game/turn", method = RequestMethod.POST)
    public String turn() {
        User user = userRepository.findByUserName(getAuthUser().getUsername());
        logger.info(user.getUserName() + " makes a turn!");

        Family family = user.getCurrentFamily();

        List<Character> characters = characterRepository.findByFamilyAndLevelAndSexAndSpouseIsNotNull(family, family.getLevel(), "male");
        int newLevel = family.getLevel() + 1;
        for (Character character : characters) {
            Set<Buff> buffs = character.getBuffs();

            double dominantPercent = 0.5; // whose feature is inherited, father or mother
            if (buffs.contains(buffRepository.findByTitle(Buff.DOMINANT_MOTHER))) {
                dominantPercent = 0.2;
            }
            if (buffs.contains(buffRepository.findByTitle(Buff.DOMINANT_FATHER))) {
                dominantPercent = 0.8;
            }

            double sonOrDaughterPercent = 0.5; // male or female child
            if (buffs.contains(buffRepository.findByTitle(Buff.MANY_SONS))) {
                sonOrDaughterPercent = 0.8;
            }
            if (buffs.contains(buffRepository.findByTitle(Buff.MANY_DAUGHTERS))) {
                sonOrDaughterPercent = 0.2;
            }

            double genModPercent = 0.05; // percentage of genetic modification
            if (buffs.contains(buffRepository.findByTitle(Buff.GENETIC_MOD))) {
                genModPercent = 0.40;
            }

            Character wife = character.getSpouse();
            int childAmount = getAmountOfChildren(buffs);

            logger.info(user.getUserName() + "'s character " + character.getName() + " marries " + wife.getName() + " and they have " + childAmount + " children");

            for (int i = 0; i < childAmount; i++) {
                Character child = new Character();
                if (Math.random() < sonOrDaughterPercent) {
                    child.setSex("male");
                    child.setName(characterRepository.getRandomNameMale());
                } else {
                    child.setSex("female");
                    child.setName(characterRepository.getRandomNameFemale());
                }
                child.setFather(character);
                child.setFamily(family);
                child.setLevel(newLevel);

                int featureToGenMod = 0; // which feature will have genetic modification
                if (Math.random() < genModPercent) {
                    featureToGenMod = (int) (1 + Math.random() * 4);
                }
                logger.info("child: " + child.getName() + ", genModFeature = " + featureToGenMod);

                child.setHeight(featureToGenMod == 1 ? app.getRandomHeight(app.RARE) : (Math.random() < dominantPercent ? character.getHeight() : wife.getHeight()));
                child.setHead(featureToGenMod == 2 ? app.getRandomHead(app.RARE) : (Math.random() < dominantPercent ? character.getHead() : wife.getHead()));
                child.setEyes(featureToGenMod == 3 ? app.getRandomEyes(app.RARE) : (Math.random() < dominantPercent ? character.getEyes() : wife.getEyes()));
                child.setSkinColor(featureToGenMod == 4 ? app.getRandomSkinColor(app.RARE) : (Math.random() < dominantPercent ? character.getSkinColor() : wife.getSkinColor()));

                //TODO: race computation
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

    private int getAmountOfChildren(Set<Buff> buffs) {
        double[] percentage = new double[]{0.25, 0.55, 0.80, 0.90, 0.95, 0.98, 1.00}; // normal
        if (buffs.contains(buffRepository.findByTitle(Buff.FERTILITY))) {
            percentage = new double[]{0.05, 0.15, 0.30, 0.50, 0.75, 0.90, 1.00}; // buff fertility
        }
        if (buffs.contains(buffRepository.findByTitle(Buff.FIVE_CHILDREN))) {
            percentage = new double[]{0.00, 0.00, 0.00, 0.00, 1.00, 0.00, 0.00}; // buff 5 children
        }

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
