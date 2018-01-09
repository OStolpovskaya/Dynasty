package dyn.controllers;

import dyn.model.Family;
import dyn.model.User;
import dyn.repository.UserRepository;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import static dyn.controllers.GameController.getAuthUser;
import static dyn.controllers.GameController.loc;

/**
 * Created by OM on 30.08.2017.
 */
@Controller
public class AdventureController {
    private static final Logger logger = LogManager.getLogger(AdventureController.class);
    @Autowired
    MessageSource messageSource;
    @Autowired
    private UserRepository userRepository;

    @RequestMapping("/game/adventures")
    public String adventures(ModelMap model, RedirectAttributes redirectAttributes) {
        User user = userRepository.findByUserName(getAuthUser().getUsername());

        Family family = user.getCurrentFamily();
        if (family == null) {
            logger.debug(user.getUserName() + " doesn't have any family");
            redirectAttributes.addFlashAttribute("mess", messageSource.getMessage("new.user", null, loc()));
            return "redirect:/game/addNewFamily";
        }
        model.addAttribute("family", family);

        return "game/adventures";
    }

}
