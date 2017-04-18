package dyn.controllers;

/**
 * Created by OM on 17.02.2017.
 */


import dyn.form.RaceAppearanceForm;
import dyn.model.Character;
import dyn.model.*;
import dyn.repository.*;
import dyn.service.AppearanceService;
import dyn.service.CareerService;
import dyn.service.RaceService;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.List;

@Controller
public class AdminController {
    private static final Logger logger = LogManager.getLogger(AdminController.class);
    @Autowired
    AppearanceService app;
    @Autowired
    RaceService raceService;
    @Autowired
    CareerService careerService;

    @Autowired
    RaceRepository raceRepository;
    @Autowired
    AchievementRepository achievementRepository;
    @Autowired
    BuffRepository buffRepository;
    @Autowired
    FamilyRepository familyRepository;
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

    @RequestMapping("/admin/fiancees")
    public String fianceesView(ModelMap model) {

        List<List<Fiancee>> levelList = new ArrayList<>();
        List<Fiancee> fiancees = fianceeRepository.findAllByOrderByCharacterLevel();
        int level = 0;
        for (Fiancee fiancee : fiancees) {
            if (fiancee.getCharacter().getLevel() != level) {
                levelList.add(new ArrayList<>());
                level = fiancee.getCharacter().getLevel();
            }
            levelList.get(level - 1).add(fiancee);
        }

        model.addAttribute("fianceeLevelList", levelList);
        return "admin/fiancees";
    }

    @RequestMapping("/admin/families")
    public String familiesView(ModelMap model) {

        model.addAttribute("familyList", familyRepository.findAll());
        return "admin/families";
    }

    @RequestMapping(value = "/admin/family", method = RequestMethod.GET)
    public String familyView(ModelMap model,
                             @RequestParam("familyId") Long familyId,
                             RedirectAttributes redirectAttributes) {
        Family family = familyRepository.findOne(familyId);

        model.addAttribute("family", family);
        return "admin/family";
    }

    @RequestMapping(value = "/admin/generateFiancee", method = RequestMethod.POST)
    public String generateFiancees(ModelMap model,
                                   @RequestParam("level") int level,
                                   RedirectAttributes redirectAttributes) {
        logger.debug("AdminController.generateFiancees, level=" + level);
        User user = userRepository.findByUserName(getAuthUser().getUsername());
        Family family = user.getCurrentFamily();

        String names = genFiancees(level, family);
        redirectAttributes.addFlashAttribute("mess", "Fiancees are generated: " + names);
        return "redirect:/admin";
    }

    public String genFiancees(int level, Family family) {
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
            female.setRace(raceRepository.findOne(Race.RACE_HUMAN));

            female.generateView();

            female.setCareer(careerService.generateCareerForFounders());

            characterRepository.save(female);

            Fiancee fiancee = new Fiancee();
            fiancee.setCharacter(female);
            fiancee.setCost(10);
            fianceeRepository.save(fiancee);

            names.append(fiancee.getCharacter().getName()).append(" ");
        }
        return names.toString();
    }

    @RequestMapping(value = "/admin/updateCharacter", method = RequestMethod.POST)
    public String updateCharacterRequest(ModelMap model,
                                         @RequestParam("character_id") long characterId,
                                         RedirectAttributes redirectAttributes) {
        logger.debug("AdminController.generateFiancees, characterId=" + characterId);

        Character character = characterRepository.findOne(characterId);
        if (character != null) {
            updateCharacter(character);
        } else {
            redirectAttributes.addFlashAttribute("mess", "ОШИБКА! Персонаж с id=" + characterId + " не найден!");
            return "redirect:/admin";
        }

        redirectAttributes.addFlashAttribute("mess", "Персонаж обновлен: " + character.getName());
        return "redirect:/admin";
    }

    @RequestMapping(value = "/admin/alterCharacterForm", method = RequestMethod.POST)
    public String alterCharacterForm(ModelMap model,
                                     @RequestParam("character_id") long characterId,
                                     RedirectAttributes redirectAttributes) {
        Character character = characterRepository.findOne(characterId);
        if (character != null) {
            model.addAttribute("character", character);

            RaceAppearanceForm raceAppearanceForm = new RaceAppearanceForm();
            raceAppearanceForm = app.fillRaceAppearanceForm(raceAppearanceForm);
            model.addAttribute("form", raceAppearanceForm);
            return "admin/alterCharacterForm";
        } else {
            redirectAttributes.addFlashAttribute("mess", "Персонаж не найден: " + characterId);
            return "redirect:/admin";
        }

    }

    @RequestMapping(value = "/admin/alterCharacter", method = RequestMethod.POST)
    public String alterCharacter(ModelMap model,
                                 @RequestParam("character_id") long characterId,
                                 @ModelAttribute("character") Character formCharacter,
                                 RedirectAttributes redirectAttributes) {
        String username = getAuthUser().getUsername();
        Character character = characterRepository.findOne(characterId);
        boolean updated = false;
        if (character != null) {
            logger.info(username + " changes " + character.getName());
            if (character.getBody() != formCharacter.getBody()) {
                logger.debug("   body changed:" + character.getBody() + " -> " + formCharacter.getBody());
                character.setBody(formCharacter.getBody());
                updated = true;
            }
            if (character.getEars() != formCharacter.getEars()) {
                logger.debug("   ears changed:" + character.getEars() + " -> " + formCharacter.getEars());
                character.setEars(formCharacter.getEars());
                updated = true;
            }
            if (character.getEyebrows() != formCharacter.getEyebrows()) {
                logger.debug("   eyebrows changed:" + character.getEyebrows() + " -> " + formCharacter.getEyebrows());
                character.setEyebrows(formCharacter.getEyebrows());
                updated = true;
            }
            if (character.getEyeColor() != formCharacter.getEyeColor()) {
                logger.debug("   eyeColor changed:" + character.getEyeColor() + " -> " + formCharacter.getEyeColor());
                character.setEyeColor(formCharacter.getEyeColor());
                updated = true;
            }
            if (character.getEyes() != formCharacter.getEyes()) {
                logger.debug("   eyes changed:" + character.getEyes() + " -> " + formCharacter.getEyes());
                character.setEyes(formCharacter.getEyes());
                updated = true;
            }
            if (character.getHairColor() != formCharacter.getHairColor()) {
                logger.debug("   hairColor changed:" + character.getHairColor() + " -> " + formCharacter.getHairColor());
                character.setHairColor(formCharacter.getHairColor());
                updated = true;
            }
            if (character.getHairType() != formCharacter.getHairType()) {
                logger.debug("   hairType changed:" + character.getHairType() + " -> " + formCharacter.getHairType());
                character.setHairType(formCharacter.getHairType());
                character.setHairStyle(app.getRandomHairStyle(character.getSex(), character.getHairType()));
                updated = true;
            }
            if (character.getHead() != formCharacter.getHead()) {
                logger.debug("   head changed:" + character.getHead() + " -> " + formCharacter.getHead());
                character.setHead(formCharacter.getHead());
                updated = true;
            }
            if (character.getHeight() != formCharacter.getHeight()) {
                logger.debug("   height changed:" + character.getHeight() + " -> " + formCharacter.getHeight());
                character.setHeight(formCharacter.getHeight());
                updated = true;
            }
            if (character.getMouth() != formCharacter.getMouth()) {
                logger.debug("   mouth changed:" + character.getMouth() + " -> " + formCharacter.getMouth());
                character.setMouth(formCharacter.getMouth());
                updated = true;
            }
            if (character.getNose() != formCharacter.getNose()) {
                logger.debug("   nose changed:" + character.getNose() + " -> " + formCharacter.getNose());
                character.setNose(formCharacter.getNose());
                updated = true;
            }
            if (character.getSkinColor() != formCharacter.getSkinColor()) {
                logger.debug("   skinColor changed:" + character.getSkinColor() + " -> " + formCharacter.getSkinColor());
                character.setSkinColor(formCharacter.getSkinColor());
                updated = true;
            }

            if (updated) {
                updateCharacter(character);
                redirectAttributes.addFlashAttribute("mess", "Персонаж обновлен: " + character.getName());
                return "redirect:/admin";
            } else {
                redirectAttributes.addFlashAttribute("mess", "Персонаж не был изменен: " + character.getName());
                return "redirect:/admin";
            }

        } else {
            redirectAttributes.addFlashAttribute("mess", "Персонаж не найден: " + characterId);
            return "redirect:/admin";
        }
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

    public void updateCharacter(Character character) {
        character.generateView();

        Race race = raceService.defineRace(character);
        character.setRace(race);

        characterRepository.save(character);
    }


    private UserDetails getAuthUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return (UserDetails) auth.getPrincipal();
    }

}
