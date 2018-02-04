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
import org.springframework.context.MessageSource;
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

import static dyn.controllers.GameController.loc;

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
    MessageSource messageSource;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private FamilyRepository familyRepository;
    @Autowired
    private TownNewsService townNewsService;

    @RequestMapping("/game/house")
    public String house(ModelMap model,
                        @RequestParam(value = "family", defaultValue = "0") long familyId,
                        @RequestParam(value = "room", defaultValue = "kitchen") String roomName,
                        RedirectAttributes redirectAttributes) {
//        long startTime = System.currentTimeMillis();
        User user = userRepository.findByUserName(getAuthUser().getUsername());

        Family family = user.getCurrentFamily();
        if (family == null) {
            logger.debug(user.getUserName() + " doesn't have any family");
            redirectAttributes.addFlashAttribute("mess", messageSource.getMessage("new.user", null, loc()));
            return "redirect:/game/addNewFamily";
        }
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
//        long endTime = System.currentTimeMillis();
//        logger.debug("@House took " + (endTime - startTime) + " milliseconds");
        return "game/house";
    }


    @RequestMapping(value = "/game/buildings", method = RequestMethod.GET)
    public String buildings(ModelMap model, RedirectAttributes redirectAttributes) {
        User user = userRepository.findByUserName(getAuthUser().getUsername());
        Family family = user.getCurrentFamily();
        if (family == null) {
            logger.debug(user.getUserName() + " doesn't have any family");
            redirectAttributes.addFlashAttribute("mess", messageSource.getMessage("new.user", null, loc()));
            return "redirect:/game/addNewFamily";
        }
        model.addAttribute("family", family);

//        List<House> buildingList = family.getBuildings();
        List<FamilyBuilding> buildingList = houseService.getFamilyBuildings(family);
        model.addAttribute("buildingList", buildingList);

        Map<FamilyBuilding, List<RoomView>> buildingMap = new LinkedHashMap<>();
        for (FamilyBuilding familyBuilding : buildingList) {
            House building = familyBuilding.getBuilding();
            buildingMap.put(familyBuilding, houseService.getRoomMaps(building, family));
        }
        model.addAttribute("buildingMap", buildingMap);

        return "game/buildings";

    }


    @RequestMapping("/game/storage")
    public String storage(ModelMap model, RedirectAttributes redirectAttributes) {
        User user = userRepository.findByUserName(getAuthUser().getUsername());
        Family family = user.getCurrentFamily();
        if (family == null) {
            logger.debug(user.getUserName() + " doesn't have any family");
            redirectAttributes.addFlashAttribute("mess", messageSource.getMessage("new.user", null, loc()));
            return "redirect:/game/addNewFamily";
        }
        model.addAttribute("family", family);

        Map<Item, Integer> itemsInStorageCMap = craftService.arrangeItems(houseService.getItemsInStorage(family));
        model.addAttribute("itemsInStorageCMap", itemsInStorageCMap);

        Map<Item, Integer> buffsInStorageCMap = craftService.arrangeItems(houseService.getBuffsInStorage(family));
        model.addAttribute("buffsInStorageCMap", buffsInStorageCMap);

        model.addAttribute("itemsInStore", houseService.getItemsInStore(family));

        model.addAttribute("itemRequests", craftService.getItemRequestsOfFamily(family));

        return "game/storage";
    }

    @RequestMapping(value = "/game/moveItem", method = RequestMethod.POST)
    public String moveItem(ModelMap model, RedirectAttributes redirectAttributes,
                           @RequestParam(value = "itemId") Long itemId,
                           @RequestParam(value = "roomThingId") Long roomThingId,
                           @RequestParam(value = "x") Integer x,
                           @RequestParam(value = "y") Integer y,
                           @RequestParam(value = "layer") Integer layer) {
        User user = userRepository.findByUserName(getAuthUser().getUsername());
        Family family = user.getCurrentFamily();


        Item item = houseService.getItemByFamilyAndItemId(family, itemId);
        if (item != null && (item.getPlace().equals(ItemPlace.home) || item.getPlace().equals(ItemPlace.building))) {
            RoomThing roomThing = houseService.getRoomThingById(roomThingId);
            String returnTo;
            if (roomThing.getHouse().getType().equals(HouseType.building)) {
                returnTo = "buildings#building" + roomThing.getHouse().getId();
            } else {
                returnTo = "house#room" + roomThing.getRoom().getId();
            }
            if (x < 0 || x > 1035 - item.getProject().getThing().getWidth() || y < 0 || y > 405 - item.getProject().getThing().getHeight() || layer <= 0 || layer > 10) {
                logger.error(family.userNameAndFamilyName() + "want to move item with incorrect coordinates: " + itemId + ", x=" + x + ", y=" + y + ", layer=" + layer);
                redirectAttributes.addFlashAttribute("mess", "Вы ввели неправильные координаты для предмета");
            } else {
                item.setX(x);
                item.setY(y);
                item.setLayer(layer);
                houseService.saveItem(item);
                logger.debug(family.userNameAndFamilyName() + "move item with coordinates: " + item.getFullName() + ", x=" + x + ", y=" + y + ", layer=" + layer);
                redirectAttributes.addFlashAttribute("mess", "Предмет " + item.getFullName() + " переставлен");
            }
            return "redirect:/game/" + returnTo;
        } else {
            logger.error(family.userNameAndFamilyName() + "want to set nonexisting item: " + itemId + " or incorrect itemplace");
            redirectAttributes.addFlashAttribute("mess", "Нет такого предмета или непонятно куда ставить");
        }

        return "redirect:/game/";
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
            if (oldItem != null) {
                item.setX(oldItem.getX());
                item.setY(oldItem.getY());
                item.setLayer(oldItem.getLayer());

                oldItem.setX(null);
                oldItem.setY(null);
                oldItem.setLayer(null);
                oldItem.setPlace(ItemPlace.storage);
                oldItem.setInteriorId(0L);
                houseService.saveItem(oldItem);
            } else {
                item.setX(roomThing.getX());
                item.setY(roomThing.getY());
                item.setLayer(roomThing.getLayer());
            }
            String returnTo;
            if (roomThing.getHouse().getType().equals(HouseType.building)) {
                item.setPlace(ItemPlace.building);
                returnTo = "buildings#building" + roomThing.getHouse().getId();
            } else {
                item.setPlace(ItemPlace.home);
                returnTo = "house#room" + roomThing.getRoom().getId();
            }
            item.setInteriorId(roomThing.getId());
            houseService.saveItem(item);

            if (roomThing.getHouse().getType().equals(HouseType.building)) {
                recountBuidingQuality(family, roomThing.getHouse());
            } else {
                recountHouseQuality(family);
            }

            logger.debug(family.userNameAndFamilyName() + "set item " + item.getProject().getName() + "(" + item.getId() + ") to room interior thing=" + roomThing.getThing().getName());
            redirectAttributes.addFlashAttribute("mess", "Предмет размещен на месте вещи: " + roomThing.getThing().getName());
            return "redirect:/game/" + returnTo;
        }

        logger.error(family.userNameAndFamilyName() + "want to set nonexisting item: " + itemId + " or to nonexisting room thing: " + roomThingId);
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
            RoomThing roomThing;
            switch (item.getPlace()) {
                case home:
                    roomThing = houseService.getRoomThingById(item.getInteriorId());
                    item.setX(null);
                    item.setY(null);
                    item.setLayer(null);
                    item.setInteriorId(0L);
                    logger.debug(family.userNameAndFamilyName() + "unset item " + item.getProject().getName() + "(" + item.getId() + ") from room ");
                    redirectAttributes.addFlashAttribute("mess", "Предмет перенесен из дома на склад: " + item.getFullName());
                    item.setPlace(ItemPlace.storage);
                    houseService.saveItem(item);

                    recountHouseQuality(family);
                    returnTo = "house#room" + roomThing.getRoom().getId();
                    break;
                case building:
                    roomThing = houseService.getRoomThingById(item.getInteriorId());
                    item.setX(null);
                    item.setY(null);
                    item.setLayer(null);
                    item.setInteriorId(0L);
                    logger.debug(family.userNameAndFamilyName() + "unset item " + item.getProject().getName() + "(" + item.getId() + ") from building ");
                    redirectAttributes.addFlashAttribute("mess", "Предмет перенесен из помещения на склад: " + item.getFullName());
                    item.setPlace(ItemPlace.storage);
                    houseService.saveItem(item);

                    recountBuidingQuality(family, roomThing.getHouse());
                    returnTo = "buildings#building" + roomThing.getHouse().getId();
                    break;
                case store:
                    String mess = "Предмет удален из базы продаж и перенесен на склад: " + item.getFullName() + ". Стоимость: " + item.getCost() + " д.";
                    item.setCost(0);
                    logger.debug(family.userNameAndFamilyName() + "put item " + item.getProject().getName() + "(" + item.getId() + ") back from store to storage");
                    familyLogService.addToLog(family, mess);
                    redirectAttributes.addFlashAttribute("mess", mess);
                    item.setPlace(ItemPlace.storage);
                    houseService.saveItem(item);

                    returnTo = "storage#inStore";
                    break;
            }
            return "redirect:/game/" + returnTo;
        }
        logger.error(family.userNameAndFamilyName() + "want to unset nonexisting item: " + itemId);
        redirectAttributes.addFlashAttribute("mess", "Нет такого предмета");
        return "redirect:/game";
    }

    private void recountBuidingQuality(Family family, House house) {
        List<RoomView> roomViewList = houseService.getRoomMaps(house, family);
        int numThings = 0;
        int sumItemQuality = 0;
        for (RoomView roomView : roomViewList) {
            List<RoomThingWithItems> roomThingWithItems = roomView.getRoomThingWithItemsList();
            for (RoomThingWithItems roomThingWithItem : roomThingWithItems) {
                numThings += 1;
                if (roomThingWithItem.getCurrentItem() != null) {
                    sumItemQuality += roomThingWithItem.getCurrentItem().getQuality();
                }
            }
        }
        float buildingQuality = (float) sumItemQuality / numThings;
        buildingQuality = (float) Math.round(buildingQuality * 100) / 100;

        houseService.updateBuildingQuality(family, house, buildingQuality);
    }

    private void recountHouseQuality(Family family) {
        List<RoomView> roomViewList = houseService.getRoomMaps(family.getHouse(), family);
        int numThings = 0;
        int sumItemQuality = 0;
        for (RoomView roomView : roomViewList) {
            List<RoomThingWithItems> roomThingWithItems = roomView.getRoomThingWithItemsList();
            for (RoomThingWithItems roomThingWithItem : roomThingWithItems) {
                numThings += 1;
                if (roomThingWithItem.getCurrentItem() != null) {
                    sumItemQuality += roomThingWithItem.getCurrentItem().getQuality();
                }
            }
        }
        float houseQuality = (float) sumItemQuality / numThings;
        houseQuality = (float) Math.round(houseQuality * 100) / 100;
        family.setHouseQuality(houseQuality);
        familyRepository.save(family);
    }

    @RequestMapping(value = "/game/destroyItem", method = RequestMethod.POST)
    public String destroyItem(ModelMap model, RedirectAttributes redirectAttributes,
                              @RequestParam(value = "itemId") Long itemId,
                              @RequestParam(value = "btn", defaultValue = "single") String amount) {
        User user = userRepository.findByUserName(getAuthUser().getUsername());
        Family family = user.getCurrentFamily();

        Item item = houseService.getItemByFamilyAndItemId(family, itemId);
        if (item != null) {
            String mess;
            if (amount.equals("single")) {
                family.getFamilyResources().addResFromDestroyedItem(item);
                familyRepository.save(family);
                houseService.deleteItem(item);
                mess = "Вещь разобрана: " + item.getFullName() + ". Получены ресурсы: " + item.getProject().resDestroyString(1);
            } else {
                List<Item> itemsWhichIsEqualTo = houseService.getItemsWhichIsEqualTo(item);
                for (Item equalItem : itemsWhichIsEqualTo) {
                    family.getFamilyResources().addResFromDestroyedItem(equalItem);
                    familyRepository.save(family);
                    houseService.deleteItem(equalItem);
                }
                int size = itemsWhichIsEqualTo.size();
                mess = "Вещи (" + size + " шт.) разобраны: " + item.getFullName() + ". Получены ресурсы: " + item.getProject().resDestroyString(size);
            }
            logger.info(family.userNameAndFamilyName() + mess);
            familyLogService.addToLog(family, mess);
            redirectAttributes.addFlashAttribute("mess", mess);
            return "redirect:/game/storage";
        }
        logger.error(family.userNameAndFamilyName() + " want to put in store non existing item: " + itemId);
        redirectAttributes.addFlashAttribute("mess", "Вещь не найдена");
        return "redirect:/game/storage";
    }

    @RequestMapping(value = "/game/putItemToStore", method = RequestMethod.POST)
    public String putItemToStore(ModelMap model, RedirectAttributes redirectAttributes,
                                 @RequestParam(value = "itemId") Long itemId,
                                 @RequestParam(value = "cost") int cost,
                                 @RequestParam(value = "btn", defaultValue = "single") String amount) {
        User user = userRepository.findByUserName(getAuthUser().getUsername());
        Family family = user.getCurrentFamily();

        Item item = houseService.getItemOfFamilyInPlace(itemId, family, ItemPlace.storage);
        if (item != null) {
            String mess;
            if (amount.equals("single")) {
                item.setPlace(ItemPlace.store);
                item.setCost(cost);
                houseService.saveItem(item);
                mess = "Вещь выставлена на продажу: " + item.getFullName() + ". Стоимость: " + cost + " д.";
            } else {
                List<Item> itemsWhichIsEqualTo = houseService.getItemsWhichIsEqualTo(item);
                for (Item equalItem : itemsWhichIsEqualTo) {
                    equalItem.setPlace(ItemPlace.store);
                    equalItem.setCost(cost);
                    houseService.saveItem(equalItem);
                }
                mess = "Вещи (" + itemsWhichIsEqualTo.size() + " шт.) выставлены на продажу: " + item.getFullName() + ". Стоимость: " + cost + " д.";
            }
            logger.info(family.userNameAndFamilyName() + mess);
            familyLogService.addToLog(family, mess);
            redirectAttributes.addFlashAttribute("mess", mess);
            return "redirect:/game/storage";
        }
        logger.error(family.userNameAndFamilyName() + " want to put in store non existing item: " + itemId);
        redirectAttributes.addFlashAttribute("mess", "Вещь не найдена");
        return "redirect:/game/storage";
    }

    @RequestMapping(value = "/game/buyNewHouse", method = RequestMethod.POST)
    public String buyNewHouse(ModelMap model, RedirectAttributes redirectAttributes) {
        User user = userRepository.findByUserName(getAuthUser().getUsername());
        Family family = user.getCurrentFamily();

        House currentHouse = family.getHouse();

        if (currentHouse.hasNextLevel()) {
            List<RoomView> roomViewList = houseService.getRoomMaps(currentHouse, family);
            boolean houseIsFull = true;
            for (RoomView roomView : roomViewList) {
                if (!roomView.isFull()) {
                    houseIsFull = false;
                }
            }
            if (houseIsFull) {
                House nextHouse = houseService.getNextHouse(currentHouse);
                if (family.getMoney() >= nextHouse.getCost()) {
                    family.setMoney(family.getMoney() - nextHouse.getCost());
                    family.setHouse(nextHouse);

                    family.setCraftPoint(family.getCraftPoint() + 10);

                    familyRepository.save(family);

                    logger.info(family.userNameAndFamilyName() + "buy house: " + nextHouse.getName());
                    String mess = "Вы купили новый дом " + nextHouse.getName() + ". Потрачено " + nextHouse.getCost() + " д.";
                    familyLogService.addToLog(family, mess);
                    townNewsService.addNewHouseNews(family);
                    redirectAttributes.addFlashAttribute("mess", mess);
                    return "redirect:/game/house";
                }
                logger.error(family.userNameAndFamilyName() + "want to buy house, but has not enough money: " + nextHouse.getName());
                redirectAttributes.addFlashAttribute("mess", "Недостаточно денег для покупки этого дома");
                return "redirect:/game/house";
            }
            logger.error(family.userNameAndFamilyName() + "want to buy house, but current house is not full of items: " + currentHouse.getName());
            redirectAttributes.addFlashAttribute("mess", "Ваш текущий дом недостаточно обставлен, чтобы покупать новый дом.");
            return "redirect:/game/house";
        }
        logger.error(family.userNameAndFamilyName() + "want to buy house, but there is no next houses : " + currentHouse.getName());
        redirectAttributes.addFlashAttribute("mess", "У вас уже есть самый крутой дом!");
        return "redirect:/game/house";
    }

    private UserDetails getAuthUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetail = (UserDetails) auth.getPrincipal();
        return userDetail;
    }
}
