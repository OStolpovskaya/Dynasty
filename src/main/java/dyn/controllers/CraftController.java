package dyn.controllers;

/**
 * Created by OM on 17.02.2017.
 */


import dyn.model.*;
import dyn.repository.CraftBranchRepository;
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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.List;

@Controller
public class CraftController {
    private static final Logger logger = LogManager.getLogger(CraftController.class);
    @Autowired
    CraftService craftService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private FamilyRepository familyRepository;

    @Autowired
    private CraftBranchRepository craftBranchRepository;

    @RequestMapping("/game/craft")
    public String craft(ModelMap model) {
        User user = userRepository.findByUserName(getAuthUser().getUsername());
        Family family = user.getCurrentFamily();
        model.addAttribute("family", family);
        model.addAttribute("parentThings", craftService.getThingsForTree());
        return "game/craft";
    }

    @RequestMapping(value = "/game/learnThing", method = RequestMethod.POST)
    public String learnThing(ModelMap model, RedirectAttributes redirectAttributes,
                             @RequestParam(value = "thingId") Long thingId) {
        User user = userRepository.findByUserName(getAuthUser().getUsername());
        Family family = user.getCurrentFamily();

        Thing thing = craftService.getThing(thingId);
        if (thing != null) {
            if (thing.getParent() == null || family.getCraftThings().contains(thing.getParent())) {
                if (family.getCraftPoint() >= thing.getCost()) {
                    family.setCraftPoint(family.getCraftPoint() - thing.getCost());
                    family.getCraftThings().add(thing);
                    logger.info(family.getLogName() + " learn thing " + thing.getName() + "(" + thing.getId() + ")");
                    familyRepository.save(family);
                    redirectAttributes.addFlashAttribute("mess", "Ваша семья выучила схему изготовления предмета " + thing.getName());
                    return "redirect:/game/craft";
                } else {
                    logger.error(family.getLogName() + " hasn't enough craft points to learn thing " + thing.getName() + "(" + thing.getId() + ")");
                    redirectAttributes.addFlashAttribute("mess", "Семья " + family.getFamilyName() + " не может выучить схему изготовления предмета " + thing.getName() + ". Недостаточно баллов развития. ");
                    return "redirect:/game/craft";
                }
            }
            logger.error(family.getLogName() + " want to learn thing without learning its parent id=" + thing.getId());
            redirectAttributes.addFlashAttribute("mess", "Сначала вам нужно изучить предмет-родитель.");
            return "redirect:/game/craft";
        }
        logger.error(family.getLogName() + " want to learn non-existing thing id=" + thing.getId());
        redirectAttributes.addFlashAttribute("mess", "Такого предмета нет");
        return "redirect:/game/craft";
    }


    @RequestMapping(value = "/game/chooseProject", method = RequestMethod.GET)
    public String chooseProject(ModelMap model, RedirectAttributes redirectAttributes,
                                @RequestParam(value = "thingId") Long thingId) {
        User user = userRepository.findByUserName(getAuthUser().getUsername());
        Family family = user.getCurrentFamily();
        model.addAttribute("family", family);

        Thing thing = craftService.getThing(thingId);
        if (thing != null) {
            if (family.getCraftThings().contains(thing)) {
                List<Project> availableProjects = thing.getProjects();
                List<Project> projects = new ArrayList<>();
                List<Project> familyCraftProjects = family.getCraftProjectsForThing(thing);
                for (Project availableProject : availableProjects) {
                    if (!familyCraftProjects.contains(availableProject)) {
                        projects.add(availableProject);
                    }
                }
                model.addAttribute("familyProjects", familyCraftProjects);
                model.addAttribute("projects", projects);
                model.addAttribute("thing", thing);
                return "game/chooseProject";
            } else {
                logger.error(family.getLogName() + " doesn't know the thing to buy projects for:" + thing.getName());
                redirectAttributes.addFlashAttribute("mess", "Ваша семья еще не умеет изготавливать такие предметы: " + thing.getName());
                return "redirect:/game/craft";
            }
        }
        logger.error(family.getLogName() + " want to buy projects for non-existing thing id=" + thingId);
        redirectAttributes.addFlashAttribute("mess", "Такого предмета нет");
        return "redirect:/game/craft";
    }

    @RequestMapping(value = "/game/buyProject", method = RequestMethod.POST)
    public String buyProject(ModelMap model, RedirectAttributes redirectAttributes,
                             @RequestParam(value = "projectId") Long projectId) {
        User user = userRepository.findByUserName(getAuthUser().getUsername());
        Family family = user.getCurrentFamily();

        Project project = craftService.getProject(projectId);
        if (project != null) {
            Thing thing = project.getThing();
            if (family.getCraftThings().contains(thing)) {
                if (!family.getCraftProjects().contains(project)) {
                    if (family.getMoney() >= project.getCost()) {
                        family.setMoney(family.getMoney() - project.getCost());
                        family.getCraftProjects().add(project);
                        familyRepository.save(family);
                        logger.info(family.getLogName() + " buy project: '" + project.getName() + "' for thing " + thing.getName());
                        redirectAttributes.addFlashAttribute("mess", "Ваша семья приобретает проект " + project.getName() + " предмета " + thing.getName() + ". Потрачено: " + project.getCost() + ". Время производить!");
                        return "redirect:/game/chooseProject?thingId=" + thing.getId();
                    }
                    logger.error(family.getLogName() + " doesn't have enough money to buy project: '" + project.getName() + "' for thing " + thing.getName());
                    redirectAttributes.addFlashAttribute("mess", "Ваша семья не может приобрести проект " + project.getName() + ". Недостаточно денег!");
                    return "redirect:/game/craft";
                }
                logger.error(family.getLogName() + " already has project " + project.getName());
                redirectAttributes.addFlashAttribute("mess", "Ваша семья уже владеет таким проектом: " + project.getName());
                return "redirect:/game/craft";
            }
            logger.error(family.getLogName() + " doesn't know the thing to buy projects:" + thing.getName());
            redirectAttributes.addFlashAttribute("mess", "Ваша семья еще не умеет изготавливать такие предметы: " + thing.getName());
            return "redirect:/game/craft";
        }
        logger.error(family.getLogName() + " want to buy non-existing project id=" + projectId);
        redirectAttributes.addFlashAttribute("mess", "Такого проекта нет");
        return "redirect:/game/craft";
    }

    @RequestMapping(value = "/game/makeItem", method = RequestMethod.POST)
    public String makeItem(ModelMap model, RedirectAttributes redirectAttributes,
                           @RequestParam(value = "projectId") Long projectId) {
        User user = userRepository.findByUserName(getAuthUser().getUsername());
        Family family = user.getCurrentFamily();

        Project project = craftService.getProject(projectId);
        if (project != null) {
            if (family.getCraftProjects().contains(project)) {
                if (family.hasResourcesForProject(project)) {
                    Item item = craftService.createItem(project, family);
                    family.getItems().add(item);
                    family.getFamilyResources().makingProject(project);
                    familyRepository.save(family);

                    logger.info(family.getLogName() + " makes the item for project:" + project.getName());
                    redirectAttributes.addFlashAttribute("mess", "Ваша семья изготавливает предмет по проекту '" + project.getName() + "' предмета " + project.getThing().getName() + ". Загляните на ваш склад!");
                    return "redirect:/game/chooseProject?thingId=" + project.getThing().getId();
                }
                logger.error(family.getLogName() + " doesn't have resources to make item for project:" + project.getName());
                redirectAttributes.addFlashAttribute("mess", "Недостаточно ресурсов для изготовления предмета по проекту: " + project.getName());
                return "redirect:/game/chooseProject?thingId=" + project.getThing().getId();
            }
            logger.error(family.getLogName() + " doesn't have the project:" + project.getName());
            redirectAttributes.addFlashAttribute("mess", "Ваша семья не владеет таким проектом: " + project.getName());
            return "redirect:/game/craft";
        }
        logger.error(family.getLogName() + " want to make item for non-existing project id=" + projectId);
        redirectAttributes.addFlashAttribute("mess", "Такого проекта нет");
        return "redirect:/game/craft";
    }

    @RequestMapping(value = "/game/buyResources", method = RequestMethod.POST)
    public String buyResources(ModelMap model, RedirectAttributes redirectAttributes,
                               @RequestParam(value = "res") String res,
                               @RequestParam(value = "thingId") String thingId) {
        User user = userRepository.findByUserName(getAuthUser().getUsername());
        Family family = user.getCurrentFamily();

        if (family.getMoney() >= FamilyResources.BUYCOST) {
            if (family.getFamilyResources().addRes(res, 5)) {
                family.setMoney(family.getMoney() - FamilyResources.BUYCOST);
                familyRepository.save(family);
                logger.info(family.getLogName() + " buy res: " + res);
                redirectAttributes.addFlashAttribute("mess", "Ресурсы добавлены");
                return "redirect:/game/chooseProject?thingId=" + thingId;
            } else {
                logger.error(family.getLogName() + " buy unknown res: " + res);
                redirectAttributes.addFlashAttribute("mess", "Такого ресурса нет");
                return "redirect:/game/chooseProject?thingId=" + thingId;
            }


        }
        logger.error(family.getLogName() + " doesn't have enough money to buy res: " + res);
        redirectAttributes.addFlashAttribute("mess", "Недостаточно денег для приобретения ресурсов");
        return "redirect:/game/chooseProject?thingId=" + thingId;
    }

    private UserDetails getAuthUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetail = (UserDetails) auth.getPrincipal();
        return userDetail;
    }
}
