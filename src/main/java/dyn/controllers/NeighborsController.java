package dyn.controllers;

import dyn.model.Family;
import dyn.model.User;
import dyn.model.UserNeighbor;
import dyn.repository.FamilyRepository;
import dyn.repository.UserRepository;
import dyn.service.UserNeighborService;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import static dyn.controllers.GameController.getAuthUser;

/**
 * Created by OM on 26.12.2017.
 */
@Controller
public class NeighborsController {
    private static final Logger logger = LogManager.getLogger(NeighborsController.class);
    @Autowired
    MessageSource messageSource;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private FamilyRepository familyRepository;
    @Autowired
    private UserNeighborService userNeighborService;

    @RequestMapping(value = "/game/addNeighbor", method = RequestMethod.POST)
    public String addNeighbor(ModelMap model, RedirectAttributes redirectAttributes,
                              @RequestParam(value = "neighborUserId") Long neighborUserId,
                              @RequestParam(value = "neighborFamilyId") Long neighborFamilyId) {
        User user = userRepository.findByUserName(getAuthUser().getUsername());
        Family family = user.getCurrentFamily();

        User neighborUser = userRepository.findOne(neighborUserId);
        Family neighborFamily = familyRepository.findByIdAndUser(neighborFamilyId, neighborUser);

        if (neighborUser != null && neighborFamily != null) {
            if (!userNeighborService.userHasThisNeighborLink(user, neighborFamily)) {
                userNeighborService.addNewNeighbor(user, neighborUser, neighborFamily);

                String mess = "Ссылка на семью " + neighborFamily.link() + " добавлена в список соседей";
                redirectAttributes.addFlashAttribute("mess", mess);
                return "redirect:/game/userview?userId=" + neighborUserId + "&familyId=" + neighborFamilyId;
            } else {
                logger.error(family.userNameAndFamilyName() + " try to add already added link of family: family id = " + neighborFamilyId);
                redirectAttributes.addFlashAttribute("mess", "Семья уже есть в вашем списке.");
                return "redirect:/game/userview?userId=" + neighborUserId + "&familyId=" + neighborFamilyId;
            }

        } else {
            logger.error(family.userNameAndFamilyName() + " try to add link of nonexistent family: family id = " + neighborFamilyId + ", user id = " + neighborUserId);
            redirectAttributes.addFlashAttribute("mess", "Семья не найдена.");
            return "redirect:/game/userview?userId=" + neighborUserId + "&familyId=" + neighborFamilyId;
        }
    }

    @RequestMapping(value = "/game/removeNeighbor", method = RequestMethod.POST)
    public String removeNeighbor(ModelMap model, RedirectAttributes redirectAttributes,
                                 @RequestParam(value = "neighborId") Long neighborId) {
        User user = userRepository.findByUserName(getAuthUser().getUsername());
        Family family = user.getCurrentFamily();

        UserNeighbor neighbor = userNeighborService.getNeighbor(neighborId);
        if (neighbor.getUser() == user) {
            String mess = "Ссылка на семью " + neighbor.getNeighborFamily().link() + " удалена из списка соседей";
            userNeighborService.removeNeighbor(neighbor);
            redirectAttributes.addFlashAttribute("mess", mess);
            return "redirect:/game/town";
        } else {
            logger.error(family.userNameAndFamilyName() + " try to remove not his link of neigbor family: neighbor id = " + neighborId);
            redirectAttributes.addFlashAttribute("mess", "Ссылка вам не принадлежит.");
            return "redirect:/game/town";
        }
    }
}
