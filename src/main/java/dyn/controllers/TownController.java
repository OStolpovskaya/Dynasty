package dyn.controllers;

/**
 * Created by OM on 17.02.2017.
 */


import dyn.model.*;
import dyn.repository.FamilyRepository;
import dyn.repository.UserRepository;
import dyn.service.*;
import dyn.utils.MailCounter;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Map;

import static dyn.controllers.GameController.loc;

@Controller
public class TownController {
    private static final Logger logger = LogManager.getLogger(TownController.class);
    @Autowired
    FamilyLogService familyLogService;
    @Autowired
    HouseService houseService;
    @Autowired
    CraftService craftService;
    @Autowired
    MessageSource messageSource;
    @Autowired
    UserNeighborService userNeighborService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private FamilyRepository familyRepository;
    @Autowired
    private MailService mailService;
    @Autowired
    private TownNewsService townNewsService;

    @RequestMapping("/game/town")
    public String townView(ModelMap model, RedirectAttributes redirectAttributes) {
        User user = userRepository.findByUserName(getAuthUser().getUsername());
        Family family = user.getCurrentFamily();

        if (family == null) {
            logger.debug(user.getUserName() + " doesn't have any family");
            redirectAttributes.addFlashAttribute("mess", messageSource.getMessage("new.user", null, loc()));
            return "redirect:/game/addNewFamily";
        }

        model.addAttribute("family", family);
        model.addAttribute("buildingList", houseService.getBuildingList());
        model.addAttribute("familyBuildingList", family.getBuildings());

        List<UserNeighbor> neighborsOfUser = userNeighborService.getNeighborsOfUser(user);
        model.addAttribute("neighborsOfUser", neighborsOfUser);

        Map<User, MailCounter> userMailCounterMap = mailService.countMailOfUser(user);
        model.addAttribute("userMailCounterMap", userMailCounterMap);

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

                    logger.info(family.userNameAndFamilyName() + "buy building: " + building.getName());
                    String mess = "Вы купили здание '" + building.getName() + "'. Потрачено: " + building.getCost() + " д.";
                    familyLogService.addToLog(family, mess);
                    townNewsService.addNewBuildingNews(family, building);
                    redirectAttributes.addFlashAttribute("mess", mess);
                    return "redirect:/game/buildings";
                }
                logger.error(family.userNameAndFamilyName() + " not enough money to buy building: " + building.getName());
                redirectAttributes.addFlashAttribute("mess", "Недостаточно денег для покупки этого здания " + building.getName());
                return "redirect:/game/town";
            }
            logger.error(family.userNameAndFamilyName() + "want to buy building, but already has one: " + building.getName());
            redirectAttributes.addFlashAttribute("mess", "Вы уже владеете таким зданием");
            return "redirect:/game/town";
        }
        logger.error(family.userNameAndFamilyName() + "want to buy non existing building: " + buildingId);
        redirectAttributes.addFlashAttribute("mess", "Такого здания не существует");
        return "redirect:/game/town";
    }


    private UserDetails getAuthUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetail = (UserDetails) auth.getPrincipal();
        return userDetail;
    }
}
