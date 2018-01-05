package dyn.controllers;

/**
 * Created by OM on 17.02.2017.
 */


import dyn.form.RaceAppearanceForm;
import dyn.model.*;
import dyn.model.Character;
import dyn.model.appearance.*;
import dyn.model.career.Career;
import dyn.model.career.Education;
import dyn.repository.*;
import dyn.service.*;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.ResourceUtils;
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
import java.awt.image.WritableRaster;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.*;

@Controller
public class AdminController {
    private static final Logger logger = LogManager.getLogger(AdminController.class);
    @Autowired
    AppearanceService app;
    @Autowired
    RaceService raceService;
    @Autowired
    CareerService careerService;

    @Autowired
    RaceRepository raceRepository;
    @Autowired
    AchievementRepository achievementRepository;
    @Autowired
    BuffRepository buffRepository;
    @Autowired
    FamilyRepository familyRepository;
    @Autowired
    CraftService craftService;
    @Autowired
    HouseService houseService;
    @Autowired
    ThingRepository thingRepository;
    @Autowired
    ProjectRepository projectRepository;
    @Autowired
    private RoomRepository roomRepository;
    @Autowired
    private RoomThingRepository roomThingRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private FianceeRepository fianceeRepository;
    @Autowired
    private CharacterRepository characterRepository;
    @Autowired
    private TownNewsService townNewsService;

    @RequestMapping("/admin")
    public String admin(ModelMap model) {
        return "admin";
    }

    @RequestMapping("/admin/appearance")
    public String appearance(ModelMap model) {

        model.addAttribute("bodyList", app.getBodyList(app.ALL));
        model.addAttribute("earsList", app.getEarsList(app.ALL));
        model.addAttribute("eyebrowsList", app.getEyebrowsList(app.ALL));
        model.addAttribute("eyeColorList", app.getEyeColorList(app.ALL));
        model.addAttribute("eyesList", app.getEyesList(app.ALL));
        model.addAttribute("hairColorList", app.getHairColorList(app.ALL));
        model.addAttribute("hairStyleList", app.getHairStyleList());
        model.addAttribute("hairTypeList", app.getHairTypeList(app.ALL));
        model.addAttribute("headList", app.getHeadList(app.ALL));
        model.addAttribute("heightList", app.getHeightList(app.ALL));
        model.addAttribute("mouthList", app.getMouthList(app.ALL));
        model.addAttribute("noseList", app.getNoseList(app.ALL));
        model.addAttribute("skinColorList", app.getSkinColorList(app.ALL));
        return "admin/appearance";
    }

    @RequestMapping("/admin/achievements")
    public String achievements(ModelMap model) {

        List<Achievement> achievements = achievementRepository.findAllByOrderByTypeAscForWhatAsc();
        for (Achievement achievement : achievements) {
            if (achievement.getType().equals(AchievementType.newborn)) {
                achievement.forWhatString = raceRepository.findOne(achievement.getForWhat()).getName();
            }
            if (achievement.getType().equals(AchievementType.vocation10level)) {
                achievement.forWhatString = careerService.getVocation(achievement.getForWhat()).getName();
            }
        }
        model.addAttribute("achievementList", achievements);
        return "admin/achievements";
    }

    @RequestMapping("/admin/buffs")
    public String buffs(ModelMap model) {

        model.addAttribute("buffList", buffRepository.findAll());
        return "admin/buffs";
    }

    @RequestMapping("/admin/fiancees")
    public String fianceesView(ModelMap model) {

        List<Object[]> list = fianceeRepository.countFianceeByLevel();
        model.addAttribute("list", list);
        return "admin/fiancees";
    }

    @RequestMapping("/admin/families")
    public String familiesView(ModelMap model) {

        Iterable<Family> families = familyRepository.findAllByOrderByUserLastLoginDateDesc();
        List<Family> currentFamilies = new ArrayList<>();
        List<Family> notCurrentFamilies = new ArrayList<>();
        List<Family> guestFamilies = new ArrayList<>();
        List<Family> notPlayerFamilies = new ArrayList<>();
        for (Family family : families) {
            if (family.getUser().isGuest()) {
                guestFamilies.add(family);
                continue;
            }
            if (family.getUser().getType().equals(UserType.player)) {
                if (family.isCurrent()) {
                    currentFamilies.add(family);
                } else {
                    notCurrentFamilies.add(family);
                }
                continue;
            }
            notPlayerFamilies.add(family);
        }
        model.addAttribute("curFamilyList", currentFamilies);
        model.addAttribute("notCurFamilyList", notCurrentFamilies);
        model.addAttribute("guestFamilies", guestFamilies);
        model.addAttribute("notPlayerFamilies", notPlayerFamilies);


        return "admin/families";
    }

    @RequestMapping("/admin/users")
    public String usersView(ModelMap model) {

        List<User> userList = userRepository.findAllByOrderByLastLoginDateDesc();
        for (User user : userList) {
            Family currentFamily = user.getCurrentFamily();
        }
        model.addAttribute("userList", userList);
        return "admin/users";
    }

    @RequestMapping(value = "/admin/family", method = RequestMethod.GET)
    public String familyView(ModelMap model,
                             @RequestParam("familyId") Long familyId,
                             RedirectAttributes redirectAttributes) {
        Family family = familyRepository.findOne(familyId);
        model.addAttribute("family", family);

        List<Item> items = craftService.getItemsByFamilyAndPlace(family, ItemPlace.store);
        Map<Item, Integer> itemsInStoreCMap = craftService.arrangeItems(items);
        model.addAttribute("itemsInStoreCMap", itemsInStoreCMap);

        return "admin/family";
    }

    @RequestMapping(value = "/admin/townNews", method = RequestMethod.GET)
    public String info(ModelMap model, RedirectAttributes redirectAttributes) {
        List<TownNews> news = townNewsService.getNews();
        model.addAttribute("news", news);
        return "admin/townNews";
    }

    @RequestMapping(value = "/admin/roomThingsWithProjects", method = RequestMethod.GET)
    public String roomThingsWithProjects(ModelMap model,
                                         @RequestParam(value = "houseId", defaultValue = "1") Long houseId,
                                         RedirectAttributes redirectAttributes) {
        List<House> houseList = houseService.getHomeList();
        List<House> buildingList = houseService.getBuildingList();
        model.addAttribute("houseList", houseList);
        model.addAttribute("buildingList", buildingList);

        House house = houseService.getHouse(houseId);
        if (house == null) {
            house = houseService.getHouse(1L);
        }
        model.addAttribute("house", house);
        List<RoomView> roomViewList = new ArrayList<>();

        List<Room> roomList;
        if (house.getType() == HouseType.home) {
            roomList = roomRepository.findByHouseIdLessThanEqualOrderById(house.getId());
        } else {
            roomList = roomRepository.findByHouseIdOrderById(house.getId());
        }
        for (Room room : roomList) {
            RoomView roomView = new RoomView(room);
            roomView.setFull(true);

            List<RoomThing> roomThings;

            ItemPlace itemPlace;
            if (house.getType() == HouseType.home) {
                roomThings = roomThingRepository.findByHouseIdLessThanEqualAndRoomIdOrderByLayerAsc(house.getId(), room.getId());
            } else {
                roomThings = roomThingRepository.findByHouseIdAndRoomIdOrderByLayerAsc(house.getId(), room.getId());
            }
            Random random = new Random();
            for (RoomThing roomThing : roomThings) {
                RoomThingWithProjects roomThingWithProjects = new RoomThingWithProjects(roomThing);


                List<Project> availableProjects = projectRepository.findByThing(roomThing.getThing());
                int idx = random.nextInt(availableProjects.size());
                Project currentProject = availableProjects.get(idx);


                roomThingWithProjects.setCurrentProject(currentProject);
                roomThingWithProjects.setAvailableProjects(availableProjects);

                roomView.getRoomThingWithProjects().add(roomThingWithProjects);
            }

            roomViewList.add(roomView);
        }
        model.addAttribute("roomViewList", roomViewList);

        return "admin/roomThingsWithProjects";
    }

    @RequestMapping(value = "/admin/generateFiancee", method = RequestMethod.POST)
    public String generateFiancees(ModelMap model,
                                   @RequestParam("level") int level,
                                   RedirectAttributes redirectAttributes) {
        logger.debug("AdminController.generateFiancees, level=" + level);
        Family family = familyRepository.findOne(1L);

        String names = genFiancees(level, family);
        redirectAttributes.addFlashAttribute("mess", "Fiancees are generated: " + names);
        return "redirect:/admin";
    }

    public String genFiancees(int level, Family family) {
        StringBuilder names = new StringBuilder();
        for (int i = 0; i < 5; i++) {
            Character female = new Character();
            female.setName(characterRepository.getRandomNameFemale());
            female.setSex("female");

            female.setBody(app.getRandomBody(app.USUAL));
            female.setEars(app.getRandomEars(app.USUAL));
            female.setEyebrows(app.getRandomEyeBrows(app.USUAL));
            female.setEyeColor(app.getRandomEyeColor(app.USUAL));
            female.setEyes(app.getRandomEyes(app.USUAL));
            female.setHairColor(app.getRandomHairColor(app.USUAL));
            female.setHairType(app.getRandomHairType(app.USUAL));
            female.setHairStyle(app.getRandomHairStyle(female.getSex(), female.getHairType()));
            female.setHead(app.getRandomHead(app.USUAL));
            female.setHeight(app.getRandomHeight(app.USUAL));
            female.setMouth(app.getRandomMouth(app.USUAL));
            female.setNose(app.getRandomNose(app.USUAL));
            female.setSkinColor(app.getRandomSkinColor(app.USUAL));

            female.setFamily(family);
            female.setLevel(level);
            female.setRace(raceRepository.findOne(Race.RACE_HUMAN));


            female.setCareer(careerService.generateCareerForFounders());

            characterRepository.save(female);

            Fiancee fiancee = new Fiancee();
            fiancee.setCharacter(female);
            fiancee.setCost(10);
            fianceeRepository.save(fiancee);

            names.append(fiancee.getCharacter().getName()).append(" ");
        }
        return names.toString();
    }

    @RequestMapping(value = "/admin/updateCharacter", method = RequestMethod.POST)
    public String updateCharacterRequest(ModelMap model,
                                         @RequestParam("character_id") long characterId,
                                         RedirectAttributes redirectAttributes) {
        logger.debug("AdminController.generateFiancees, characterId=" + characterId);

        Character character = characterRepository.findOne(characterId);
        if (character != null) {
            updateCharacter(character);
        } else {
            redirectAttributes.addFlashAttribute("mess", "ОШИБКА! Персонаж с id=" + characterId + " не найден!");
            return "redirect:/admin";
        }

        redirectAttributes.addFlashAttribute("mess", "Персонаж обновлен: " + character.getName());
        return "redirect:/admin";
    }

    @RequestMapping(value = "/admin/alterCharacterForm", method = RequestMethod.POST)
    public String alterCharacterForm(ModelMap model,
                                     @RequestParam("character_id") long characterId,
                                     RedirectAttributes redirectAttributes) {
        Character character = characterRepository.findOne(characterId);
        if (character != null) {
            model.addAttribute("character", character);
            model.addAttribute("career", character.getCareer());
            model.addAttribute("vocationList", careerService.getVocationList());
            model.addAttribute("educationList", careerService.getEducationList());

            RaceAppearanceForm raceAppearanceForm = new RaceAppearanceForm();
            raceAppearanceForm = app.fillRaceAppearanceForm(raceAppearanceForm);
            model.addAttribute("form", raceAppearanceForm);
            return "admin/alterCharacterForm";
        } else {
            redirectAttributes.addFlashAttribute("mess", "Персонаж не найден: " + characterId);
            return "redirect:/admin";
        }

    }

    @RequestMapping(value = "/admin/alterCharacter", method = RequestMethod.POST)
    public String alterCharacter(ModelMap model,
                                 @RequestParam("character_id") long characterId,
                                 @ModelAttribute("character") Character formCharacter,
                                 RedirectAttributes redirectAttributes) {
        String username = getAuthUser().getUsername();
        Character character = characterRepository.findOne(characterId);
        boolean updated = false;
        if (character != null) {
            logger.info(username + " changes " + character.getName());
            if (character.getBody() != formCharacter.getBody()) {
                logger.debug("   body changed:" + character.getBody() + " -> " + formCharacter.getBody());
                character.setBody(formCharacter.getBody());
                updated = true;
            }
            if (character.getEars() != formCharacter.getEars()) {
                logger.debug("   ears changed:" + character.getEars() + " -> " + formCharacter.getEars());
                character.setEars(formCharacter.getEars());
                updated = true;
            }
            if (character.getEyebrows() != formCharacter.getEyebrows()) {
                logger.debug("   eyebrows changed:" + character.getEyebrows() + " -> " + formCharacter.getEyebrows());
                character.setEyebrows(formCharacter.getEyebrows());
                updated = true;
            }
            if (character.getEyeColor() != formCharacter.getEyeColor()) {
                logger.debug("   eyeColor changed:" + character.getEyeColor() + " -> " + formCharacter.getEyeColor());
                character.setEyeColor(formCharacter.getEyeColor());
                updated = true;
            }
            if (character.getEyes() != formCharacter.getEyes()) {
                logger.debug("   eyes changed:" + character.getEyes() + " -> " + formCharacter.getEyes());
                character.setEyes(formCharacter.getEyes());
                updated = true;
            }
            if (character.getHairColor() != formCharacter.getHairColor()) {
                logger.debug("   hairColor changed:" + character.getHairColor() + " -> " + formCharacter.getHairColor());
                character.setHairColor(formCharacter.getHairColor());
                updated = true;
            }
            if (character.getHairType() != formCharacter.getHairType()) {
                logger.debug("   hairType changed:" + character.getHairType() + " -> " + formCharacter.getHairType());
                character.setHairType(formCharacter.getHairType());
                character.setHairStyle(app.getRandomHairStyle(character.getSex(), character.getHairType()));
                updated = true;
            }
            if (character.getHead() != formCharacter.getHead()) {
                logger.debug("   head changed:" + character.getHead() + " -> " + formCharacter.getHead());
                character.setHead(formCharacter.getHead());
                updated = true;
            }
            if (character.getHeight() != formCharacter.getHeight()) {
                logger.debug("   height changed:" + character.getHeight() + " -> " + formCharacter.getHeight());
                character.setHeight(formCharacter.getHeight());
                updated = true;
            }
            if (character.getMouth() != formCharacter.getMouth()) {
                logger.debug("   mouth changed:" + character.getMouth() + " -> " + formCharacter.getMouth());
                character.setMouth(formCharacter.getMouth());
                updated = true;
            }
            if (character.getNose() != formCharacter.getNose()) {
                logger.debug("   nose changed:" + character.getNose() + " -> " + formCharacter.getNose());
                character.setNose(formCharacter.getNose());
                updated = true;
            }
            if (character.getSkinColor() != formCharacter.getSkinColor()) {
                logger.debug("   skinColor changed:" + character.getSkinColor() + " -> " + formCharacter.getSkinColor());
                character.setSkinColor(formCharacter.getSkinColor());
                updated = true;
            }

            if (updated) {
                updateCharacter(character);
                redirectAttributes.addFlashAttribute("mess", "Персонаж обновлен: " + character.getName());
                return "redirect:/admin";
            } else {
                redirectAttributes.addFlashAttribute("mess", "Персонаж не был изменен: " + character.getName());
                return "redirect:/admin";
            }

        } else {
            redirectAttributes.addFlashAttribute("mess", "Персонаж не найден: " + characterId);
            return "redirect:/admin";
        }
    }

    @RequestMapping(value = "/admin/alterCharacterCareer", method = RequestMethod.POST)
    public String alterCharacterCareer(ModelMap model,
                                       @RequestParam("character_id") long characterId,
                                       @RequestParam("vocation") long vocation,
                                       @RequestParam("intelligence") int intelligence,
                                       @RequestParam("charisma") int charisma,
                                       @RequestParam("strength") int strength,
                                       @RequestParam("creativity") int creativity,
                                       RedirectAttributes redirectAttributes) {
        String username = getAuthUser().getUsername();
        Character character = characterRepository.findOne(characterId);
        if (character != null) {
            Career career = character.getCareer();
            career.setVocation(careerService.getVocation(vocation));
            career.setIntelligence(intelligence);
            career.setCharisma(charisma);
            career.setStrength(strength);
            career.setCreativity(creativity);
            career.setEducation(careerService.getEducation(Education.PRIMARY));
            characterRepository.save(character);
            logger.info(username + " changes character's cereer:" + character.getId());
            redirectAttributes.addFlashAttribute("mess", "Карьера персонажа обновлена: " + characterId);
            return "redirect:/admin";
        } else {
            redirectAttributes.addFlashAttribute("mess", "Персонаж не найден: " + characterId);
            return "redirect:/admin";
        }
    }

    @RequestMapping("/admin/craft")
    public String craft(ModelMap model) {
        model.addAttribute("thingList", craftService.getAllThings());
        model.addAttribute("craftBranchList", craftService.getCraftBranches());
        model.addAttribute("parentThings", craftService.getThingsForTree());
        return "admin/craft";
    }

    //projectsForThing
    @RequestMapping("/admin/projectsForThing")
    public String projectsForThing(ModelMap model, @RequestParam("thingId") Long thingId) {
        Thing thing = thingRepository.findOne(thingId);
        if (thing != null) {
            model.addAttribute("thing", thing);
            model.addAttribute("projects", thing.getProjects());

            Project project = new Project();
            project.setThing(thing);
            project.setAuthor(familyRepository.findOne(1L));
            model.addAttribute("newProject", project);

            return "admin/projectsForThing";
        }
        return "admin/craft";
    }

    //createProject
    @RequestMapping(value = "/admin/createProject", method = RequestMethod.POST)
    public String createProject(ModelMap model, @ModelAttribute("newProject") @Valid Project project,
                                BindingResult result,
                                RedirectAttributes redirectAttributes,
                                @RequestParam(value = "newProjectFile") MultipartFile file) {
        String username = getAuthUser().getUsername();
        Thing thing = project.getThing();

        BufferedImage image = null;
        byte[] imageInByte = null;

        if (file.isEmpty() || file.getSize() == 0) {
            logger.error(username + " try to create project. File is empty or has size 0 ");
            redirectAttributes.addFlashAttribute("mess", "Пустой файл или размер 0");
        } else {
            if (!file.getContentType().equalsIgnoreCase("image/png")) {
                logger.error(username + " try to create project . File is not png ");
                redirectAttributes.addFlashAttribute("mess", "Не PNG");
            } else {
                try {
                    image = ImageIO.read(file.getInputStream());
                    int width = image.getWidth();
                    int height = image.getHeight();
                    if (width != thing.getWidth() || height != thing.getHeight()) {
                        logger.error(username + " try to create project. Uploaded image has incorrect size " + file.getOriginalFilename());
                        redirectAttributes.addFlashAttribute("mess", "Размеры не соответствуют thing");
                    } else {
                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        ImageIO.write(image, "png", baos);
                        baos.flush();
                        imageInByte = baos.toByteArray();
                        baos.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    logger.error(username + " try to create project. Error in reading uploaded file " + file.getOriginalFilename());
                    redirectAttributes.addFlashAttribute("mess", "Ошибка чтения файла");
                }
            }
        }
        if (imageInByte != null) {
            project.setView(imageInByte);
            project.setStatus(ProjectStatus.approved);
            project.setStatusMessage("");
            craftService.saveProject(project);
            logger.info(username + " create project " + project.getFullName());
            redirectAttributes.addFlashAttribute("mess", "Проект создан: " + project.getFullName());
        }
        return "redirect:/admin/projectsForThing?thingId=" + thing.getId();
    }

    //updateProjectName
    @RequestMapping(value = "/admin/updateProjectName", method = RequestMethod.POST)
    public String updateProjectName(ModelMap model, RedirectAttributes redirectAttributes,
                                    @RequestParam("projectId") Long projectId,
                                    @RequestParam("name") String name) {
        String username = getAuthUser().getUsername();
        Project project = craftService.getProject(projectId);
        Thing thing = project.getThing();
        if (name.length() >= 2 && name.length() <= 30) {
            project.setName(name);
            craftService.saveProject(project);
            logger.info(username + " update project name: " + project.getFullName());
            redirectAttributes.addFlashAttribute("mess", "Название проекта изменено: " + project.getFullName());
        } else {
            logger.error(username + " try to update project name. Name length is incorrect. ");
            redirectAttributes.addFlashAttribute("mess", "Имя должно быть больше 2, но меньше 30 символов");
        }
        return "redirect:/admin/projectsForThing?thingId=" + thing.getId();
    }

    //updateProjectCost
    @RequestMapping(value = "/admin/updateProjectCost", method = RequestMethod.POST)
    public String updateProjectCost(ModelMap model, RedirectAttributes redirectAttributes,
                                    @RequestParam("projectId") Long projectId,
                                    @RequestParam("cost") int cost) {
        String username = getAuthUser().getUsername();
        Project project = craftService.getProject(projectId);
        Thing thing = project.getThing();

        project.setCost(cost);
        craftService.saveProject(project);
        logger.info(username + " update project cost " + project.getFullName() + ": " + project.getCost());
        redirectAttributes.addFlashAttribute("mess", "Стоимость проекта изменена " + project.getFullName() + ": " + project.getCost());

        return "redirect:/admin/projectsForThing?thingId=" + thing.getId();
    }

    //updateProjectResources
    @RequestMapping(value = "/admin/updateProjectResources", method = RequestMethod.POST)
    public String updateProjectResources(ModelMap model, RedirectAttributes redirectAttributes,
                                         @RequestParam("projectId") Long projectId,
                                         @RequestParam("food") int food,
                                         @RequestParam("wood") int wood,
                                         @RequestParam("metall") int metall,
                                         @RequestParam("plastic") int plastic,
                                         @RequestParam("microelectronics") int microelectronics,
                                         @RequestParam("cloth") int cloth,
                                         @RequestParam("stone") int stone,
                                         @RequestParam("chemical") int chemical) {
        String username = getAuthUser().getUsername();
        Project project = craftService.getProject(projectId);
        Thing thing = project.getThing();

        project.setFood(food);
        project.setWood(wood);
        project.setMetall(metall);
        project.setPlastic(plastic);
        project.setMicroelectronics(microelectronics);
        project.setCloth(cloth);
        project.setStone(stone);
        project.setChemical(chemical);
        craftService.saveProject(project);
        logger.info(username + " update project resources " + project.getFullName() + ": " + project.resString());
        redirectAttributes.addFlashAttribute("mess", "Ресурсы проекта изменены " + project.getFullName() + ": " + project.resString());

        return "redirect:/admin/projectsForThing?thingId=" + thing.getId();
    }

    //updateProjectView
    @RequestMapping(value = "/admin/updateProjectView", method = RequestMethod.POST)
    public String updateProjectView(ModelMap model, RedirectAttributes redirectAttributes,
                                    @RequestParam("projectId") Long projectId,
                                    @RequestParam(value = "file") MultipartFile file) {
        String username = getAuthUser().getUsername();

        Family family = familyRepository.findOne(1L);
        Project project = craftService.getProject(projectId);
        Thing thing = project.getThing();

        BufferedImage image = null;
        byte[] imageInByte = null;

        if (file.isEmpty() || file.getSize() == 0) {
            logger.error(username + " try to update project view. File is empty or has size 0 ");
            redirectAttributes.addFlashAttribute("mess", "Пустой файл или размер 0");
        } else {
            if (!file.getContentType().equalsIgnoreCase("image/png")) {
                logger.error(username + " try to update project view. File is not png ");
                redirectAttributes.addFlashAttribute("mess", "Не PNG");
            } else {
                try {
                    image = ImageIO.read(file.getInputStream());
                    int width = image.getWidth();
                    int height = image.getHeight();
                    if (width != thing.getWidth() || height != thing.getHeight()) {
                        logger.error(username + " try to update project view. Uploaded image has incorrect size " + file.getOriginalFilename());
                        redirectAttributes.addFlashAttribute("mess", "Размеры не соответствуют thing");
                    } else {
                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        ImageIO.write(image, "png", baos);
                        baos.flush();
                        imageInByte = baos.toByteArray();
                        baos.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    logger.error(username + " try to update project view. Error in reading uploaded file " + file.getOriginalFilename());
                    redirectAttributes.addFlashAttribute("mess", "Ошибка чтения файла");
                }
            }
        }
        if (imageInByte != null) {
            project.setView(imageInByte);
            craftService.saveProject(project);
            logger.info(username + " update project view " + project.getFullName());
            redirectAttributes.addFlashAttribute("mess", "Изображение проекта обновлено: " + project.getFullName());
        }
        return "redirect:/admin/projectsForThing?thingId=" + thing.getId();
    }

    @RequestMapping("/admin/rooms")
    public String rooms(ModelMap model,
                        @RequestParam(value = "houseId", defaultValue = "1") Long houseId,
                        RedirectAttributes redirectAttributes) {
        List<House> houseList = houseService.getHomeList();
        model.addAttribute("houseList", houseList);

        model.addAttribute("thingList", thingRepository.findByCraftBranchIdLessThanEqualOrderByName(Const.CRAFTBRANCH_MAX));

        House house = houseService.getHouse(houseId);
        model.addAttribute("house", house);

        List<RoomView> roomViewList = houseService.getRoomMaps(house, familyRepository.findOne(1L));
        model.addAttribute("roomViewList", roomViewList);

        return "admin/rooms";
    }

    @RequestMapping("/admin/buildings")
    public String buildings(ModelMap model, RedirectAttributes redirectAttributes) {

        List<House> buildingList = houseService.getBuildingList();
        model.addAttribute("buildingList", buildingList);

        Map<House, List<RoomView>> buildingMap = new LinkedHashMap<>();
        for (House house : buildingList) {
            buildingMap.put(house, houseService.getRoomMaps(house, familyRepository.findOne(1L)));
        }
        model.addAttribute("buildingMap", buildingMap);

        Iterable<Thing> thingList = thingRepository.findAllByOrderByName();
        model.addAttribute("thingList", thingList);

        return "admin/buildings";
    }

    @RequestMapping(value = "/admin/placeBuildingThing", method = RequestMethod.POST)
    public String placeBuildingThing(ModelMap model,
                                     @RequestParam("buildingId") Long buildingId,
                                     @RequestParam("thingId") Long thingId,
                                     @RequestParam("x") int x,
                                     @RequestParam("y") int y,
                                     @RequestParam("layer") int layer,
                                     RedirectAttributes redirectAttributes) {
        RoomThing buildingThing = new RoomThing();
        buildingThing.setHouse(houseService.getHouse(buildingId));
        buildingThing.setThing(thingRepository.findOne(thingId));
        buildingThing.setX(x);
        buildingThing.setY(y);
        buildingThing.setLayer(layer);
        houseService.saveRoomThing(buildingThing);
        return "redirect:/admin/buildings#building" + buildingId;
    }

    @RequestMapping(value = "/admin/changeThing", method = RequestMethod.POST)
    public String changeThing(ModelMap model,
                              @RequestParam("thingId") Long thingId,
                              @RequestParam("thingName") String thingName,
                              @RequestParam("thingParent") Long thingParentId,
                              @RequestParam("thingCost") int thingCost,
                              @RequestParam("thingWidth") int thingWidth,
                              @RequestParam("thingHeight") int thingHeight,
                              RedirectAttributes redirectAttributes) {
        craftService.changeThing(thingId, thingName, thingParentId, thingCost, thingWidth, thingHeight);
        return "redirect:/admin/craft";
    }

    @RequestMapping(value = "/admin/newThing", method = RequestMethod.POST)
    public String newThing(ModelMap model,
                           @RequestParam("thingCraftBranchId") Long thingCraftBranchId,
                           @RequestParam("thingName") String thingName,
                           @RequestParam("thingParentId") Long thingParentId,
                           @RequestParam("thingCost") int thingCost,
                           @RequestParam("thingWidth") int thingWidth,
                           @RequestParam("thingHeight") int thingHeight,
                           RedirectAttributes redirectAttributes) {
        craftService.newThing(thingName, thingCraftBranchId, thingParentId, thingCost, thingWidth, thingHeight);
        return "redirect:/admin/craft";
    }

    @RequestMapping(value = "/admin/changeRoomThing", method = RequestMethod.POST)
    public String changeRoomThing(ModelMap model,
                                  @RequestParam("roomThingId") Long roomThingId,
                                  @RequestParam("roomThingHouseId") Long roomThingHouseId,
                                  @RequestParam("roomThingX") int roomThingX,
                                  @RequestParam("roomThingY") int roomThingY,
                                  @RequestParam("roomThingLayer") int roomThingLayer,
                                  RedirectAttributes redirectAttributes) {
        houseService.changeRoomThing(roomThingId, roomThingHouseId, roomThingX, roomThingY, roomThingLayer);
        RoomThing roomThing = houseService.getRoomThingById(roomThingId);
        House house = roomThing.getHouse();
        if (house.getType() == HouseType.building) {
            return "redirect:/admin/buildings#building" + house.getId();
        }
        return "redirect:/admin/rooms?houseId=" + roomThingHouseId + "#room" + roomThing.getRoom().getId();
    }

    @RequestMapping(value = "/admin/removeRoomThing", method = RequestMethod.POST)
    public String removeRoomThing(ModelMap model,
                                  @RequestParam("roomThingId") Long roomThingId,
                                  @RequestParam("roomThingHouseId") Long roomThingHouseId,
                                  RedirectAttributes redirectAttributes) {
        RoomThing roomThing = houseService.getRoomThingById(roomThingId);
        House house = roomThing.getHouse();
        String redirect = "redirect:/admin/rooms?houseId=" + roomThingHouseId + "#room" + roomThing.getRoom().getId();
        if (house.getType() == HouseType.building) {
            redirect = "redirect:/admin/buildings#building" + house.getId();
        }

        houseService.removeRoomThing(roomThingId);
        return redirect;
    }

    @RequestMapping(value = "/admin/newRoomThing", method = RequestMethod.POST)
    public String newRoomThing(ModelMap model,
                               @RequestParam("roomThingThingId") Long roomThingThingId,
                               @RequestParam("roomThingHouseId") Long roomThingHouseId,
                               @RequestParam("roomThingRoomId") Long roomThingRoomId,
                               @RequestParam("roomThingX") int roomThingX,
                               @RequestParam("roomThingY") int roomThingY,
                               @RequestParam("roomThingLayer") int roomThingLayer,
                               RedirectAttributes redirectAttributes) {
        RoomThing roomThing = houseService.newRoomThing(roomThingThingId, roomThingHouseId, roomThingRoomId, roomThingX, roomThingY, roomThingLayer);
        House house = roomThing.getHouse();
        if (house.getType() == HouseType.building) {
            return "redirect:/admin/buildings#building" + house.getId();
        }
        return "redirect:/admin/rooms?houseId=" + roomThingHouseId + "#room" + roomThingRoomId;
    }

    @RequestMapping("/admin/random")
    public String achievements(ModelMap model, @RequestParam(value = "sex", required = false) String sex) {
        if (sex == null || sex.isEmpty() || !sex.equals("female")) {
            sex = "male";
        }

        Character character = new Character();
        character.setSex(sex);
        character.setBody(app.getRandomBody(app.ALL));
        character.setEars(app.getRandomEars(app.ALL));
        character.setEyebrows(app.getRandomEyeBrows(app.ALL));
        character.setEyeColor(app.getRandomEyeColor(app.ALL));
        character.setEyes(app.getRandomEyes(app.ALL));
        character.setHairColor(app.getRandomHairColor(app.ALL));
        character.setHairType(app.getRandomHairType(app.ALL));
        character.setHairStyle(app.getRandomHairStyle(sex, character.getHairType()));
        character.setHead(app.getRandomHead(app.ALL));
        character.setHeight(app.getRandomHeight(app.ALL));
        character.setMouth(app.getRandomMouth(app.ALL));
        character.setNose(app.getRandomNose(app.ALL));
        character.setSkinColor(app.getRandomSkinColor(app.ALL));


        model.addAttribute("character", character);

        return "admin/random";
    }

    public void updateCharacter(Character character) {

        Race race = raceService.defineRace(character);
        character.setRace(race);

        characterRepository.save(character);
    }


    private UserDetails getAuthUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return (UserDetails) auth.getPrincipal();
    }

    @RequestMapping("/admin/test")
    public String test(ModelMap model, RedirectAttributes redirectAttributes,
                       @RequestParam(name = "id", defaultValue = "2488") Long characterId) {
        System.out.println("=== TEST ===");

        System.out.println("GET CHARACTER");
        Character character = characterRepository.findOne(characterId);
        model.addAttribute("character", character);

        System.out.println("GET FATHERS");
        Family family = familyRepository.findOne(37L);
        List<Character> fathers;
        if (family.getLevel() > 0) {
            fathers = characterRepository.findByFamilyAndLevelAndSexAndSpouseIsNotNull(family, family.getLevel() - 1, "male");
        } else {
            fathers = characterRepository.findByFamilyAndLevel(family, family.getLevel());
        }
        model.addAttribute("fathers", fathers);
        System.out.println("END");
        return "admin/test";
    }

    @RequestMapping("/admin/generateImages")
    public String generateImages() {
        logger.debug("Generating images");

        List<SkinColor> skinColorList = app.getSkinColorList(AppearanceService.ALL);
        List<Body> bodyList = app.getBodyList(AppearanceService.ALL);
        List<Ears> earsList = app.getEarsList(AppearanceService.ALL);
        List<Head> headList = app.getHeadList(AppearanceService.ALL);

        List<HairColor> hairColorList = app.getHairColorList(AppearanceService.ALL);

        try {
            List<String> sexList = new ArrayList();
            sexList.add("female");
            sexList.add("male");

            for (String sex : sexList) {
                System.out.println("sex = " + sex);
                /*List<HairStyle> hairStyleList = app.getHairStyleList(sex);
                for (Body body : bodyList) {
                    System.out.println("body = " + body);
                    BufferedImage bodyImageSub = ImageIO.read(ResourceUtils.getFile("classpath:static/graphics/" + sex + "/" + body.getName() + "_sub.png"));
                    for (SkinColor skinColor : skinColorList) {
                        BufferedImage bodyImageSubWithSkinColor = colorImage(bodyImageSub, skinColor.getColor());

                        File outputfile = new File("materials/" + sex + "/" + body.getName() + "_sub_" + skinColor.getNamePart() + ".png");
                        System.out.println("   outputfile = " + outputfile.getPath());
                        ImageIO.write(bodyImageSubWithSkinColor, "png", outputfile);
                    }

                }*/
                for (Ears ears : earsList) {
                    System.out.println("ears = " + ears);
                    BufferedImage earsImageSub = ImageIO.read(ResourceUtils.getFile("classpath:static/graphics/" + sex + "/" + ears.getName() + "_sub.png"));
                    for (SkinColor skinColor : skinColorList) {
                        BufferedImage earsImageSubWithSkinColor = colorImage(earsImageSub, skinColor.getColor());

                        File outputfile = new File("materials/" + sex + "/" + ears.getName() + "_sub_" + skinColor.getNamePart() + ".png");
                        System.out.println("   outputfile = " + outputfile.getPath());
                        ImageIO.write(earsImageSubWithSkinColor, "png", outputfile);
                    }

                }
                /*for (Head head : headList) {
                    System.out.println("head = " + head);
                    BufferedImage headImageSub = ImageIO.read(ResourceUtils.getFile("classpath:static/graphics/" + sex + "/" + head.getName() + "_sub.png"));
                    for (SkinColor skinColor : skinColorList) {
                        BufferedImage headImageSubWithSkinColor = colorImage(headImageSub, skinColor.getColor());

                        File outputfile = new File("materials/" + sex + "/" + head.getName() + "_sub_" + skinColor.getNamePart() + ".png");
                        System.out.println("   outputfile = " + outputfile.getPath());
                        ImageIO.write(headImageSubWithSkinColor, "png", outputfile);
                    }

                }
                for (HairStyle hairStyle : hairStyleList) {
                    System.out.println("hairStyle = " + hairStyle);
                    BufferedImage hairStyleImageSub = ImageIO.read(ResourceUtils.getFile("classpath:static/graphics/" + sex + "/" + hairStyle.getName() + "_sub.png"));
                    for (HairColor hairColor : hairColorList) {
                        BufferedImage hairStyleImageSubWithSkinColor = colorImage(hairStyleImageSub, hairColor.getColor());

                        File outputfile = new File("materials/" + sex + "/" + hairStyle.getName() + "_sub_" + hairColor.getNamePart() + ".png");
                        System.out.println("   outputfile = " + outputfile.getPath());
                        ImageIO.write(hairStyleImageSubWithSkinColor, "png", outputfile);
                    }

                }*/
            }
            System.out.println("GENERATED");

        } catch (IOException e) {
            e.printStackTrace();
        }

        return "redirect:/admin/appearance";
    }

    private BufferedImage colorImage(BufferedImage image, String colorString) {
        int width = image.getWidth();
        int height = image.getHeight();
        WritableRaster raster = image.getRaster();

        for (int xx = 0; xx < width; xx++) {
            for (int yy = 0; yy < height; yy++) {
                int[] pixels = raster.getPixel(xx, yy, (int[]) null);
                pixels[0] = Integer.valueOf(colorString.substring(0, 2), 16);
                pixels[1] = Integer.valueOf(colorString.substring(2, 4), 16);
                pixels[2] = Integer.valueOf(colorString.substring(4, 6), 16);
                raster.setPixel(xx, yy, pixels);
            }
        }
        return image;
    }
}
