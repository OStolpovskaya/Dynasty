package dyn.controllers;

/**
 * Created by OM on 17.02.2017.
 */


import dyn.model.Family;
import dyn.model.Project;
import dyn.model.ProjectStatus;
import dyn.model.User;
import dyn.repository.FamilyRepository;
import dyn.repository.UserRepository;
import dyn.service.CraftService;
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

@Controller
public class ModerController {
    private static final Logger logger = LogManager.getLogger(ModerController.class);

    @Autowired
    CraftService craftService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private FamilyRepository familyRepository;

    @RequestMapping("/moder")
    public String admin(ModelMap model) {
        return "moder";
    }

    @RequestMapping("/moder/projects")
    public String projects(ModelMap model) {
        model.addAttribute("newProjects", craftService.getProjectsByStatus(ProjectStatus.newProject));
        model.addAttribute("reworkProjects", craftService.getProjectsByStatus(ProjectStatus.rework));
        model.addAttribute("correctedProjects", craftService.getProjectsByStatus(ProjectStatus.corrected));
        return "moder/projects";
    }

    @RequestMapping(value = "/moder/setProjectApproved", method = RequestMethod.POST)
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

            logger.info(user.getUserName() + " set status approved for project: " + project);
            return "redirect:/moder/projects";
        }
        logger.error(user.getUserName() + " want to set status approved for non-existing project id=" + projectId);
        return "redirect:/moder/projects";
    }

    @RequestMapping(value = "/moder/setProjectRework", method = RequestMethod.POST)
    public String setNewProjectRework(ModelMap model,
                                      @RequestParam(value = "projectId") Long projectId,
                                      @RequestParam(value = "reason") String reason) {
        User user = userRepository.findByUserName(getAuthUser().getUsername());

        Project project = craftService.getProject(projectId);
        if (project != null) {
            project.setStatus(ProjectStatus.rework);
            project.setStatusMessage(reason);
            craftService.saveProject(project);

            logger.info(user.getUserName() + " set status rework for project: " + project);
            return "redirect:/moder/projects";
        }
        logger.error(user.getUserName() + " want to set status rework for non-existing project id=" + projectId);
        return "redirect:/moder/projects";
    }


    private UserDetails getAuthUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return (UserDetails) auth.getPrincipal();
    }

}
