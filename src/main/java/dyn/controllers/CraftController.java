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

        Iterable<CraftBranch> craftBranches = craftBranchRepository.findAll();
        for (CraftBranch craftBranch : craftBranches) {
            int maxCraftNumber = 0;
            for (Thing thing : family.getCraftThings()) {
                if (thing.getCraftBranch() == craftBranch && maxCraftNumber < thing.getCraftNumber()) {
                    maxCraftNumber = thing.getCraftNumber();
                }
            }
            List<Thing> things = craftBranch.getThings();
            for (Thing thing : things) {
                thing.familyStatus = 0; // не умеет производить предметы это го типа
                if (family.getCraftThings().contains(thing)) {
                    thing.familyStatus = 1; // умеет
                }
                if (thing.getCraftNumber() == maxCraftNumber + 1) {
                    thing.familyStatus = 2; // можно купить
                }
            }
        }
        model.addAttribute("craftBranches", craftBranches);

        return "game/craft";
    }

    @RequestMapping(value = "/game/learnThing", method = RequestMethod.POST)
    public String learnThing(ModelMap model, RedirectAttributes redirectAttributes,
                             @RequestParam(value = "thingId") Long thingId) {
        User user = userRepository.findByUserName(getAuthUser().getUsername());
        Family family = user.getCurrentFamily();

        Thing thing = craftService.getThing(thingId);
        if (thing != null) {
            CraftBranch craftBranch = thing.getCraftBranch();
            if (thing.getCraftNumber() == 1 || family.hasCraftThing(craftBranch, thing.getCraftNumber() - 1)) {
                if (family.getCraftPoint() >= craftBranch.getPoint()) {
                    family.setCraftPoint(family.getCraftPoint() - craftBranch.getPoint());
                    family.getCraftThings().add(thing);
                    logger.info("Family " + family.getFamilyName() + " learn thing " + thing.getName() + "(" + thing.getId() + ")");
                    familyRepository.save(family);
                    redirectAttributes.addFlashAttribute("mess", "Семья " + family.getFamilyName() + " выучила схему изготовления предмета " + thing.getName());
                    return "redirect:/game/craft";
                } else {
                    logger.error("Family " + family.getFamilyName() + " hasn't enough craft points to learn thing " + thing.getName() + "(" + thing.getId() + ")");
                    redirectAttributes.addFlashAttribute("mess", "Семья " + family.getFamilyName() + " не может выучить схему изготовления предмета " + thing.getName() + ". Недостаточно баллов развития. ");
                    return "redirect:/game/craft";
                }
            }
        }
        logger.error("Family " + family.getFamilyName() + " want to learn non-existing thing id=" + thing.getId());
        redirectAttributes.addFlashAttribute("mess", "Такого предмета нет");
        return "redirect:/game/craft";
    }

    @RequestMapping(value = "/game/chooseProject", method = RequestMethod.POST)
    public String chooseProject(ModelMap model, RedirectAttributes redirectAttributes,
                                @RequestParam(value = "thingId") Long thingId) {
        User user = userRepository.findByUserName(getAuthUser().getUsername());
        Family family = user.getCurrentFamily();

        Thing thing = craftService.getThing(thingId);//buyProject(family, thingId);
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
                model.addAttribute("family", family);
                model.addAttribute("projects", projects);
                model.addAttribute("thing", thing);
                return "game/chooseProject";
            } else {
                logger.error("Family " + family.getFamilyName() + " doesn't know the thing to buy projects for:" + thing.getName());
                redirectAttributes.addFlashAttribute("mess", "Ваша семья еще не умеет изготавливать такие предметы: " + thing.getName());
                return "redirect:/game/craft";
            }
        }
        logger.error("Family " + family.getFamilyName() + " want to buy projects for non-existing thing id=" + thingId);
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
                        logger.info("Family " + family.getFamilyName() + " buy project:" + project.getName());
                        redirectAttributes.addFlashAttribute("mess", "Ваша семья приобретает проект " + project.getName() + " предмета " + thing.getName() + ". Потрачено: " + project.getCost() + ". Время производить!");
                        return "redirect:/game/craft";
                    }
                    logger.error("Family " + family.getFamilyName() + " doesn't have enough money to buy project:" + project.getName());
                    redirectAttributes.addFlashAttribute("mess", "Ваша семья не может приобрести проект " + project.getName() + ". Недостаточно денег!");
                    return "redirect:/game/craft";
                }
                logger.error("Family " + family.getFamilyName() + " already has project " + project.getName());
                redirectAttributes.addFlashAttribute("mess", "Ваша семья еще владеет таким проектом: " + project.getName());
                return "redirect:/game/craft";
            }
            logger.error("Family " + family.getFamilyName() + " doesn't know the thing to buy projects:" + thing.getName());
            redirectAttributes.addFlashAttribute("mess", "Ваша семья еще не умеет изготавливать такие предметы: " + thing.getName());
            return "redirect:/game/craft";
        }
        logger.error("Family " + family.getFamilyName() + " want to buy non-existing project id=" + projectId);
        redirectAttributes.addFlashAttribute("mess", "Такого проекта нет");
        return "redirect:/game/craft";
    }

    private UserDetails getAuthUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetail = (UserDetails) auth.getPrincipal();
        return userDetail;
    }
}
