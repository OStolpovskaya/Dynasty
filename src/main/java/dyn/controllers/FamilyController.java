package dyn.controllers;

import dyn.form.FamilyForm;
import dyn.model.Buff;
import dyn.model.Character;
import dyn.model.Family;
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
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;
import java.util.List;

/**
 * Created by OM on 01.03.2017.
 */
@Controller
public class FamilyController {
    private static final Logger logger = LogManager.getLogger(FamilyController.class);
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
    @Autowired
    private BuffRepository buffRepository;

    @RequestMapping("game/families")
    public String families(ModelMap model) {
        User user = userRepository.findByUserName(getAuthUser().getUsername());
        List<Family> families = user.getFamilies();
        if (families.size() == 0) {
            logger.debug(user.getUserName() + " doesn't have any family");
            return "redirect:/game/addNewFamily";
        } else {
            model.addAttribute("families", families);
            model.addAttribute("levels", user.getCurrentFamily().getLevelOrderedFathers());
        }
        return "game/families";
    }

    @GetMapping("game/addNewFamily")
    public String createNewFamily(ModelMap model, @ModelAttribute("familyForm") FamilyForm familyForm) {

        Character male = new Character();
        male.setName(characterRepository.getRandomNameMale());
        male.setSex("male");

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
        male.setSkinColor(app.getRandomSkinColor(app.USUAL));

        male.generateView();

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
        family.setMoney(100);
        logger.info(user.getUserName() + " adds new family:" + family.toString());
        familyRepository.save(family);

        founder.setFamily(family);
        founder.setLevel(0);
        founder.setRace(raceRepository.findByName("race.human"));

        Buff buff = buffRepository.findByTitle(Buff.FIVE_CHILDREN);
        founder.getBuffs().add(buff);

        logger.debug(founder.toString());
        characterRepository.save(founder);

        foundress.setSpouse(founder);
        foundress.setFamily(null);
        foundress.setLevel(0);
        foundress.setRace(raceRepository.findByName("race.human"));
        logger.debug(foundress.toString());
        characterRepository.save(foundress);

        founder.setSpouse(foundress);
        characterRepository.save(founder);

        return "redirect:/game";

    }


    private UserDetails getAuthUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetail = (UserDetails) auth.getPrincipal();
        return userDetail;
    }
}
