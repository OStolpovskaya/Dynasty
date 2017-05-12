package dyn.controllers;

/**
 * Created by OM on 17.02.2017.
 */


import dyn.model.*;
import dyn.repository.FamilyRepository;
import dyn.repository.UserRepository;
import dyn.service.*;
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

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Controller
public class HouseController {
    private static final Logger logger = LogManager.getLogger(HouseController.class);
    @Autowired
    HouseService houseService;
    @Autowired
    CraftService craftService;
    @Autowired
    CharacterService characterService;
    @Autowired
    FamilyLogService familyLogService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private FamilyRepository familyRepository;

    @RequestMapping("/game/house")
    public String house(ModelMap model,
                        @RequestParam(value = "family", defaultValue = "0") long familyId,
                        @RequestParam(value = "room", defaultValue = "kitchen") String roomName) {
        User user = userRepository.findByUserName(getAuthUser().getUsername());

        Family family = user.getCurrentFamily();
        model.addAttribute("family", family);

        model.addAttribute("roomList", houseService.getRoomsByHouseId(family.getHouse().getId()));

        List<RoomView> roomViewList = houseService.getRoomMaps(family.getHouse(), family);
        model.addAttribute("roomViewList", roomViewList);

        boolean houseIsFull = true;
        for (RoomView roomView : roomViewList) {
            if (!roomView.isFull()) {
                houseIsFull = false;
            }
        }
        model.addAttribute("houseIsFull", houseIsFull);

        House nextHouse = null;
        if (family.getHouse().hasNextLevel()) {
            nextHouse = houseService.getNextHouse(family.getHouse());
        }
        model.addAttribute("nextHouse", nextHouse);

        return "game/house";
    }


    @RequestMapping(value = "/game/buildings", method = RequestMethod.GET)
    public String buildings(ModelMap model, RedirectAttributes redirectAttributes) {
        User user = userRepository.findByUserName(getAuthUser().getUsername());
        Family family = user.getCurrentFamily();
        model.addAttribute("family", family);

        List<House> buildingList = family.getBuildings();
        model.addAttribute("buildingList", buildingList);

        Map<House, List<RoomView>> buildingMap = new LinkedHashMap<>();
        for (House house : buildingList) {
            buildingMap.put(house, houseService.getRoomMaps(house, family));
        }
        model.addAttribute("buildingMap", buildingMap);

        return "game/buildings";

    }


    @RequestMapping("/game/storage")
    public String storage(ModelMap model, RedirectAttributes redirectAttributes) {
        User user = userRepository.findByUserName(getAuthUser().getUsername());
        Family family = user.getCurrentFamily();
        model.addAttribute("family", family);

        model.addAttribute("itemsInStorage", houseService.getItemsInStorage(family));

        model.addAttribute("serviceAndBuffsInStorage", houseService.getBuffsInStorage(family));
        model.addAttribute("levelCharactersAndTheirWifes", characterService.getLevelCharactersAndSonsWifes(family));

        model.addAttribute("itemsInStore", houseService.getItemsInStore(family));

        return "game/storage";
    }

    @RequestMapping(value = "/game/setItemToThing", method = RequestMethod.POST)
    public String setItemToRoomInterior(ModelMap model, RedirectAttributes redirectAttributes,
                                        @RequestParam(value = "itemId") Long itemId,
                                        @RequestParam(value = "roomThingId") Long roomThingId) {
        User user = userRepository.findByUserName(getAuthUser().getUsername());
        Family family = user.getCurrentFamily();

        Item item = houseService.getItemByFamilyAndItemId(family, itemId);
        RoomThing roomThing = houseService.getRoomThingById(roomThingId);
        if (item != null && roomThing != null) {
            Item oldItem = houseService.getItemByFamilyAndRoomThing(family, roomThing);//itemRepository.findByFamilyAndInteriorId(family, roomThing.getId());
            if (oldItem == null) {
                logger.debug(family.logName() + "has no item with interiorId=" + roomThing.getId());
            } else {
                oldItem.setPlace(ItemPlace.storage);
                oldItem.setInteriorId(0L);
                logger.debug(family.logName() + "has item with interiorId=" + roomThing.getId() + ". Put to storage and set interiorId to 0");
                houseService.saveItem(oldItem);
            }

            item.setPlace(ItemPlace.home);
            String returnTo = "house#room" + roomThing.getRoom().getId();
            if (roomThing.getHouse().getType().equals(HouseType.building)) {
                item.setPlace(ItemPlace.building);
                returnTo = "buildings#building" + roomThing.getHouse().getId();
            }
            item.setInteriorId(roomThing.getId());
            houseService.saveItem(item);

            logger.debug(family.logName() + "set item " + item.getProject().getName() + "(" + item.getId() + ") to room interior thing=" + roomThing.getThing().getName());
            redirectAttributes.addFlashAttribute("mess", "Предмет размещен на месте вещи: " + roomThing.getThing().getName());
            return "redirect:/game/" + returnTo;
        }

        logger.error(family.logName() + "want to set nonexisting item: " + itemId + " or to nonexisting room thing: " + roomThingId);
        redirectAttributes.addFlashAttribute("mess", "Нет такого предмета или непонятно куда ставить");
        return "redirect:/game";
    }

    @RequestMapping(value = "/game/unsetItem", method = RequestMethod.POST)
    public String unsetItem(ModelMap model, RedirectAttributes redirectAttributes,
                            @RequestParam(value = "itemId") Long itemId) {
        User user = userRepository.findByUserName(getAuthUser().getUsername());
        Family family = user.getCurrentFamily();

        Item item = houseService.getItemByFamilyAndItemId(family, itemId);
        if (item != null) {
            String returnTo = "house";
            switch (item.getPlace()) {
                case home:
                    item.setInteriorId(0L);
                    logger.debug(family.logName() + "unset item " + item.getProject().getName() + "(" + item.getId() + ") from room ");
                    redirectAttributes.addFlashAttribute("mess", "Предмет перенесен из дома на склад: " + item.getFullName());
                    returnTo = "house";
                    break;
                case building:
                    item.setInteriorId(0L);
                    logger.debug(family.logName() + "unset item " + item.getProject().getName() + "(" + item.getId() + ") from building ");
                    redirectAttributes.addFlashAttribute("mess", "Предмет перенесен из помещения на склад: " + item.getFullName());
                    returnTo = "buildings";
                    break;
                case store:
                    String mess = "Предмет удален из базы продаж и перенесен на склад: " + item.getFullName() + ". Стоимость: " + item.getCost() + " р.";
                    item.setCost(0);
                    logger.debug(family.logName() + "put item " + item.getProject().getName() + "(" + item.getId() + ") back from store to storage");
                    familyLogService.addToLog(family, mess);
                    redirectAttributes.addFlashAttribute("mess", mess);
                    returnTo = "storage";
                    break;
            }
            item.setPlace(ItemPlace.storage);
            houseService.saveItem(item);
            return "redirect:/game/" + returnTo;
        }
        logger.error(family.logName() + "want to unset nonexisting item: " + itemId);
        redirectAttributes.addFlashAttribute("mess", "Нет такого предмета");
        return "redirect:/game";
    }

    @RequestMapping(value = "/game/destroyItem", method = RequestMethod.POST)
    public String destroyItem(ModelMap model, RedirectAttributes redirectAttributes,
                              @RequestParam(value = "itemId") Long itemId) {
        User user = userRepository.findByUserName(getAuthUser().getUsername());
        Family family = user.getCurrentFamily();

        Item item = houseService.getItemByFamilyAndItemId(family, itemId);
        if (item != null) {
            Project project = item.getProject();
            family.getFamilyResources().addResFromDestroyedItem(item);
            familyRepository.save(family);

            logger.info(family.logName() + " destroy item: " + item.getProject().getName() + "(" + item.getId() + ")");
            String mess = "Вещь разобрана: " + item.getFullName() + ". Получены ресурсы: " + project.resDestroyString();
            familyLogService.addToLog(family, mess);

            houseService.deleteItem(item);

            redirectAttributes.addFlashAttribute("mess", mess);
            return "redirect:/game/storage";
        }
        logger.error(family.logName() + " want to put in store non existing item: " + itemId);
        redirectAttributes.addFlashAttribute("mess", "Вещь не найдена");
        return "redirect:/game/storage";
    }

    @RequestMapping(value = "/game/putItemToStore", method = RequestMethod.POST)
    public String putItemToStore(ModelMap model, RedirectAttributes redirectAttributes,
                                 @RequestParam(value = "itemId") Long itemId,
                                 @RequestParam(value = "cost") int cost) {
        User user = userRepository.findByUserName(getAuthUser().getUsername());
        Family family = user.getCurrentFamily();

        Item item = houseService.getItem(itemId);
        if (item != null) {
            if (family.getItems().contains(item)) {
                if (item.getPlace().equals(ItemPlace.storage)) {
                    item.setPlace(ItemPlace.store);
                    item.setCost(cost);
                    houseService.saveItem(item);
                    logger.info(family.logName() + " put item to store: " + item.getProject().getName() + "(" + item.getId() + ")");
                    String mess = "Вещь выставлена на продажу: " + item.getFullName() + ". Стоимость: " + cost + " р.";
                    familyLogService.addToLog(family, mess);
                    redirectAttributes.addFlashAttribute("mess", mess);
                    return "redirect:/game/storage";
                }
                logger.error(family.logName() + " want to put in store item, which is not in the storage: " + item.getProject().getName() + "(" + item.getId() + "), place=" + item.getPlace().toString());
                redirectAttributes.addFlashAttribute("mess", "Вещь, выставляемая на продажу, должна быть на складе: " + item.getFullName());
                return "redirect:/game/storage";
            }
            logger.error(family.logName() + " has not item: " + item.getProject().getName() + "(" + item.getId() + ")");
            redirectAttributes.addFlashAttribute("mess", "Вы не владеете такой вещью: " + item.getFullName());
            return "redirect:/game/storage";
        }
        logger.error(family.logName() + " want to put in store non existing item: " + itemId);
        redirectAttributes.addFlashAttribute("mess", "Вещь не найдена");
        return "redirect:/game/storage";
    }

    @RequestMapping(value = "/game/chooseItemToBuy", method = RequestMethod.POST)
    public String chooseItemToBuy(ModelMap model, RedirectAttributes redirectAttributes,
                                  @RequestParam(value = "thingId") Long thingId) {
        User user = userRepository.findByUserName(getAuthUser().getUsername());
        Family family = user.getCurrentFamily();

        Thing thing = houseService.getThing(thingId);
        if (thing != null) {
            List<Item> items = houseService.getItemsInStoreByThing(thing);
            model.addAttribute("family", family);
            model.addAttribute("thing", thing);
            model.addAttribute("items", items);
            return "game/chooseItemToBuy";
        }


        logger.error(family.logName() + " want to buy items for non existing thing: " + thingId);
        redirectAttributes.addFlashAttribute("mess", "Предмет не найден");
        return "redirect:/game/house";

    }

    @RequestMapping(value = "/game/chooseProductionToBuy", method = RequestMethod.POST)
    public String chooseProductionToBuy(ModelMap model, RedirectAttributes redirectAttributes,
                                        @RequestParam(value = "projectId") Long projectId) {
        User user = userRepository.findByUserName(getAuthUser().getUsername());
        Family family = user.getCurrentFamily();

        Project project = craftService.getProject(projectId);
        if (project != null) {
            List<Item> items = houseService.getItemsInStoreByProject(project);
            int size = items.size();
            if (size == 0) {
                Family producer = familyRepository.findOne(1L);
                if (projectId.equals(Const.PROJECT_GEN_MOD)) {
                    items = craftService.createItemForStore(project, producer, Const.COST_GEN_MOD);
                }
                if (projectId.equals(Const.PROJECT_FERTILITY)) {
                    items = craftService.createItemForStore(project, producer, Const.COST_FERTILITY);
                }
                if (projectId.equals(Const.PROJECT_FATHER_DOMINANT)) {
                    items = craftService.createItemForStore(project, producer, Const.COST_FATHER_DOMINANT);
                }
                if (projectId.equals(Const.PROJECT_MOTHER_DOMINANT)) {
                    items = craftService.createItemForStore(project, producer, Const.COST_MOTHER_DOMINANT);
                }
            }


            model.addAttribute("family", family);
            model.addAttribute("thing", project.getThing());
            model.addAttribute("project", project);
            model.addAttribute("items", items);
            return "game/chooseItemToBuy";
        }


        logger.error(family.logName() + " want to buy items for non existing project: " + projectId);
        redirectAttributes.addFlashAttribute("mess", "Предмет не найден");
        return "redirect:/game/house";

    }

    @RequestMapping(value = "/game/buyItem", method = RequestMethod.POST)
    public String buyItem(ModelMap model, RedirectAttributes redirectAttributes,
                          @RequestParam(value = "itemId") Long itemId) {
        User user = userRepository.findByUserName(getAuthUser().getUsername());
        Family family = user.getCurrentFamily();

        Item item = houseService.getItem(itemId);
        if (item != null) {
            Family seller = item.getFamily();
            if (seller != family) {
                if (family.getMoney() >= item.getCost()) {
                    seller.setMoney(seller.getMoney() + item.getCost());
                    family.setMoney(family.getMoney() - item.getCost());
                    familyRepository.save(seller);
                    familyRepository.save(family);

                    familyLogService.addToLog(seller, "Семья " + family.getFamilyName() + " приобрела ваш предмет " + item.getFullName() + ". Получено: " + item.getCost() + " р.");

                    String mess = "Вы купили предмет " + item.getFullName() + ". Потрачено: " + item.getCost() + " р.";
                    familyLogService.addToLog(family, mess);

                    item.setFamily(family);
                    item.setPlace(ItemPlace.storage);
                    item.setCost(0);
                    houseService.saveItem(item);

                    logger.info(family.logName() + "buy item: " + item.getId());
                    redirectAttributes.addFlashAttribute("mess", mess);
                    return "redirect:/game/storage";
                }
                logger.error(family.logName() + "want to buy item, but has not enough money: " + item.getId());
                redirectAttributes.addFlashAttribute("mess", "Недостаточно денег для покупки этого предмета");
                return "redirect:/game/house";
            }
            logger.error(family.logName() + "want to buy their own item: " + item.getId());
            redirectAttributes.addFlashAttribute("mess", "Предмет принадлежит Вашей семье и выставлен вами на продажу.");
            return "redirect:/game/storage";
        }
        logger.error(family.logName() + "want to buy non existing item: " + itemId);
        redirectAttributes.addFlashAttribute("mess", "Предмет не найден");
        return "redirect:/game";

    }

    @RequestMapping(value = "/game/buyNewHouse", method = RequestMethod.POST)
    public String buyNewHouse(ModelMap model, RedirectAttributes redirectAttributes) {
        User user = userRepository.findByUserName(getAuthUser().getUsername());
        Family family = user.getCurrentFamily();

        House currentHouse = family.getHouse();

        if (currentHouse.hasNextLevel()) {
            HouseInterior houseInterior = houseService.getHouseInterior(family);
            if (houseInterior.isFull()) {
                House nextHouse = houseService.getNextHouse(currentHouse);
                if (family.getMoney() >= nextHouse.getCost()) {
                    family.setMoney(family.getMoney() - nextHouse.getCost());
                    family.setHouse(nextHouse);

                    family.setCraftPoint(family.getCraftPoint() + 10);

                    familyRepository.save(family);

                    logger.info(family.logName() + "buy house: " + nextHouse.getName());
                    String mess = "Вы купили новый дом " + nextHouse.getName() + ". Потрачено " + nextHouse.getCost() + " р.";
                    familyLogService.addToLog(family, mess);
                    redirectAttributes.addFlashAttribute("mess", mess);
                    return "redirect:/game/house";
                }
                logger.error(family.logName() + "want to buy house, but has not enough money: " + nextHouse.getName());
                redirectAttributes.addFlashAttribute("mess", "Недостаточно денег для покупки этого дома");
                return "redirect:/game/house";
            }
            logger.error(family.logName() + "want to buy house, but current house is not full of items: " + currentHouse.getName());
            redirectAttributes.addFlashAttribute("mess", "Ваш текущий дом недостаточно обставлен, чтобы покупать новый дом.");
            return "redirect:/game/house";
        }
        logger.error(family.logName() + "want to buy house, but there is no next houses : " + currentHouse.getName());
        redirectAttributes.addFlashAttribute("mess", "У вас уже есть самый крутой дом!");
        return "redirect:/game/house";
    }

    private UserDetails getAuthUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetail = (UserDetails) auth.getPrincipal();
        return userDetail;
    }
}
