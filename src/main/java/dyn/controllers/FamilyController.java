package dyn.controllers;

import dyn.form.FamilyForm;
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

    @RequestMapping("game/families")
    public String families(ModelMap model) {
        User user = userRepository.findByUserName(getAuthUser().getUsername());
        List<Family> families = user.getFamilies();
        if (families.size() == 0) {
            logger.debug(user.getUserName() + " doesn't have any family");
            return "redirect:/game/addNewFamily";
        } else {
            model.addAttribute("families", families);
        }
        return "game/families";
    }

    @GetMapping("game/addNewFamily")
    public String createNewFamily(ModelMap model, @ModelAttribute("familyForm") FamilyForm familyForm) {

        Character male = new Character();
        male.setName(characterRepository.getRandomNameMale());
        male.setSex("male");
        male.setHeight(heightRepository.getRandomUsual());
        male.setHead(headRepository.getRandomUsual());
        male.setEyes(eyesRepository.getRandomUsual());
        male.setSkinColor(skinColorRepository.getRandomUsual());
        male.generateView();

        Character female = new Character();
        female.setName(characterRepository.getRandomNameFemale());
        female.setSex("female");
        female.setHeight(heightRepository.getRandomUsual());
        female.setHead(headRepository.getRandomUsual());
        female.setEyes(eyesRepository.getRandomUsual());
        female.setSkinColor(skinColorRepository.getRandomUsual());
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
        characterRepository.save(founder);

        foundress.setSpouse(founder);
        foundress.setFamily(null);
        foundress.setLevel(0);
        foundress.setRace(raceRepository.findByName("race.human"));
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
