package dyn.controllers;

/**
 * Created by OM on 17.02.2017.
 */


import dyn.model.Family;
import dyn.model.User;
import dyn.model.buildings.Building;
import dyn.repository.FamilyRepository;
import dyn.repository.UserRepository;
import dyn.repository.buildings.BuildingRepository;
import dyn.service.FamilyLogService;
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
public class TownController {
    private static final Logger logger = LogManager.getLogger(TownController.class);
    @Autowired
    BuildingRepository buildingRepository;
    @Autowired
    FamilyLogService familyLogService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private FamilyRepository familyRepository;

    @RequestMapping("/game/town")
    public String townView(ModelMap model) {
        User user = userRepository.findByUserName(getAuthUser().getUsername());
        Family family = user.getCurrentFamily();
        model.addAttribute("family", family);
        model.addAttribute("buildingList", buildingRepository.findAll());
        model.addAttribute("familyBuildingList", family.getBuildings());

        return "game/town";
    }

    @RequestMapping(value = "/game/buyBuilding", method = RequestMethod.POST)
    public String buyBuilding(ModelMap model, RedirectAttributes redirectAttributes,
                              @RequestParam(value = "buildingId") Long buildingId) {
        User user = userRepository.findByUserName(getAuthUser().getUsername());
        Family family = user.getCurrentFamily();

        Building building = buildingRepository.findOne(buildingId);
        if (building != null) {
            if (family.getMoney() >= building.getCost()) {
                family.getBuildings().add(building);
                family.setMoney(family.getMoney() - building.getCost());
                familyRepository.save(family);

                logger.info(family.logName() + "buy building: " + building.getName());
                String mess = "Вы купили здание '" + building.getName() + "'. Потрачено: " + building.getCost() + " р.";
                familyLogService.addToLog(family, mess);
                redirectAttributes.addFlashAttribute("mess", mess);
                return "redirect:/game/town";
            }
            logger.error(family.logName() + " not enough money to buy building: " + building.getName());
            redirectAttributes.addFlashAttribute("mess", "Недостаточно денег для покупки этого здания " + building.getName());
            return "redirect:/game/town";
        }
        logger.error(family.logName() + "want to buy non existing building: " + buildingId);
        redirectAttributes.addFlashAttribute("mess", "Такого здания не существует");
        return "redirect:/game/town";
    }

    @RequestMapping(value = "/game/familyBuildingView", method = RequestMethod.GET)
    public String familyBuildingView(ModelMap model, RedirectAttributes redirectAttributes,
                                     @RequestParam(value = "familyId") Long familyId,
                                     @RequestParam(value = "buildingId") Long buildingId) {
        User user = userRepository.findByUserName(getAuthUser().getUsername());
        Family family = user.getCurrentFamily();
        model.addAttribute("family", family);

        Family ownerFamily = familyRepository.findOne(familyId);
        Building building = buildingRepository.findOne(buildingId);

        if (ownerFamily != null && building != null && ownerFamily.getBuildings().contains(building)) {
            model.addAttribute("building", building);
            model.addAttribute("ownerFamily", ownerFamily);
            return "game/building";
        }
        logger.error(family.logName() + "want to see nonexisting building " + buildingId + " of family " + familyId + " or family doesn't own this building");
        redirectAttributes.addFlashAttribute("mess", "Нет такого здания или нет такой семьи или эта семья не владеет таким зданием");
        return "redirect:/game/town";
    }

    private UserDetails getAuthUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetail = (UserDetails) auth.getPrincipal();
        return userDetail;
    }
}
