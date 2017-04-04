package dyn.controllers;

/**
 * Created by OM on 17.02.2017.
 */


import dyn.model.Family;
import dyn.model.Item;
import dyn.model.User;
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

    //putItemToStore
    @RequestMapping(value = "/game/putItemToStore", method = RequestMethod.POST)
    public String putItemToStore(ModelMap model, RedirectAttributes redirectAttributes,
                                 @RequestParam(value = "itemId") Long itemId,
                                 @RequestParam(value = "cost") int cost) {
        // Todo: cost. Новыя таблица? Или новый стоолбец в таблице item?
        User user = userRepository.findByUserName(getAuthUser().getUsername());
        Family family = user.getCurrentFamily();

        Item item = houseService.getItem(itemId);
        if (item != null) {
            if (family.getItems().contains(item)) {
                if (item.getInteriorId() == 0) {
                    houseService.putItemInStore(item);
                    logger.info("Family " + family.getFamilyName() + " put item to store: " + item.getProject().getName() + "(" + item.getId() + ")");
                    redirectAttributes.addFlashAttribute("mess", "Вещь выставлена на продажу: " + item.getProject().getName() + "(" + item.getId() + ")");
                    return "redirect:/game/house";
                }
                logger.error("Family " + family.getFamilyName() + " want to put in store item, which is not in the storage: " + item.getProject().getName() + "(" + item.getId() + "), interior_id=" + item.getInteriorId());
                redirectAttributes.addFlashAttribute("mess", "Вещь, выставляемая на продажу, должна быть на складе: " + item.getProject().getName() + "(" + item.getId() + ")");
                return "redirect:/game/house";
            }
            logger.error("Family " + family.getFamilyName() + " has not item: " + item.getProject().getName() + "(" + item.getId() + ")");
            redirectAttributes.addFlashAttribute("mess", "Вы не владеете такой вещью: " + item.getProject().getName() + "(" + item.getId() + ")");
            return "redirect:/game/house";
        }
        logger.error("Family " + family.getFamilyName() + " want to put in store non existing item: " + itemId);
        redirectAttributes.addFlashAttribute("mess", "Вещь не найдена");
        return "redirect:/game/house";
    }

    private UserDetails getAuthUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetail = (UserDetails) auth.getPrincipal();
        return userDetail;
    }
}
