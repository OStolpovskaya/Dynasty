package dyn.controllers;

import dyn.model.Family;
import dyn.model.Mail;
import dyn.model.MailStatus;
import dyn.model.User;
import dyn.repository.FamilyRepository;
import dyn.repository.UserRepository;
import dyn.service.MailService;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.List;

import static dyn.controllers.GameController.getAuthUser;
import static dyn.controllers.GameController.loc;

/**
 * Created by OM on 26.12.2017.
 */
@Controller
public class MailController {
    private static final Logger logger = LogManager.getLogger(MailController.class);
    @Autowired
    MessageSource messageSource;
    @Autowired
    MailService mailService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private FamilyRepository familyRepository;

    //mail
    @RequestMapping("/game/mail")
    public String mail(ModelMap model, RedirectAttributes redirectAttributes,
                       @RequestParam(value = "chatWith") Long chatWith) {
        User user = userRepository.findByUserName(getAuthUser().getUsername());
        Family family = user.getCurrentFamily();

        if (family == null) {
            logger.debug(user.getUserName() + " doesn't have any family");
            redirectAttributes.addFlashAttribute("mess", messageSource.getMessage("new.user", null, loc()));
            return "redirect:/game/addNewFamily";
        }

        model.addAttribute("family", family);

        User player = userRepository.findOne(chatWith);
        if (player != null) {
            model.addAttribute("newMail", new Mail());
            getChatView(model, user, player);
            return "game/mail";
        }
        logger.error(family.userNameAndFamilyName() + "want to chat with nonexistent user: " + chatWith);
        redirectAttributes.addFlashAttribute("mess", "Такого игрока нет");
        return "redirect:/game/town";
    }

    private void getChatView(ModelMap model, User user, User player) {
        List<Mail> readMailList = mailService.showReadChat(user, player);
        List<Mail> newMailList = mailService.showUnreadChat(user, player);

        model.addAttribute("newMailList", newMailList);
        model.addAttribute("readMailList", readMailList);
        model.addAttribute("player", player);
    }

    @PostMapping("/game/addMail")
    public String addMail(ModelMap model, @ModelAttribute(value = "newMail") @Valid Mail newMail, BindingResult result,
                          @RequestParam(value = "playerId") Long playerId, RedirectAttributes redirectAttributes) {
        User user = userRepository.findByUserName(getAuthUser().getUsername());
        Family family = user.getCurrentFamily();
        if (result.hasErrors()) {
            logger.error(family.userNameAndFamilyName() + " try to add mail, but form returned with binding errors: " + result.getSuppressedFields());
            model.addAttribute("family", family);
            model.addAttribute("chatWith", playerId);
            getChatView(model, user, userRepository.findOne(playerId));
            return "game/mail";
        }
        User player = userRepository.findOne(playerId);
        if (player != null) {
            newMail.setFrom(user);
            newMail.setTo(player);
            newMail.setStatus(MailStatus.unread);
            mailService.save(newMail);
            return "redirect:/game/mail?chatWith=" + player.getUserid();
        }
        logger.error(family.userNameAndFamilyName() + "want to chat with nonexistent user: " + playerId);
        redirectAttributes.addFlashAttribute("mess", "Такого игрока нет");
        return "redirect:/game/town";
    }
}
