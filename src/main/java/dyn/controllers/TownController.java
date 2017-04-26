package dyn.controllers;

/**
 * Created by OM on 17.02.2017.
 */


import dyn.model.Family;
import dyn.model.Item;
import dyn.model.ItemPlace;
import dyn.model.User;
import dyn.model.buildings.Building;
import dyn.model.buildings.BuildingThing;
import dyn.model.buildings.BuildingThingWithItems;
import dyn.repository.FamilyRepository;
import dyn.repository.ItemRepository;
import dyn.repository.UserRepository;
import dyn.repository.buildings.BuildingRepository;
import dyn.repository.buildings.BuildingThingRepository;
import dyn.service.FamilyLogService;
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
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Controller
public class TownController {
    private static final Logger logger = LogManager.getLogger(TownController.class);
    @Autowired
    BuildingRepository buildingRepository;
    @Autowired
    BuildingThingRepository buildingThingRepository;
    @Autowired
    FamilyLogService familyLogService;
    @Autowired
    private ItemRepository itemRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private FamilyRepository familyRepository;

    @RequestMapping("/game/town")
    public String townView(ModelMap model) {
        User user = userRepository.findByUserName(getAuthUser().getUsername());
        Family family = user.getCurrentFamily();
        model.addAttribute("family", family);
        model.addAttribute("buildingList", buildingRepository.findAll());
        model.addAttribute("familyBuildingList", family.getBuildings());

        return "game/town";
    }

    @RequestMapping(value = "/game/buyBuilding", method = RequestMethod.POST)
    public String buyBuilding(ModelMap model, RedirectAttributes redirectAttributes,
                              @RequestParam(value = "buildingId") Long buildingId) {
        User user = userRepository.findByUserName(getAuthUser().getUsername());
        Family family = user.getCurrentFamily();

        Building building = buildingRepository.findOne(buildingId);
        if (building != null) {
            if (!family.getBuildings().contains(building)) {
                if (family.getMoney() >= building.getCost()) {
                    family.getBuildings().add(building);
                    family.setMoney(family.getMoney() - building.getCost());
                    familyRepository.save(family);

                    logger.info(family.logName() + "buy building: " + building.getName());
                    String mess = "Вы купили здание '" + building.getName() + "'. Потрачено: " + building.getCost() + " р.";
                    familyLogService.addToLog(family, mess);
                    redirectAttributes.addFlashAttribute("mess", mess);
                    return "redirect:/game/town";
                }
                logger.error(family.logName() + " not enough money to buy building: " + building.getName());
                redirectAttributes.addFlashAttribute("mess", "Недостаточно денег для покупки этого здания " + building.getName());
                return "redirect:/game/town";
            }
            logger.error(family.logName() + "want to buy building, but already has one: " + building.getName());
            redirectAttributes.addFlashAttribute("mess", "Вы уже владеете таким зданием");
            return "redirect:/game/town";
        }
        logger.error(family.logName() + "want to buy non existing building: " + buildingId);
        redirectAttributes.addFlashAttribute("mess", "Такого здания не существует");
        return "redirect:/game/town";
    }

    @RequestMapping(value = "/game/buildings", method = RequestMethod.GET)
    public String buildings(ModelMap model, RedirectAttributes redirectAttributes) {
        User user = userRepository.findByUserName(getAuthUser().getUsername());
        Family family = user.getCurrentFamily();
        model.addAttribute("family", family);

        List<Building> buildingList = family.getBuildings();
        model.addAttribute("buildingList", buildingList);

        Map<Building, List<BuildingThingWithItems>> buildingMap = new LinkedHashMap<>();
        for (Building building : buildingList) {
            List<BuildingThing> buildingThingList = buildingThingRepository.findAllByBuilding(building);
            List<BuildingThingWithItems> buildingThingWithItemsList = new ArrayList<>();
            for (BuildingThing buildingThing : buildingThingList) {
                BuildingThingWithItems buildingThingWithItems = new BuildingThingWithItems(buildingThing);

                Item currentItem = null;
                List<Item> availableItems = new ArrayList<>();

                List<Item> itemList = itemRepository.findByFamilyAndProjectThing(family, buildingThing.getThing());
                for (Item availableItem : itemList) {
                    if (availableItem.getPlace().equals(ItemPlace.building) && availableItem.getInteriorId() == buildingThing.getId()) {
                        currentItem = availableItem;
                    }
                    if (availableItem.getPlace().equals(ItemPlace.storage)) {
                        availableItems.add(availableItem);
                    }
                }
                buildingThingWithItems.setCurrentItem(currentItem);
                buildingThingWithItems.setAvailableItems(availableItems);
                buildingThingWithItems.setKnownThing(family.getCraftThings().contains(buildingThing.getThing()));

                buildingThingWithItemsList.add(buildingThingWithItems);
            }
            buildingMap.put(building, buildingThingWithItemsList);
        }
        model.addAttribute("buildingMap", buildingMap);

        return "game/buildings";

    }

    @RequestMapping(value = "/game/setItemToBuildingThing", method = RequestMethod.POST)
    public String setItemToBuildingThing(ModelMap model, RedirectAttributes redirectAttributes,
                                         @RequestParam(value = "buildingThingId") Long buildingThingId,
                                         @RequestParam(value = "itemId") Long itemId) {
        User user = userRepository.findByUserName(getAuthUser().getUsername());
        Family family = user.getCurrentFamily();

        Item item = itemRepository.findByFamilyAndId(family, itemId);
        BuildingThing buildingThing = buildingThingRepository.findOne(buildingThingId);

        if (item != null && buildingThing != null) {
            Item oldItem = itemRepository.findByFamilyAndInteriorId(family, buildingThing.getId());
            if (oldItem != null) {
                oldItem.setPlace(ItemPlace.storage);
                oldItem.setInteriorId(0L);
                logger.debug(family.logName() + "has item with interiorId=" + buildingThing.getId() + ". Put to storage and set interiorId to 0");
                itemRepository.save(oldItem);


            }
            item.setPlace(ItemPlace.building);
            item.setInteriorId(buildingThing.getId());
            itemRepository.save(item);

            logger.info(family.logName() + "set item " + item.getProject().getName() + "(" + item.getId() + ") to building thing=" + buildingThing.getThing().getName());
            String mess = "Вы установили предмет '" + item.getProject().getName() + "'";
            familyLogService.addToLog(family, mess);
            redirectAttributes.addFlashAttribute("mess", mess);
            return "redirect:/game/buildings";
        } else {
            logger.error(family.logName() + "want to set item to building thing: item.id=" + itemId + ", buildingThingId=" + buildingThingId);
        }

        redirectAttributes.addFlashAttribute("mess", "Ошибка при установке предмета.");
        return "redirect:/game/buildings";
    }

    //unsetItemFromBuildingThing
    @RequestMapping(value = "/game/unsetItemFromBuildingThing", method = RequestMethod.POST)
    public String unsetItemFromBuildingThing(ModelMap model, RedirectAttributes redirectAttributes,
                                             @RequestParam(value = "itemId") Long itemId) {
        User user = userRepository.findByUserName(getAuthUser().getUsername());
        Family family = user.getCurrentFamily();

        Item item = itemRepository.findByFamilyAndId(family, itemId);

        if (item != null) {
            item.setPlace(ItemPlace.storage);
            item.setCost(0);
            item.setInteriorId(0L);
            itemRepository.save(item);

            logger.debug(family.logName() + "put item " + item.getProject().getName() + "(" + item.getId() + ") to storage");
            String mess = "Предмет '" + item.getProject().getName() + "' вернулся на склад";
            familyLogService.addToLog(family, mess);
            redirectAttributes.addFlashAttribute("mess", mess);
            return "redirect:/game/buildings";
        }
        redirectAttributes.addFlashAttribute("mess", "Ошибка при убирании предмета на склад");
        return "redirect:/game/buildings";
    }


    private UserDetails getAuthUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetail = (UserDetails) auth.getPrincipal();
        return userDetail;
    }
}
