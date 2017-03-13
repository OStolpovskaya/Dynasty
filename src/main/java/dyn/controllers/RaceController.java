package dyn.controllers;

/**
 * Created by OM on 17.02.2017.
 */


import dyn.form.RaceAppearanceForm;
import dyn.model.Race;
import dyn.model.RaceAppearance;
import dyn.model.appearance.*;
import dyn.repository.RaceAppearanceRepository;
import dyn.repository.RaceRepository;
import dyn.service.AppearanceService;
import dyn.utils.CartesianIterator;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class RaceController {
    private static final Logger logger = LogManager.getLogger(RaceController.class);
    @Autowired
    AppearanceService app;
    @Autowired
    RaceRepository raceRepository;
    @Autowired
    RaceAppearanceRepository raceAppearanceRepository;

    @RequestMapping("/admin/race")
    public String race(ModelMap model, @ModelAttribute("raceAppearanceForm") RaceAppearanceForm form) {
        model.addAttribute("raceList", raceRepository.findAll());

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
        return "admin/race";
    }

    @RequestMapping(value = "/admin/generateRace", method = RequestMethod.POST)
    public String generateRace(ModelMap model,
                               @ModelAttribute("raceAppearanceForm") RaceAppearanceForm form,
                               BindingResult result,
                               RedirectAttributes redirectAttributes) {
        String username = getAuthUser().getUsername();
        form.fillEmptyLists();
        Race race;
        if (form.getRaceId() == 0L) {
            if (form.getTitle().isEmpty()) {
                result.reject("title", "Заполни название расы или выбери из уже существующих");
                return "admin/race";
            }
            race = new Race();
            race.setName(form.getTitle());
            logger.info(username + " creates new race: " + race);
            raceRepository.save(race);
        } else {
            race = raceRepository.findOne(form.getRaceId());
            logger.info(username + " deletes all RaceAppearances for race: " + race);
            raceAppearanceRepository.deleteByRace(race);
        }

        CartesianIterator it = new CartesianIterator(
                form.getBodyList(),
                form.getEarsList(),
                form.getEyebrowsList(),
                form.getEyeColorList(),
                form.getEyesList(),
                form.getHairColorList(),
                form.getHairTypeList(),
                form.getHeadList(),
                form.getHeightList(),
                form.getMouthList(),
                form.getNoseList(),
                form.getSkinColorList());
        while (it.hasNext()) {
            Object[] objects = it.next();
            RaceAppearance raceAppearance = new RaceAppearance();
            raceAppearance.setRace(race);
            raceAppearance.setBody((Body) objects[0]);
            raceAppearance.setEars((Ears) objects[1]);
            raceAppearance.setEyebrows((Eyebrows) objects[2]);
            raceAppearance.setEyeColor((EyeColor) objects[3]);
            raceAppearance.setEyes((Eyes) objects[4]);
            raceAppearance.setHairColor((HairColor) objects[5]);
            raceAppearance.setHairType((HairType) objects[6]);
            raceAppearance.setHead((Head) objects[7]);
            raceAppearance.setHeight((Height) objects[8]);
            raceAppearance.setMouth((Mouth) objects[9]);
            raceAppearance.setNose((Nose) objects[10]);
            raceAppearance.setSkinColor((SkinColor) objects[11]);

            logger.info(username + " creates new RaceAppearance: " + raceAppearance);
            raceAppearanceRepository.save(raceAppearance);
        }
        return "redirect:/admin/race";
    }

    private UserDetails getAuthUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetail = (UserDetails) auth.getPrincipal();
        return userDetail;
    }
}
