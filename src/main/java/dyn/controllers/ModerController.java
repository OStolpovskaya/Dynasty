package dyn.controllers;

/**
 * Created by OM on 17.02.2017.
 */


import dyn.model.*;
import dyn.repository.FamilyRepository;
import dyn.repository.FeedbackRepository;
import dyn.repository.UserRepository;
import dyn.service.CraftService;
import dyn.service.TownNewsService;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class ModerController {
    private static final Logger logger = LogManager.getLogger(ModerController.class);

    @Autowired
    CraftService craftService;
    @Autowired
    FeedbackRepository feedbackRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private FamilyRepository familyRepository;
    @Autowired
    private TownNewsService townNewsService;

    @RequestMapping("/moder")
    public String admin(ModelMap model) {
        return "moder";
    }

    @RequestMapping("/admin/projects")
    public String projects(ModelMap model) {
        model.addAttribute("newProjects", craftService.getProjectsByStatus(ProjectStatus.newProject));
        model.addAttribute("reworkProjects", craftService.getProjectsByStatus(ProjectStatus.rework));
        model.addAttribute("correctedProjects", craftService.getProjectsByStatus(ProjectStatus.corrected));
        return "admin/projects";
    }

    @RequestMapping("/admin/feedbacks")
    public String feedbacks(ModelMap model) {
        model.addAttribute("bugs", feedbackRepository.findByTypeOrderByStatusAscDateDesc(FeedbackType.bug));
        model.addAttribute("complaints", feedbackRepository.findByTypeOrderByStatusAscDateDesc(FeedbackType.complaint));
        model.addAttribute("suggestions", feedbackRepository.findByTypeOrderByStatusAscDateDesc(FeedbackType.suggestion));
        return "admin/feedbacks";
    }

    @RequestMapping(value = "/admin/setProjectApproved", method = RequestMethod.POST)
    public String setNewProjectApproved(ModelMap model,
                                        @RequestParam(value = "projectId") Long projectId) {
        User user = userRepository.findByUserName(getAuthUser().getUsername());

        Project project = craftService.getProject(projectId);
        if (project != null) {
            project.setStatus(ProjectStatus.approved);
            craftService.saveProject(project);

            Family author = project.getAuthor();
            author.getCraftProjects().add(project);
            familyRepository.save(author);

            logger.info("Moderator " + user.getUserName() + " set status approved for project: " + project);
            townNewsService.addCommonNews(author, "Семья " + author.link() + " создала авторский проект " + project.getName() + " для предмета " + project.getThing().getName());
            return "redirect:/admin/projects";
        }
        logger.error("Moderator " + user.getUserName() + " want to set status approved for non-existing project id=" + projectId);
        return "redirect:/admin/projects";
    }

    @RequestMapping(value = "/admin/setProjectRework", method = RequestMethod.POST)
    public String setNewProjectRework(ModelMap model,
                                      @RequestParam(value = "projectId") Long projectId,
                                      @RequestParam(value = "reason") String reason) {
        User user = userRepository.findByUserName(getAuthUser().getUsername());

        Project project = craftService.getProject(projectId);
        if (project != null) {
            project.setStatus(ProjectStatus.rework);
            project.setStatusMessage(reason);
            craftService.saveProject(project);

            logger.info("Moderator " + user.getUserName() + " set status rework for project: " + project);
            return "redirect:/admin/projects";
        }
        logger.error("Moderator " + user.getUserName() + " want to set status rework for non-existing project id=" + projectId);
        return "redirect:/admin/projects";
    }

    // /admin/changeFeedbackStatus
    @PostMapping("/admin/changeFeedbackStatus")
    public String changeFeedbackStatus(ModelMap model,
                                       @RequestParam(value = "feedbackId") Long feedbackId,
                                       @RequestParam(value = "feedbackStatus") FeedbackStatus feedbackStatus,
                                       RedirectAttributes redirectAttributes) {
        User user = userRepository.findByUserName(getAuthUser().getUsername());
        Feedback feedback = feedbackRepository.findOne(feedbackId);
        if (feedback != null) {
            feedback.setStatus(feedbackStatus);
            feedbackRepository.save(feedback);
            logger.info("Moderator " + user.getUserName() + " set status for feedback: " + feedbackId);
            redirectAttributes.addFlashAttribute("mess", "Статус заявки обновлен: " + feedbackStatus);

        } else {
            logger.error("Moderator " + user.getUserName() + " want to set status for non-existing feedback id=" + feedbackId);
            redirectAttributes.addFlashAttribute("mess", "Неверный id: " + feedbackId);
        }
        return "redirect:/admin/feedbacks";
    }

    private UserDetails getAuthUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return (UserDetails) auth.getPrincipal();
    }

}
