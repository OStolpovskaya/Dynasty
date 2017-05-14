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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.imageio.ImageIO;
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

                    logger.info(family.logName() + " learn thing " + thing.getName() + "(" + thing.getId() + ")");
                    String mess = "Ваша семья выучила схему изготовления предмета " + thing.getName() + ". Использовано: " + thing.getCost() + " б.";
                    familyLogService.addToLog(family, mess);
                    redirectAttributes.addFlashAttribute("mess", mess);
                    return "redirect:/game/craft#craftbranch" + thing.getCraftBranch().getId();
                } else {
                    logger.error(family.logName() + " hasn't enough craft points to learn thing " + thing.getName() + "(" + thing.getId() + ")");
                    redirectAttributes.addFlashAttribute("mess", "Семья " + family.getFamilyName() + " не может выучить схему изготовления предмета " + thing.getName() + ". Недостаточно баллов развития. ");
                    return "redirect:/game/craft";
                }
            }
            logger.error(family.logName() + " want to learn thing without learning its parent id=" + thing.getId());
            redirectAttributes.addFlashAttribute("mess", "Сначала вам нужно изучить предмет-родитель.");
            return "redirect:/game/craft";
        }
        logger.error(family.logName() + " want to learn non-existing thing id=" + thing.getId());
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
                logger.error(family.logName() + " doesn't know the thing to buy projects for:" + thing.getName());
                redirectAttributes.addFlashAttribute("mess", "Ваша семья еще не умеет изготавливать такие предметы: " + thing.getName());
                return "redirect:/game/craft";
            }
        }
        logger.error(family.logName() + " want to buy projects for non-existing thing id=" + thingId);
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
                        logger.info(family.logName() + " buy project: '" + project.getName() + "' for thing " + thing.getName());
                        return "redirect:/game/chooseProject?thingId=" + thing.getId();
                    }
                    logger.error(family.logName() + " doesn't have enough money to buy project: '" + project.getName() + "' for thing " + thing.getName());
                    redirectAttributes.addFlashAttribute("mess", "Ваша семья не может приобрести проект " + project.getName() + ". Недостаточно денег!");
                    return "redirect:/game/craft";
                }
                logger.error(family.logName() + " already has project " + project.getName());
                redirectAttributes.addFlashAttribute("mess", "Ваша семья уже владеет таким проектом: " + project.getName());
                return "redirect:/game/craft";
            }
            logger.error(family.logName() + " doesn't know the thing to buy projects:" + thing.getName());
            redirectAttributes.addFlashAttribute("mess", "Ваша семья еще не умеет изготавливать такие предметы: " + thing.getName());
            return "redirect:/game/craft";
        }
        logger.error(family.logName() + " want to buy non-existing project id=" + projectId);
        redirectAttributes.addFlashAttribute("mess", "Такого проекта нет");
        return "redirect:/game/craft";
    }

    @RequestMapping(value = "/game/createProject", method = RequestMethod.POST)
    public String createProject(ModelMap model, RedirectAttributes redirectAttributes,
                                @RequestParam(value = "newProjectThingId") Long thingId,
                                @RequestParam(value = "newProjectName") String name,
                                @RequestParam(value = "newProjectCost", defaultValue = "0") int cost,
                                @RequestParam(value = "newProjectFile") MultipartFile file,
                                @RequestParam(value = "newProjectFood", defaultValue = "0") int food,
                                @RequestParam(value = "newProjectWood", defaultValue = "0") int wood,
                                @RequestParam(value = "newProjectMetall", defaultValue = "0") int metall,
                                @RequestParam(value = "newProjectPlastic", defaultValue = "0") int plastic,
                                @RequestParam(value = "newProjectMicroelectronics", defaultValue = "0") int microelectronics,
                                @RequestParam(value = "newProjectCloth", defaultValue = "0") int cloth,
                                @RequestParam(value = "newProjectStone", defaultValue = "0") int stone,
                                @RequestParam(value = "newProjectChemical", defaultValue = "0") int chemical) {
        User user = userRepository.findByUserName(getAuthUser().getUsername());
        Family family = user.getCurrentFamily();

        Thing thing = craftService.getThing(thingId);
        if (thing != null) {
            if (family.getMoney() >= Const.COST_NEW_PROJECT) {
                if (name.length() <= 30) {
                    if (!file.isEmpty() && file.getSize() != 0 && file.getContentType().equalsIgnoreCase("image/png")) {
                        BufferedImage image = null;
                        try {
                            image = ImageIO.read(file.getInputStream());
                            int width = image.getWidth();
                            int height = image.getHeight();
                            if (width == thing.getWidth() && height == thing.getHeight()) {
                                Project project = new Project();
                                project.setThing(thing);
                                project.setAuthor(family);
                                project.setStatus(ProjectStatus.newProject);
                                project.setName(name);
                                project.setCost(cost);

                                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                                ImageIO.write(image, "png", baos);
                                baos.flush();
                                byte[] imageInByte = baos.toByteArray();
                                baos.close();

                                project.setView(imageInByte);

                                project.setFood(food);
                                project.setWood(wood);
                                project.setMetall(metall);
                                project.setPlastic(plastic);
                                project.setMicroelectronics(microelectronics);
                                project.setCloth(cloth);
                                project.setStone(stone);
                                project.setChemical(chemical);

                                craftService.saveProject(project);

                                family.setMoney(family.getMoney() - Const.COST_NEW_PROJECT);
                                familyRepository.save(family);

                                logger.info(family.logName() + " created new project " + project.getName());
                                redirectAttributes.addFlashAttribute("mess", "Ваша заявка на создание нового проекта зарегистрирована. Ждите сообщений от модератора.");
                                return "redirect:/game/chooseProject?thingId=" + thing.getId();
                            }
                            logger.error(family.logName() + " uploaded image for new project with incorrect size " + file.getOriginalFilename());
                            redirectAttributes.addFlashAttribute("mess", "Изображение имеет неверные размеры: " + width + "x" + height + ", а нужно: " + thing.getWidth() + "x" + thing.getHeight());
                            return "redirect:/game/chooseProject?thingId=" + thing.getId();
                        } catch (IOException e) {
                            e.printStackTrace();
                            logger.error(family.logName() + " has error in reading uploaded file " + file.getOriginalFilename());
                            redirectAttributes.addFlashAttribute("mess", "Ошибка при чтении файла");
                            return "redirect:/game/chooseProject?thingId=" + thing.getId();
                        }

                    }
                }
                logger.error(family.logName() + " file is empty or has size 0 or not png " + thing.getName());
                redirectAttributes.addFlashAttribute("mess", "Загружаемый файл пустой или имеет размер 0 или это не png");
                return "redirect:/game/chooseProject?thingId=" + thing.getId();
            }
            logger.error(family.logName() + " has not enough money to create project for thing " + thing.getName());
            redirectAttributes.addFlashAttribute("mess", "Недостаточно денег для создания нового проекта");
            return "redirect:/game/chooseProject?thingId=" + thing.getId();
        }
        logger.error(family.logName() + " want to buy projects for non-existing thing id=" + thingId);
        redirectAttributes.addFlashAttribute("mess", "Такого предмета нет");
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

                    logger.info(family.logName() + " makes the item:" + item.getFullName());
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
                logger.error(family.logName() + " doesn't have resources to make item for project:" + project.getName());
                redirectAttributes.addFlashAttribute("mess", "Недостаточно ресурсов для изготовления предмета по проекту: " + project.getName());
                return "redirect:/game/chooseProject?thingId=" + project.getThing().getId();
            }
            logger.error(family.logName() + " doesn't have the project:" + project.getName());
            redirectAttributes.addFlashAttribute("mess", "Ваша семья не владеет таким проектом: " + project.getName());
            return "redirect:/game/craft";
        }
        logger.error(family.logName() + " want to make item for non-existing project id=" + projectId);
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
                logger.info(family.logName() + " buy res: " + res);
                redirectAttributes.addFlashAttribute("mess", "Ресурсы добавлены");
                return "redirect:/game/chooseProject?thingId=" + thingId;
            } else {
                logger.error(family.logName() + " buy unknown res: " + res);
                redirectAttributes.addFlashAttribute("mess", "Такого ресурса нет");
                return "redirect:/game/chooseProject?thingId=" + thingId;
            }


        }
        logger.error(family.logName() + " doesn't have enough money to buy res: " + res);
        redirectAttributes.addFlashAttribute("mess", "Недостаточно денег для приобретения ресурсов");
        return "redirect:/game/chooseProject?thingId=" + thingId;
    }

    private UserDetails getAuthUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetail = (UserDetails) auth.getPrincipal();
        return userDetail;
    }
}
