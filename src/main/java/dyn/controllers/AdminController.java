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
    private static final Logger logger = LogManager.getLogger(AdminController.class);
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

        model.addAttribute("bodyList", app.getBodyList(app.ALL));
        model.addAttribute("earsList", app.getEarsList(app.ALL));
        model.addAttribute("eyebrowsList", app.getEyebrowsList(app.ALL));
        model.addAttribute("eyeColorList", app.getEyeColorList(app.ALL));
        model.addAttribute("eyesList", app.getEyesList(app.ALL));
        model.addAttribute("hairColorList", app.getHairColorList(app.ALL));
        model.addAttribute("hairStyleList", app.getHairStyleList(app.ALL));
        model.addAttribute("hairTypeList", app.getHairTypeList(app.ALL));
        model.addAttribute("headList", app.getHeadList(app.ALL));
        model.addAttribute("heightList", app.getHeightList(app.ALL));
        model.addAttribute("mouthList", app.getMouthList(app.ALL));
        model.addAttribute("noseList", app.getNoseList(app.ALL));
        model.addAttribute("skinColorList", app.getSkinColorList(app.ALL));
        return "admin/appearance";
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

            female.setBody(app.getRandomBody(app.USUAL));
            female.setEars(app.getRandomEars(app.USUAL));
            female.setEyebrows(app.getRandomEyeBrows(app.USUAL));
            female.setEyeColor(app.getRandomEyeColor(app.USUAL));
            female.setEyes(app.getRandomEyes(app.USUAL));
            female.setHairColor(app.getRandomHairColor(app.USUAL));
            female.setHairType(app.getRandomHairType(app.USUAL));
            female.setHairStyle(app.getRandomHairStyle(female.getSex(), female.getHairType()));
            female.setHead(app.getRandomHead(app.USUAL));
            female.setHeight(app.getRandomHeight(app.USUAL));
            female.setMouth(app.getRandomMouth(app.USUAL));
            female.setNose(app.getRandomNose(app.USUAL));
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

    @RequestMapping(value = "/admin/reGenerateView", method = RequestMethod.POST)
    public String reGenerateView(ModelMap model,
                                 @RequestParam("character_id") long characterId,
                                 RedirectAttributes redirectAttributes) {
        logger.debug("AdminController.generateFiancees, characterId=" + characterId);

        Character character = characterRepository.findOne(characterId);
        if (character != null) {
            character.generateView();
            characterRepository.save(character);
        } else {
            redirectAttributes.addFlashAttribute("mess", "ОШИБКА! Персонаж с id=" + characterId + " не найден!");
            return "redirect:/admin";
        }

        redirectAttributes.addFlashAttribute("mess", "Персонаж обновлен: " + character.getName());
        return "redirect:/admin";
    }

    @RequestMapping("/admin/random")
    public String achievements(ModelMap model, @RequestParam(value = "sex", required = false) String sex) {
        if (sex == null || sex.isEmpty() || !sex.equals("female")) {
            sex = "male";
        }

        Character character = new Character();
        character.setSex(sex);
        character.setBody(app.getRandomBody(app.ALL));
        character.setEars(app.getRandomEars(app.ALL));
        character.setEyebrows(app.getRandomEyeBrows(app.ALL));
        character.setEyeColor(app.getRandomEyeColor(app.ALL));
        character.setEyes(app.getRandomEyes(app.ALL));
        character.setHairColor(app.getRandomHairColor(app.ALL));
        character.setHairType(app.getRandomHairType(app.ALL));
        character.setHairStyle(app.getRandomHairStyle(sex, character.getHairType()));
        character.setHead(app.getRandomHead(app.ALL));
        character.setHeight(app.getRandomHeight(app.ALL));
        character.setMouth(app.getRandomMouth(app.ALL));
        character.setNose(app.getRandomNose(app.ALL));
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
