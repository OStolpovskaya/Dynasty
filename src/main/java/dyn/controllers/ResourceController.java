package dyn.controllers;

import dyn.model.Family;
import dyn.model.User;
import dyn.repository.FamilyRepository;
import dyn.repository.UserRepository;
import dyn.utils.ResUtils;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Objects;

import static dyn.controllers.GameController.getAuthUser;

/**
 * Created by OM on 02.10.2017.
 */
@Controller
public class ResourceController {
    private static final Logger logger = LogManager.getLogger(ResourceController.class);

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private FamilyRepository familyRepository;

    @RequestMapping("/game/resExchange")
    public String resExchange(ModelMap model, RedirectAttributes redirectAttributes) {
        User user = userRepository.findByUserName(getAuthUser().getUsername());

        Family family = user.getCurrentFamily();
        model.addAttribute("family", family);

        return "game/resExchange";
    }

    @RequestMapping(value = "/game/exchangeResources", method = RequestMethod.POST)
    public String exchangeResources(ModelMap model, RedirectAttributes redirectAttributes,
                                    @RequestParam(value = "drop") String dropRes,
                                    @RequestParam(value = "take") String takeRes,
                                    @RequestParam(value = "count") int count) {
        User user = userRepository.findByUserName(getAuthUser().getUsername());
        Family family = user.getCurrentFamily();

        String mess = "Error";

        String dropResName = ResUtils.getName(dropRes);
        String takeResName = ResUtils.getName(takeRes);

        if (!Objects.equals(dropResName, "") && !Objects.equals(takeResName, "")) {
            if (!dropResName.equals(takeResName)) {
                int dropResCount = family.getFamilyResources().getResByName(dropRes);
                if (10 <= count && count <= dropResCount) {
                    int floorCount = (int) (Math.floor(count / 10) * 10);
                    int newResAmount = floorCount / 10;
                    family.getFamilyResources().removeResByName(dropRes, floorCount);
                    family.getFamilyResources().addResByName(takeRes, newResAmount);
                    familyRepository.save(family);
                    logger.info(family.familyNameAndUserName() + " exchanges " + floorCount + " of resource " + dropRes + " to " + newResAmount + " of resource " + takeRes);
                    mess = "Вы обменяли " + floorCount + " шт. ресурса " + dropResName + " на " + newResAmount + " шт. ресурса " + takeResName;
                } else {
                    logger.error(family.familyNameAndUserName() + " not enough resources to exchange:" + dropRes);
                    mess = "Выбранного для обмена ресурса должно быть больше 10, но меньше или равно, чем есть у вас";
                }
            } else {
                logger.debug(family.familyNameAndUserName() + " try to exchange the same resources:" + dropRes);
                mess = "Невыгодно обменивать один и тот же ресурс.";
            }
        } else {
            logger.error(family.familyNameAndUserName() + " try to exchange nonexisting resource:" + dropRes);
            mess = "Нет такого ресурса.";
        }


        redirectAttributes.addFlashAttribute("mess", mess);
        return "redirect:/game/resExchange";
    }
}
