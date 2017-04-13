package dyn.controllers;

/**
 * Created by OM on 17.02.2017.
 */


import dyn.model.*;
import dyn.repository.FamilyRepository;
import dyn.repository.UserRepository;
import dyn.service.HouseInterior;
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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
public class HouseController {
    private static final Logger logger = LogManager.getLogger(HouseController.class);
    @Autowired
    HouseService houseService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private FamilyRepository familyRepository;

    @RequestMapping("/game/house")
    public String house(ModelMap model,
                        @RequestParam(value = "family", defaultValue = "0") long familyId,
                        @RequestParam(value = "room", defaultValue = "kitchen") String roomName) {
        User user = userRepository.findByUserName(getAuthUser().getUsername());

        Family family;
        if (familyId == 0) {
            family = user.getCurrentFamily();
        } else {
            family = familyRepository.findOne(familyId);
            if (family == null) {
                family = user.getCurrentFamily();
            }
        }
        model.addAttribute("family", family);
        model.addAttribute("itemsInStorage", houseService.getItemsInStorage(family));
        model.addAttribute("itemsInStore", houseService.getItemsInStore(family));

        HouseInterior houseInterior = houseService.getHouseInterior(family);
        model.addAttribute("houseInterior", houseInterior);

        return "game/house";
    }

    @RequestMapping(value = "/game/setItemToThing", method = RequestMethod.POST)
    public String setItemToRoomInterior(ModelMap model, RedirectAttributes redirectAttributes,
                                        @RequestParam(value = "itemId") Long itemId,
                                        @RequestParam(value = "roomInteriorId") Long roomInteriorId) {
        User user = userRepository.findByUserName(getAuthUser().getUsername());
        Family family = user.getCurrentFamily();

        houseService.setItemToRoomInterior(family, itemId, roomInteriorId);

        return "redirect:/game/house";
    }

    @RequestMapping(value = "/game/unsetItem", method = RequestMethod.POST)
    public String unsetItem(ModelMap model, RedirectAttributes redirectAttributes,
                            @RequestParam(value = "itemId") Long itemId) {
        User user = userRepository.findByUserName(getAuthUser().getUsername());
        Family family = user.getCurrentFamily();

        boolean result = houseService.unsetItem(family, itemId);
        if (result) {
            redirectAttributes.addFlashAttribute("mess", "Вещь вернулась на склад");
        } else {
            redirectAttributes.addFlashAttribute("mess", "Ошибка при возвращении вещи на склад!");
        }

        return "redirect:/game/house";
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
                    logger.info(family.getLogName() + " put item to store: " + item.getProject().getName() + "(" + item.getId() + ")");
                    redirectAttributes.addFlashAttribute("mess", "Вещь выставлена на продажу: " + item.getProject().getName() + "(" + item.getId() + ")");
                    return "redirect:/game/house";
                }
                logger.error(family.getLogName() + " want to put in store item, which is not in the storage: " + item.getProject().getName() + "(" + item.getId() + "), place=" + item.getPlace().toString());
                redirectAttributes.addFlashAttribute("mess", "Вещь, выставляемая на продажу, должна быть на складе: " + item.getProject().getName() + "(" + item.getId() + ")");
                return "redirect:/game/house";
            }
            logger.error(family.getLogName() + " has not item: " + item.getProject().getName() + "(" + item.getId() + ")");
            redirectAttributes.addFlashAttribute("mess", "Вы не владеете такой вещью: " + item.getProject().getName() + "(" + item.getId() + ")");
            return "redirect:/game/house";
        }
        logger.error(family.getLogName() + " want to put in store non existing item: " + itemId);
        redirectAttributes.addFlashAttribute("mess", "Вещь не найдена");
        return "redirect:/game/house";
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


        logger.error(family.getLogName() + " want to buy items for non existing thing: " + thingId);
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

                    item.setFamily(family);
                    item.setPlace(ItemPlace.storage);
                    item.setCost(0);
                    houseService.saveItem(item);

                    logger.info(family.getLogName() + "buy item: " + item.getId());
                    redirectAttributes.addFlashAttribute("mess", "Вы купили предмет " + item.getProject().getName() + "(" + item.getProject().getThing().getName() + ")");
                    return "redirect:/game/house";
                }
                logger.error(family.getLogName() + "want to buy item, but has not enough money: " + item.getId());
                redirectAttributes.addFlashAttribute("mess", "Недостаточно денег для покупки этого предмета");
                return "redirect:/game/house";
            }
            logger.error(family.getLogName() + "want to buy their own item: " + item.getId());
            redirectAttributes.addFlashAttribute("mess", "Предмет принадлежит Вашей семье и выставлен вами на продажу.");
            return "redirect:/game/house";
        }
        logger.error(family.getLogName() + "want to buy non existing item: " + itemId);
        redirectAttributes.addFlashAttribute("mess", "Предмет не найден");
        return "redirect:/game/house";

    }

    //buyNewHouse
    @RequestMapping(value = "/game/buyNewHouse", method = RequestMethod.POST)
    public String buyNewHouse(ModelMap model, RedirectAttributes redirectAttributes) {
        User user = userRepository.findByUserName(getAuthUser().getUsername());
        Family family = user.getCurrentFamily();

        House currentHouse = family.getHouse();

        if (currentHouse.nextHouse()) {
            HouseInterior houseInterior = houseService.getHouseInterior(family);
            if (houseInterior.isFull()) {
                House nextHouse = houseService.getNextHouse(currentHouse);
                if (family.getMoney() >= nextHouse.getCost()) {
                    family.setMoney(family.getMoney() - nextHouse.getCost());
                    family.setHouse(nextHouse);

                    family.setCraftPoint(family.getCraftPoint() + 10);

                    familyRepository.save(family);

                    logger.info(family.getLogName() + "buy house: " + nextHouse.getName());
                    redirectAttributes.addFlashAttribute("mess", "Вы купили новый дом " + nextHouse.getName() + ". Потрачено " + nextHouse.getCost() + " р.");
                    return "redirect:/game/house";
                }
                logger.error(family.getLogName() + "want to buy house, but has not enough money: " + nextHouse.getName());
                redirectAttributes.addFlashAttribute("mess", "Недостаточно денег для покупки этого дома");
                return "redirect:/game/house";
            }
            logger.error(family.getLogName() + "want to buy house, but current house is not full of items: " + currentHouse.getName());
            redirectAttributes.addFlashAttribute("mess", "Ваш текущий дом недостаточно обставлен, чтобы покупать новый дом.");
            return "redirect:/game/house";
        }
        logger.error(family.getLogName() + "want to buy house, but there is no next houses : " + currentHouse.getName());
        redirectAttributes.addFlashAttribute("mess", "У вас уже есть самый крутой дом!");
        return "redirect:/game/house";
    }

    private UserDetails getAuthUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetail = (UserDetails) auth.getPrincipal();
        return userDetail;
    }
}
