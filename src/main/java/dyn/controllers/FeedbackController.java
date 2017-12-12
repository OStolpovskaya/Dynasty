package dyn.controllers;

import dyn.model.*;
import dyn.repository.FeedbackRepository;
import dyn.repository.UserRepository;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import static dyn.controllers.GameController.getAuthUser;

/**
 * Created by OM on 30.08.2017.
 */
@Controller
public class FeedbackController {
    private static final Logger logger = LogManager.getLogger(FeedbackController.class);
    @Autowired
    FeedbackRepository feedbackRepository;
    @Autowired
    private UserRepository userRepository;

    @RequestMapping("/game/feedback")
    public String house(ModelMap model, RedirectAttributes redirectAttributes) {
        User user = userRepository.findByUserName(getAuthUser().getUsername());

        Family family = user.getCurrentFamily();
        model.addAttribute("family", family);

        model.addAttribute("bugs", feedbackRepository.findByTypeOrderByStatusAscDateDesc(FeedbackType.bug));
        model.addAttribute("complaints", feedbackRepository.findByTypeOrderByStatusAscDateDesc(FeedbackType.complaint));
        model.addAttribute("suggestions", feedbackRepository.findByTypeOrderByStatusAscDateDesc(FeedbackType.suggestion));

        model.addAttribute("newFeedback", new Feedback());

        return "game/feedback";
    }

    @PostMapping("/game/addFeedback")
    public String addFeedback(ModelMap model, @ModelAttribute("newFeedback") @Valid Feedback newFeedback, BindingResult result, HttpServletRequest request, RedirectAttributes redirectAttributes) {
        User user = userRepository.findByUserName(getAuthUser().getUsername());
        Family family = user.getCurrentFamily();

        if (result.hasErrors()) {
            logger.error(family.familyNameAndUserName() + " try to add feedback, but form returned with binding errors: " + result.getSuppressedFields());

            model.addAttribute("family", family);

            model.addAttribute("bugs", feedbackRepository.findByTypeOrderByStatusAscDateDesc(FeedbackType.bug));
            model.addAttribute("complaints", feedbackRepository.findByTypeOrderByStatusAscDateDesc(FeedbackType.complaint));
            model.addAttribute("suggestions", feedbackRepository.findByTypeOrderByStatusAscDateDesc(FeedbackType.suggestion));

            return "game/feedback";
        }

        newFeedback.setStatus(FeedbackStatus.submitted);
        feedbackRepository.save(newFeedback);

        logger.info(family.familyNameAndUserName() + " submitted new feedback '" + newFeedback.getType() + "': " + newFeedback.getId());
        redirectAttributes.addFlashAttribute("mess", "Ваша заявка зарегистрирована!");

        return "redirect:/game/feedback";
    }
}
