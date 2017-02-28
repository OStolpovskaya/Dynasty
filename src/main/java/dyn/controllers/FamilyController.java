package dyn.controllers;

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
            System.out.println("User doesn't have any family");
            return "redirect:/game/addNewFamily";
        } else {
            System.out.println("User has " + families.size() + " families");
            model.addAttribute("families", families);
        }
        return "game/families";
    }

    @GetMapping("game/addNewFamily")
    public String createNewFamily(ModelMap model) {
        model.addAttribute("familyForm", new Family());
        return "game/addNewFamily";
    }

    @PostMapping("game/addNewFamily")
    public String addNewFamily(ModelMap model, @ModelAttribute("familyForm") @Valid Family family, BindingResult result) {
        System.out.println("post add new family");
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
        System.out.println("SAVE:" + family.toString());
        familyRepository.save(family);

        Character male = new Character();
        male.setName(characterRepository.getRandomNameMale());
        male.setSex("male");
        male.setFamily(family);
        male.setLevel(0);
        male.setHeight(heightRepository.getRandomUsual());
        male.setHead(headRepository.getRandomUsual());
        male.setEyes(eyesRepository.getRandomUsual());
        male.setSkinColor(skinColorRepository.getRandomUsual());
        male.setRace(raceRepository.findByName("race.human"));

        male.generateView();
        characterRepository.save(male);

        Character female = new Character();
        female.setName(characterRepository.getRandomNameFemale());
        female.setSex("female");
        female.setSpouse(male);
        female.setLevel(0);
        female.setHeight(heightRepository.getRandom());
        female.setHead(headRepository.getRandom());
        female.setEyes(eyesRepository.getRandom());
        female.setSkinColor(skinColorRepository.getRandom());
        female.setRace(raceRepository.findByName("race.human"));

        female.generateView();
        characterRepository.save(female);

        male.setSpouse(female);
        characterRepository.save(male);

        return "redirect:/game";
    }


    private UserDetails getAuthUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetail = (UserDetails) auth.getPrincipal();
        System.out.println(userDetail);
        return userDetail;
    }
}
