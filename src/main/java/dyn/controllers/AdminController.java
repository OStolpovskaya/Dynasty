package dyn.controllers;

/**
 * Created by OM on 17.02.2017.
 */


import dyn.model.Character;
import dyn.model.Family;
import dyn.model.Fiancee;
import dyn.model.User;
import dyn.repository.*;
import dyn.service.AppearanceService;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class AdminController {
    private static final Logger logger = LogManager.getLogger(FianceeController.class);
    @Autowired
    AppearanceService app;
    @Autowired
    RaceRepository raceRepository;
    @Autowired
    AchievementRepository achievementRepository;
    @Autowired
    BuffRepository buffRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private FianceeRepository fianceeRepository;
    @Autowired
    private CharacterRepository characterRepository;

    @RequestMapping("/admin")
    public String admin(ModelMap model) {
        return "admin";
    }

    @RequestMapping("/admin/appearance")
    public String appearance(ModelMap model) {

        model.addAttribute("heightList", app.getHeightList(app.ALL));
        model.addAttribute("headList", app.getHeadList(app.ALL));
        model.addAttribute("eyesList", app.getEyesList(app.ALL));
        model.addAttribute("skinColorList", app.getSkinColorList(app.ALL));
        return "admin/appearance";
    }

    @RequestMapping("/admin/race")
    public String race(ModelMap model) {

        model.addAttribute("raceList", raceRepository.findAll());
        return "admin/race";
    }

    @RequestMapping("/admin/achievements")
    public String achievements(ModelMap model) {

        model.addAttribute("achievementList", achievementRepository.findAll());
        return "admin/achievements";
    }

    @RequestMapping("/admin/buffs")
    public String buffs(ModelMap model) {

        model.addAttribute("buffList", buffRepository.findAll());
        return "admin/buffs";
    }

    @RequestMapping(value = "/admin/generateFiancee", method = RequestMethod.POST)
    public String generateFiancees(ModelMap model,
                                   @RequestParam("level") int level,
                                   RedirectAttributes redirectAttributes) {
        logger.debug("AdminController.generateFiancees, level=" + level);
        User user = userRepository.findByUserName(getAuthUser().getUsername());
        Family family = user.getCurrentFamily();

        StringBuilder names = new StringBuilder();
        for (int i = 0; i < 5; i++) {
            Character female = new Character();
            female.setName(characterRepository.getRandomNameFemale());
            female.setSex("female");

            female.setHeight(app.getRandomHeight(app.USUAL));
            female.setHead(app.getRandomHead(app.USUAL));
            female.setEyes(app.getRandomEyes(app.USUAL));
            female.setSkinColor(app.getRandomSkinColor(app.USUAL));

            female.setFamily(family);
            female.setLevel(level);
            female.setRace(raceRepository.findByName("race.human"));

            female.generateView();
            characterRepository.save(female);

            Fiancee fiancee = new Fiancee();
            fiancee.setCharacter(female);
            fiancee.setCost(10);
            fianceeRepository.save(fiancee);

            names.append(fiancee.getCharacter().getName()).append(" ");
        }
        redirectAttributes.addFlashAttribute("mess", "Fiancees are generated: " + names.toString());
        return "redirect:/admin";
    }

    @RequestMapping("/admin/random")
    public String achievements(ModelMap model, @RequestParam(value = "sex", required = false) String sex) {
        if (sex == null || sex.isEmpty() || !sex.equals("female")) {
            sex = "male";
        }

        Character character = new Character();
        character.setSex(sex);
        character.setHeight(app.getRandomHeight(app.ALL));
        character.setHead(app.getRandomHead(app.ALL));
        character.setEyes(app.getRandomEyes(app.ALL));
        character.setSkinColor(app.getRandomSkinColor(app.ALL));

        character.generateView();

        model.addAttribute("character", character);

        return "admin/random";
    }


    private UserDetails getAuthUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return (UserDetails) auth.getPrincipal();
    }

}
