package dyn.controllers;

import dyn.form.FamilyForm;
import dyn.model.*;
import dyn.model.Character;
import dyn.model.appearance.SkinColor;
import dyn.repository.*;
import dyn.service.*;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static dyn.controllers.GameController.loc;

/**
 * Created by OM on 01.03.2017.
 */
@Controller
public class FamilyController {
    private static final Logger logger = LogManager.getLogger(FamilyController.class);
    @Autowired
    AppearanceService app;

    @Autowired
    CareerService careerService;

    @Autowired
    CraftService craftService;

    @Autowired
    FamilyLogService familyLogService;
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
    private BuffRepository buffRepository;
    @Autowired
    private HouseRepository houseRepository;

    @RequestMapping("game/families")
    public String families(ModelMap model, RedirectAttributes redirectAttributes) {
        User user = userRepository.findByUserName(getAuthUser().getUsername());
        List<Family> families = user.getFamilies();
        if (families.size() == 0) {
            logger.debug(user.getUserName() + " doesn't have any family");
            redirectAttributes.addFlashAttribute("mess", messageSource.getMessage("new.user", null, loc()));
            return "redirect:/game/addNewFamily";
        } else {
            model.addAttribute("families", families);

            Family family = user.getCurrentFamily();
            model.addAttribute("family", family);

            List<Map<Character, List<Character>>> levelFatherChildren = new ArrayList<>();
            for (int i = 0; i <= family.getLevel(); i++) {
                List<Character> fathersOnLevel = characterRepository.findByFamilyAndLevelAndSexAndSpouseIsNotNull(family, i, "male");
                Map<Character, List<Character>> fatherChildrenMap = new LinkedHashMap<>();
                for (Character father : fathersOnLevel) {
                    fatherChildrenMap.put(father, father.getChildren());
                }
                levelFatherChildren.add(fatherChildrenMap);
            }
            model.addAttribute("levelFatherChildren", levelFatherChildren);

            model.addAttribute("familyLog", familyLogService.getFamilyLogOrderbyLevel(family));
        }
        return "game/families";
    }

    @GetMapping("game/addNewFamily")
    public String createNewFamily(ModelMap model, @ModelAttribute("familyForm") FamilyForm familyForm, RedirectAttributes redirectAttributes) {
        User user = userRepository.findByUserName(getAuthUser().getUsername());
        List<Family> families = user.getFamilies();
        if (families.size() >= Const.MAX_FAMILIES) {
            redirectAttributes.addFlashAttribute("mess", "Вы достигли максимального количества семей: " + Const.MAX_FAMILIES);
            return "redirect:/game/families";
        }

        Race race = raceRepository.findOne(Race.RACE_HUMAN);

        SkinColor skinColor = app.getRandomSkinColor(app.USUAL);

        Character male = new Character();
        male.setName(characterRepository.getRandomNameMale());
        male.setSex("male");
        male.setRace(race);
        male.setLevel(0);

        male.setCareer(careerService.generateCareerForFounders());

        male.setBody(app.getRandomBody(app.USUAL));
        male.setEars(app.getRandomEars(app.USUAL));
        male.setEyebrows(app.getRandomEyeBrows(app.USUAL));
        male.setEyeColor(app.getRandomEyeColor(app.USUAL));
        male.setEyes(app.getRandomEyes(app.USUAL));
        male.setHairColor(app.getRandomHairColor(app.USUAL));
        male.setHairType(app.getRandomHairType(app.USUAL));
        male.setHairStyle(app.getRandomHairStyle(male.getSex(), male.getHairType()));
        male.setHead(app.getRandomHead(app.USUAL));
        male.setHeight(app.getRandomHeight(app.USUAL));
        male.setMouth(app.getRandomMouth(app.USUAL));
        male.setNose(app.getRandomNose(app.USUAL));
        male.setSkinColor(skinColor);

        male.generateView();

        Character female = new Character();
        female.setName(characterRepository.getRandomNameFemale());
        female.setSex("female");
        female.setRace(race);
        female.setLevel(0);

        female.setCareer(careerService.generateCareerForFounders());

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
        female.setSkinColor(skinColor);

        female.generateView();

        familyForm.setFounder(male);
        familyForm.setFoundress(female);

        return "game/addNewFamily";
    }

    @PostMapping("game/addNewFamily")
    public String addNewFamily(ModelMap model, @ModelAttribute("familyForm") @Valid FamilyForm familyForm, BindingResult result) {
        Family family = familyForm.getFamily();
        Character founder = familyForm.getFounder();
        Character foundress = familyForm.getFoundress();

        founder.generateView();
        foundress.generateView();

        User user = userRepository.findByUserName(getAuthUser().getUsername());
        List<Family> families = user.getFamilies();

        if (families.size() >= 5) {
            result.rejectValue("familyName", "familySize.alreadyExists");
            logger.info(user.getUserName() + " try to create family, but already has 5!");
        }

        for (Family existingFamily : families) {
            if (existingFamily.getFamilyName().equalsIgnoreCase(family.getFamilyName())) {
                result.rejectValue("familyName", "familyName.alreadyExists");
            }
        }

        if (result.hasErrors()) {
            return "game/addNewFamily";
        }

        for (Family existingFamily : families) {
            existingFamily.setCurrent(false);
            familyRepository.save(existingFamily);
        }

        family.setUser(user);
        family.setCurrent(true);
        family.setLevel(0);
        family.setMoney(1000);

        family.setHouse(houseRepository.findOne(1L));
        family.setFamilyResources(new FamilyResources("NewFamily"));
        family.setGameView(GameView.defView);

        logger.info(user.getUserName() + " adds new family:" + family.toString());
        familyRepository.save(family);

        founder.setFamily(family);

        Buff buff = buffRepository.findByTitle(Buff.SEVEN_CHILDREN);
        founder.getBuffs().add(buff);

        characterRepository.save(founder);

        foundress.setSpouse(founder);
        foundress.setFamily(familyRepository.findOne(1L));
        characterRepository.save(foundress);

        founder.setSpouse(foundress);
        characterRepository.save(founder);

        familyLogService.createNewFamilyLog(family, founder, foundress);

        craftService.newFamily(family);
        familyRepository.save(family);

        return "redirect:/game";

    }

    @RequestMapping(value = "/game/makeFamilyCurrent", method = RequestMethod.POST)
    public String makeFamilyCurrent(ModelMap model, RedirectAttributes redirectAttributes,
                                    @RequestParam(value = "familyId") Long familyId) {
        User user = userRepository.findByUserName(getAuthUser().getUsername());
        Family family = familyRepository.findOne(familyId);
        if (family != null) {
            if (user.getFamilies().contains(family)) {
                Family currentFamily = user.getCurrentFamily();
                currentFamily.setCurrent(false);
                family.setCurrent(true);
                familyRepository.save(currentFamily);
                familyRepository.save(family);
                redirectAttributes.addFlashAttribute("mess", "Теперь ваш текущий выбор семьи: " + family.getFamilyName());
            } else {
                redirectAttributes.addFlashAttribute("mess", "Это не ваша семья: " + family.getFamilyName());
            }
        } else {
            redirectAttributes.addFlashAttribute("mess", "Семья с таким id не найдена");
        }

        return "redirect:/game/families";
    }

    private UserDetails getAuthUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetail = (UserDetails) auth.getPrincipal();
        return userDetail;
    }
}
