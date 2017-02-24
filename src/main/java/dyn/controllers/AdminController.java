package dyn.controllers;

/**
 * Created by OM on 17.02.2017.
 */


import dyn.repository.AchievementRepository;
import dyn.repository.RaceRepository;
import dyn.repository.appearance.EyesRepository;
import dyn.repository.appearance.HeadRepository;
import dyn.repository.appearance.HeightRepository;
import dyn.repository.appearance.SkinColorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.HashMap;
import java.util.Map;

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

    @RequestMapping("/admin/random")
    public String achievements(ModelMap model, @RequestParam(value = "sex", required = false) String sex) {
        if (sex == null || sex.isEmpty()) {
            System.out.println("Sex is null or empty. Set it to male.");
            sex = "male";
        }

        if (!"female".equals(sex)) {
            sex = "male";
        }

        Map<String, String> map = new HashMap<String, String>();
        map.put("sex", sex);
        map.put("height", heightRepository.getRandom().getName());


        model.addAttribute("map", map);

        return "admin/random";
    }

}
