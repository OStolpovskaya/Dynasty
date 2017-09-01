package dyn.controllers;

/**
 * Created by OM on 17.02.2017.
 */


import dyn.model.Family;
import dyn.model.House;
import dyn.model.Project;
import dyn.model.User;
import dyn.repository.FamilyRepository;
import dyn.repository.UserRepository;
import dyn.service.FamilyLogService;
import dyn.service.HouseService;
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
    FamilyLogService familyLogService;
    @Autowired
    HouseService houseService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private FamilyRepository familyRepository;

    @RequestMapping("/game/town")
    public String townView(ModelMap model) {
        User user = userRepository.findByUserName(getAuthUser().getUsername());
        Family family = user.getCurrentFamily();
        model.addAttribute("family", family);
        model.addAttribute("buildingList", houseService.getBuildingList());
        model.addAttribute("familyBuildingList", family.getBuildings());

        return "game/town";
    }

    @RequestMapping(value = "/game/buyBuilding", method = RequestMethod.POST)
    public String buyBuilding(ModelMap model, RedirectAttributes redirectAttributes,
                              @RequestParam(value = "buildingId") Long buildingId) {
        User user = userRepository.findByUserName(getAuthUser().getUsername());
        Family family = user.getCurrentFamily();

        House building = houseService.getHouse(buildingId);
        if (building != null) {
            if (!family.getBuildings().contains(building)) {
                if (family.getMoney() >= building.getCost()) {
                    family.getBuildings().add(building);
                    family.setMoney(family.getMoney() - building.getCost());

                    Project production = building.getProduction();
                    if (production != null) {
                        family.getCraftProjects().add(production);
                    }

                    familyRepository.save(family);

                    logger.info(family.familyNameAndId() + "buy building: " + building.getName());
                    String mess = "Вы купили здание '" + building.getName() + "'. Потрачено: " + building.getCost() + " д.";
                    familyLogService.addToLog(family, mess);
                    redirectAttributes.addFlashAttribute("mess", mess);
                    return "redirect:/game/town";
                }
                logger.error(family.familyNameAndId() + " not enough money to buy building: " + building.getName());
                redirectAttributes.addFlashAttribute("mess", "Недостаточно денег для покупки этого здания " + building.getName());
                return "redirect:/game/town";
            }
            logger.error(family.familyNameAndId() + "want to buy building, but already has one: " + building.getName());
            redirectAttributes.addFlashAttribute("mess", "Вы уже владеете таким зданием");
            return "redirect:/game/town";
        }
        logger.error(family.familyNameAndId() + "want to buy non existing building: " + buildingId);
        redirectAttributes.addFlashAttribute("mess", "Такого здания не существует");
        return "redirect:/game/town";
    }


    private UserDetails getAuthUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetail = (UserDetails) auth.getPrincipal();
        return userDetail;
    }
}
