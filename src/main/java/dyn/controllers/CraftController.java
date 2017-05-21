package dyn.controllers;

/**
 * Created by OM on 17.02.2017.
 */


import dyn.model.*;
import dyn.repository.CraftBranchRepository;
import dyn.repository.FamilyRepository;
import dyn.repository.UserRepository;
import dyn.service.Const;
import dyn.service.CraftService;
import dyn.service.FamilyLogService;
import dyn.service.HouseService;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.imageio.ImageIO;
import javax.validation.Valid;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Controller
public class CraftController {
    private static final Logger logger = LogManager.getLogger(CraftController.class);
    @Autowired
    CraftService craftService;

    @Autowired
    HouseService houseService;

    @Autowired
    FamilyLogService familyLogService;

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
        model.addAttribute("craftBranchList", craftService.getCraftBranches());
        model.addAttribute("parentThings", craftService.getThingsForTree());
        model.addAttribute("authorProjects", craftService.getAuthorProjects(family));

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
                    familyRepository.save(family);

                    logger.info(family.familyNameAndId() + " learn thing " + thing.getName() + "(" + thing.getId() + ")");
                    String mess = "Ваша семья выучила схему изготовления предмета " + thing.getName() + ". Использовано: " + thing.getCost() + " б.";
                    familyLogService.addToLog(family, mess);
                    redirectAttributes.addFlashAttribute("mess", mess);
                    return "redirect:/game/craft#craftbranch" + thing.getCraftBranch().getId();
                } else {
                    logger.error(family.familyNameAndId() + " hasn't enough craft points to learn thing " + thing.getName() + "(" + thing.getId() + ")");
                    redirectAttributes.addFlashAttribute("mess", "Семья " + family.getFamilyName() + " не может выучить схему изготовления предмета " + thing.getName() + ". Недостаточно баллов развития. ");
                    return "redirect:/game/craft";
                }
            }
            logger.error(family.familyNameAndId() + " want to learn thing without learning its parent id=" + thing.getId());
            redirectAttributes.addFlashAttribute("mess", "Сначала вам нужно изучить предмет-родитель.");
            return "redirect:/game/craft";
        }
        logger.error(family.familyNameAndId() + " want to learn non-existing thing id=" + thing.getId());
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
                List<Project> availableProjects = craftService.getApprovedProjectsByThing(thing);//thing.getProjects();
                List<Project> projectsToBuy = new ArrayList<>();
                List<FamilyProject> familyCraftProjects = craftService.getFamilyProjectsForThing(family, thing);//
                List<Project> haveProjects = family.getCraftProjectsForThing(thing);
                for (Project availableProject : availableProjects) {
                    if (!haveProjects.contains(availableProject)) {
                        projectsToBuy.add(availableProject);
                    }
                }
                model.addAttribute("familyProjects", familyCraftProjects);
                model.addAttribute("projects", projectsToBuy);
                model.addAttribute("thing", thing);
                return "game/chooseProject";
            } else {
                logger.error(family.familyNameAndId() + " doesn't know the thing to buy projects for:" + thing.getName());
                redirectAttributes.addFlashAttribute("mess", "Ваша семья еще не умеет изготавливать такие предметы: " + thing.getName());
                return "redirect:/game/craft";
            }
        }
        logger.error(family.familyNameAndId() + " want to buy projects for non-existing thing id=" + thingId);
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

                        Family author = project.getAuthor();
                        author.setMoney(author.getMoney() + project.getCost());
                        familyRepository.save(author);
                        familyLogService.addToLog(author, "Семья " + family.getFamilyName() + " приобрела ваш проект " + project.getName() + ". Получено: " + project.getCost() + " р.");

                        String mess = "Ваша семья приобрела проект '" + project.getName() + "' предмета '" + thing.getName() + "'. Потрачено: " + project.getCost() + " р.";
                        familyLogService.addToLog(family, mess);
                        redirectAttributes.addFlashAttribute("mess", mess + " Время производить!");
                        logger.info(family.familyNameAndId() + " buy project: '" + project.getName() + "' for thing " + thing.getName());
                        return "redirect:/game/chooseProject?thingId=" + thing.getId();
                    }
                    logger.error(family.familyNameAndId() + " doesn't have enough money to buy project: '" + project.getName() + "' for thing " + thing.getName());
                    redirectAttributes.addFlashAttribute("mess", "Ваша семья не может приобрести проект " + project.getName() + ". Недостаточно денег!");
                    return "redirect:/game/craft";
                }
                logger.error(family.familyNameAndId() + " already has project " + project.getName());
                redirectAttributes.addFlashAttribute("mess", "Ваша семья уже владеет таким проектом: " + project.getName());
                return "redirect:/game/craft";
            }
            logger.error(family.familyNameAndId() + " doesn't know the thing to buy projects:" + thing.getName());
            redirectAttributes.addFlashAttribute("mess", "Ваша семья еще не умеет изготавливать такие предметы: " + thing.getName());
            return "redirect:/game/craft";
        }
        logger.error(family.familyNameAndId() + " want to buy non-existing project id=" + projectId);
        redirectAttributes.addFlashAttribute("mess", "Такого проекта нет");
        return "redirect:/game/craft";
    }

    @RequestMapping(value = "/game/editProject", method = RequestMethod.POST)
    public String editProject(ModelMap model, RedirectAttributes redirectAttributes,
                              @RequestParam(value = "projectId") Long projectId) {
        User user = userRepository.findByUserName(getAuthUser().getUsername());
        Family family = user.getCurrentFamily();
        model.addAttribute("family", family);

        Project project = craftService.getProject(projectId);
        if (project != null) {
            model.addAttribute("thing", project.getThing());
            model.addAttribute("project", project);
            model.addAttribute("formType", "edit");
            return "game/projectForm";
        }
        logger.error(family.familyNameAndId() + " want to edit non-existing project id=" + projectId);
        redirectAttributes.addFlashAttribute("mess", "Такого проекта нет");
        return "redirect:/game/craft";
    }

    @RequestMapping(value = "/game/newProject", method = RequestMethod.POST)
    public String newProject(ModelMap model, RedirectAttributes redirectAttributes,
                             @RequestParam(value = "newProjectThingId") Long thingId) {
        User user = userRepository.findByUserName(getAuthUser().getUsername());
        Family family = user.getCurrentFamily();
        model.addAttribute("family", family);

        Thing thing = craftService.getThing(thingId);
        if (thing != null) {
            Project project = new Project();
            project.setThing(thing);
            project.setAuthor(family);
            model.addAttribute("thing", thing);
            model.addAttribute("project", project);
            model.addAttribute("formType", "new");
            return "game/projectForm";
        }

        logger.error(family.familyNameAndId() + " want to create project for non-existing thing id=" + thingId);
        redirectAttributes.addFlashAttribute("mess", "Такой вещи нет");
        return "redirect:/game/craft";
    }


    @RequestMapping(value = "/game/saveProject", method = RequestMethod.POST)
    public String saveProject(ModelMap model, @ModelAttribute("project") @Valid Project project,
                              BindingResult result,
                              RedirectAttributes redirectAttributes,
                              @RequestParam(value = "formType") String formType,
                              @RequestParam(value = "newProjectFile") MultipartFile file) {
        User user = userRepository.findByUserName(getAuthUser().getUsername());
        Family family = user.getCurrentFamily();

        System.out.println("CraftController.saveProject");
        System.out.println("model = " + model);
        System.out.println("project = " + project);
        System.out.println("formType = " + formType);
        System.out.println("file.getOriginalFilename() = " + file.getOriginalFilename());

        Thing thing = project.getThing();
        BufferedImage image = null;
        byte[] imageInByte = null;

        if (file.isEmpty() || file.getSize() == 0) {
            System.out.println("file.isEmpty() = " + file.isEmpty());
            System.out.println("file.getSize() = " + file.getSize());
            logger.error(family.familyNameAndId() + " file is empty or has size 0 ");
            result.rejectValue("view", "project.fileSize");
        } else {
            if (!file.getContentType().equalsIgnoreCase("image/png")) {
                System.out.println("file.getContentType() = " + file.getContentType());
                logger.error(family.familyNameAndId() + " file is not png ");
                result.rejectValue("view", "project.fileType");

            } else {
                try {
                    image = ImageIO.read(file.getInputStream());
                    int width = image.getWidth();
                    int height = image.getHeight();
                    if (width != thing.getWidth() || height != thing.getHeight()) {
                        logger.error(family.familyNameAndId() + " uploaded image for new project with incorrect size " + file.getOriginalFilename());
                        result.rejectValue("view", "project.imageSize");
                    } else {
                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        ImageIO.write(image, "png", baos);
                        baos.flush();
                        imageInByte = baos.toByteArray();
                        baos.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    logger.error(family.familyNameAndId() + " has error in reading uploaded file " + file.getOriginalFilename());
                    result.rejectValue("view", "project.imageReadError");
                }
            }
        }

        if (formType.equals("new") && family.getMoney() < Const.COST_NEW_PROJECT) {
            logger.error(family.familyNameAndId() + " has not enough money to create project for thing " + thing.getName());
            result.rejectValue("cost", "project.notEnoughMoney");
        }

        if (result.hasErrors()) {
            System.out.println("result.hasErrors() = " + result.hasErrors());
            System.out.println(result);
            model.addAttribute("family", family);
            model.addAttribute("formType", formType);
            return "game/projectForm";
        }

        project.setView(imageInByte);

        if (formType.equals("new")) {
            project.setStatus(ProjectStatus.newProject);
            project.setStatusMessage("");
        }

        if (formType.equals("edit")) {
            project.setStatus(ProjectStatus.corrected);
            project.setStatusMessage(project.getStatusMessage());
        }

        craftService.saveProject(project);

        if (formType.equals("new")) {
            family.setMoney(family.getMoney() - Const.COST_NEW_PROJECT);
            familyRepository.save(family);
        }

        logger.info(family.familyNameAndId() + " saved project " + project.getName());

        if (formType.equals("edit")) {
            redirectAttributes.addFlashAttribute("mess", "Проект отредактирован. Ждите сообщений от модератора.");
            return "redirect:/game/craft#createdProjects";
        }
        redirectAttributes.addFlashAttribute("mess", "Ваша заявка на создание нового проекта зарегистрирована. Ждите сообщений от модератора.");
        return "redirect:/game/chooseProject?thingId=" + thing.getId();

    }

    @RequestMapping(value = "/game/makeItem", method = RequestMethod.POST)
    public String makeItem(ModelMap model, RedirectAttributes redirectAttributes,
                           @RequestParam(value = "projectId") Long projectId) {
        User user = userRepository.findByUserName(getAuthUser().getUsername());
        Family family = user.getCurrentFamily();

        Project project = craftService.getProject(projectId);
        if (project != null) {
            if (family.getCraftProjects().contains(project)) {
                boolean projectIsProductionOfBuilding = project.isProductionProject();
                boolean enoughMoney = true;
                if (projectIsProductionOfBuilding) {
                    enoughMoney = family.getMoney() >= project.getCost();
                }
                if (family.hasResourcesForProject(project) && enoughMoney) {
                    Item item = craftService.createItem(project, family);
                    family.getItems().add(item);
                    family.getFamilyResources().makingProject(project);
                    if (projectIsProductionOfBuilding) {
                        family.setMoney(family.getMoney() - project.getCost());
                    }
                    familyRepository.save(family);


                    logger.info(family.familyNameAndId() + " makes the item:" + item.getFullName());
                    String mess = "Ваша семья изготавливает предмет " + item.getFullName() + ". Израсходовано: " + project.resString();

                    if (projectIsProductionOfBuilding) {
                        mess = "Ваша семья изготавливает " + project.getThing().getName() + " " + project.getName() + ". Израсходовано: " + project.getCost() + (project.resString() != "" ? ", " + project.resString() : "");
                    }

                    familyLogService.addToLog(family, mess);
                    redirectAttributes.addFlashAttribute("mess", mess);
                    if (projectIsProductionOfBuilding) {
                        House building = houseService.getBuildingByProduction(project);
                        return "redirect:/game/buildings#building" + building.getId();
                    }
                    return "redirect:/game/chooseProject?thingId=" + project.getThing().getId();
                }
                logger.error(family.familyNameAndId() + " doesn't have resources to make item for project:" + project.getName());
                redirectAttributes.addFlashAttribute("mess", "Недостаточно ресурсов для изготовления предмета по проекту: " + project.getName());
                return "redirect:/game/chooseProject?thingId=" + project.getThing().getId();
            }
            logger.error(family.familyNameAndId() + " doesn't have the project:" + project.getName());
            redirectAttributes.addFlashAttribute("mess", "Ваша семья не владеет таким проектом: " + project.getName());
            return "redirect:/game/craft";
        }
        logger.error(family.familyNameAndId() + " want to make item for non-existing project id=" + projectId);
        redirectAttributes.addFlashAttribute("mess", "Такого проекта нет");
        return "redirect:/game";
    }

    @RequestMapping(value = "/game/makeProduction", method = RequestMethod.POST)
    public String makeProduction(ModelMap model, RedirectAttributes redirectAttributes,
                                 @RequestParam(value = "projectId") Long projectId) {
        User user = userRepository.findByUserName(getAuthUser().getUsername());
        Family family = user.getCurrentFamily();

        Project project = craftService.getProject(projectId);
        if (project != null) {
            if (family.getCraftProjects().contains(project)) {
                if (family.hasResourcesForProject(project) && family.getMoney() >= project.getCost()) {
                    House building = houseService.getBuildingByProduction(project);
                    FamilyBuilding familyBuilding = houseService.getFamilyBuildingByFamilyAndBuilding(family, building);
                    int count = 1;
                    if (familyBuilding.getBuildingQuality() >= 3) {
                        count += 1;
                    }
                    if (familyBuilding.getBuildingQuality() >= 5) {
                        count += 1;
                    }
                    Item item = null;
                    for (int i = 0; i < count; i++) {
                        item = craftService.createProductionItem(project, family);
                        family.getItems().add(item);
                    }
                    family.getFamilyResources().makingProject(project);
                    family.setMoney(family.getMoney() - project.getCost());
                    familyRepository.save(family);

                    logger.info(family.familyNameAndId() + " makes the item:" + item.getFullName() + ". Count: " + count);
                    String mess = "Ваша семья изготавливает " + project.getThing().getName() + " " + project.getName() + "(" + count + " шт.). Израсходовано: " + project.getCost() + (project.resString() != "" ? ", " + project.resString() : "");

                    familyLogService.addToLog(family, mess);
                    redirectAttributes.addFlashAttribute("mess", mess);


                    return "redirect:/game/buildings#building" + building.getId();
                }
                logger.error(family.familyNameAndId() + " doesn't have resources to make item for project:" + project.getName());
                redirectAttributes.addFlashAttribute("mess", "Недостаточно ресурсов для изготовления предмета по проекту: " + project.getName());
                return "redirect:/game/chooseProject?thingId=" + project.getThing().getId();
            }
            logger.error(family.familyNameAndId() + " doesn't have the project:" + project.getName());
            redirectAttributes.addFlashAttribute("mess", "Ваша семья не владеет таким проектом: " + project.getName());
            return "redirect:/game/craft";
        }
        logger.error(family.familyNameAndId() + " want to make item for non-existing project id=" + projectId);
        redirectAttributes.addFlashAttribute("mess", "Такого проекта нет");
        return "redirect:/game";
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
                logger.info(family.familyNameAndId() + " buy res: " + res);
                redirectAttributes.addFlashAttribute("mess", "Ресурсы добавлены");
                return "redirect:/game/chooseProject?thingId=" + thingId;
            } else {
                logger.error(family.familyNameAndId() + " buy unknown res: " + res);
                redirectAttributes.addFlashAttribute("mess", "Такого ресурса нет");
                return "redirect:/game/chooseProject?thingId=" + thingId;
            }


        }
        logger.error(family.familyNameAndId() + " doesn't have enough money to buy res: " + res);
        redirectAttributes.addFlashAttribute("mess", "Недостаточно денег для приобретения ресурсов");
        return "redirect:/game/chooseProject?thingId=" + thingId;
    }

    private UserDetails getAuthUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetail = (UserDetails) auth.getPrincipal();
        return userDetail;
    }
}
