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
    @Autowired
    EyesRepository eyesRepository;
    @Autowired
    HeadRepository headRepository;
    @Autowired
    HeightRepository heightRepository;
    @Autowired
    SkinColorRepository skinColorRepository;
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

        model.addAttribute("eyesList", eyesRepository.findAll());
        model.addAttribute("headList", headRepository.findAll());
        model.addAttribute("heightList", heightRepository.findAll());
        model.addAttribute("skinColorList", skinColorRepository.findAll());
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
        System.out.println("AdminController.generateFiancees, level=" + level);
        User user = userRepository.findByUserName(getAuthUser().getUsername());
        Family family = user.getCurrentFamily();

        StringBuilder names = new StringBuilder();
        for (int i = 0; i < 5; i++) {
            Character female = new Character();
            female.setName(characterRepository.getRandomNameFemale());
            female.setSex("female");
            female.setHeight(heightRepository.getRandomUsual());
            female.setHead(headRepository.getRandomUsual());
            female.setEyes(eyesRepository.getRandomUsual());
            female.setSkinColor(skinColorRepository.getRandomUsual());

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
        if (sex == null || sex.isEmpty()) {
            System.out.println("Sex is null or empty. Set it to male.");
            sex = "male";
        }

        if (!"female".equals(sex)) {
            sex = "male";
        }

        Character character = new Character();
        character.setSex(sex);
        character.setHeight(heightRepository.getRandom());
        character.setHead(headRepository.getRandom());
        character.setEyes(eyesRepository.getRandom());
        character.setSkinColor(skinColorRepository.getRandom());

        character.generateView();

        model.addAttribute("character", character);

        return "admin/random";
    }

    private UserDetails getAuthUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return (UserDetails) auth.getPrincipal();
    }

}
