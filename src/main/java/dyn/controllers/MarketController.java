package dyn.controllers;

import dyn.model.*;
import dyn.repository.FamilyRepository;
import dyn.repository.UserRepository;
import dyn.service.Const;
import dyn.service.CraftService;
import dyn.service.FamilyLogService;
import dyn.service.HouseService;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

import static dyn.controllers.GameController.getAuthUser;

/**
 * Created by OM on 17.12.2017.
 */
@Controller
public class MarketController {
    private static final Logger logger = LogManager.getLogger(MarketController.class);

    final HouseService houseService;
    final CraftService craftService;
    final FamilyLogService familyLogService;
    private final UserRepository userRepository;
    private final FamilyRepository familyRepository;


    @Autowired
    public MarketController(HouseService houseService, CraftService craftService, FamilyLogService familyLogService, UserRepository userRepository, FamilyRepository familyRepository) {
        this.houseService = houseService;
        this.craftService = craftService;
        this.familyLogService = familyLogService;
        this.userRepository = userRepository;
        this.familyRepository = familyRepository;
    }


    @RequestMapping(value = "/game/chooseItemToBuy", method = RequestMethod.GET)
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


            List<Project> approvedProjects = craftService.getApprovedProjectsByThing(thing);
            approvedProjects.add(0, null);
            model.addAttribute("approvedProjects", approvedProjects);

            ItemRequest itemRequest = new ItemRequest();
            itemRequest.setThing(thing);
            itemRequest.setProject(null);
            itemRequest.setMinQuality(0);
            itemRequest.setDeposit(500);
            model.addAttribute("newItemRequest", itemRequest);

            return "game/chooseItemToBuy";
        }

        logger.error(family.userNameAndFamilyName() + " want to buy items for non existing thing: " + thingId);
        redirectAttributes.addFlashAttribute("mess", "Предмет не найден");
        return "redirect:/game/craft";

    }

    @RequestMapping(value = "/game/chooseProductionToBuy", method = RequestMethod.GET)
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
                items = craftService.createItemForStore(project, producer, project.getCost() * Const.AUTOGEN_PRODUCTION_COST_COEFFICIENT);
            }


            model.addAttribute("family", family);
            model.addAttribute("thing", project.getThing());
            model.addAttribute("project", project);
            model.addAttribute("items", items);
            return "game/chooseProductionToBuy";
        }


        logger.error(family.userNameAndFamilyName() + " want to buy items for non existing project: " + projectId);
        redirectAttributes.addFlashAttribute("mess", "Предмет не найден");
        return "redirect:/game/town";

    }

    @RequestMapping(value = "/game/buyItem", method = RequestMethod.POST)
    public String buyItem(ModelMap model, RedirectAttributes redirectAttributes,
                          @RequestParam(value = "itemId") Long itemId) {
        User user = userRepository.findByUserName(getAuthUser().getUsername());
        Family family = user.getCurrentFamily();

        Item item = houseService.getItem(itemId);
        if (item != null) {
            if (!item.getPlace().equals(ItemPlace.store)) {
                logger.error(family.userNameAndFamilyName() + "want to buy item, but item is not in store: " + item.getId());
                redirectAttributes.addFlashAttribute("mess", "Вещь больше не продается (кто-то уже выкупил или владелец снял с продажи)");
            } else {
                Family seller = item.getFamily();
                if (seller != family) {
                    if (family.getMoney() >= item.getCost()) {
                        seller.setMoney(seller.getMoney() + item.getCost());
                        family.setMoney(family.getMoney() - item.getCost());
                        familyRepository.save(seller);
                        familyRepository.save(family);

                        familyLogService.addToLog(seller, "Семья " + family.link() + " приобрела ваш предмет " + item.getFullName() + ". Получено: " + item.getCost() + " д.");

                        String mess = "Вы купили предмет " + item.getFullName() + ". Потрачено: " + item.getCost() + " д.";
                        familyLogService.addToLog(family, mess);

                        item.setFamily(family);
                        item.setPlace(ItemPlace.storage);
                        item.setCost(0);
                        houseService.saveItem(item);

                        logger.info(family.userNameAndFamilyName() + "buy item: " + item.getId());
                        redirectAttributes.addFlashAttribute("mess", mess);
                    } else {
                        logger.error(family.userNameAndFamilyName() + "want to buy item, but has not enough money: " + item.getId());
                        redirectAttributes.addFlashAttribute("mess", "Недостаточно денег для покупки этого предмета");
                    }
                } else {
                    logger.error(family.userNameAndFamilyName() + "want to buy their own item: " + item.getId());
                    redirectAttributes.addFlashAttribute("mess", "Предмет принадлежит Вашей семье и выставлен вами на продажу.");
                }
            }
            if (craftService.isItemBelongsToBuffAndServices(item)) {
                return "redirect:/game/chooseProductionToBuy?projectId=" + item.getProject().getId();
            }
            return "redirect:/game/chooseItemToBuy?thingId=" + item.getProject().getThing().getId();
        }
        logger.error(family.userNameAndFamilyName() + "want to buy non existing item: " + itemId);
        redirectAttributes.addFlashAttribute("mess", "Предмет не найден");
        return "redirect:/game";

    }

    // /game/addItemRequest
    @RequestMapping(value = "/game/addItemRequest", method = RequestMethod.POST)
    public String addItemRequest(ModelMap model, RedirectAttributes redirectAttributes,
                                 @ModelAttribute("newItemRequest") ItemRequest itemRequest) {
        User user = userRepository.findByUserName(getAuthUser().getUsername());
        Family family = user.getCurrentFamily();

        if (family.getMoney() >= itemRequest.getDeposit()) {

            itemRequest.setFamily(family);
            itemRequest.setStatus(ItemRequestStatus.newRequest);
            craftService.saveItemRequest(itemRequest);

            family.removeMoney(itemRequest.getDeposit());
            familyRepository.save(family);

            String mess = "Подана заявка на изготовление предмета " + itemRequest.desc();

            familyLogService.addToLog(family, mess);
            logger.info(family.userNameAndFamilyName() + mess);
            redirectAttributes.addFlashAttribute("mess", mess);

            return "redirect:/game/storage#itemRequets"; //TODO: сделать табличку на складе
        } else {
            logger.error(family.userNameAndFamilyName() + "tries to add item request, but has not enough money");
            redirectAttributes.addFlashAttribute("mess", "У вас недостаточно денег для подачи заявки.");
            return "redirect:/game/chooseItemToBuy?thingId=" + itemRequest.getThing().getId();
        }

    }

    // /game/removeItemRequest
    @RequestMapping(value = "/game/removeItemRequest", method = RequestMethod.POST)
    public String removeItemRequest(ModelMap model, RedirectAttributes redirectAttributes,
                                    @ModelAttribute("itemRequestId") Long itemRequestId) {
        User user = userRepository.findByUserName(getAuthUser().getUsername());
        Family family = user.getCurrentFamily();

        ItemRequest itemRequest = craftService.getItemRequest(itemRequestId);

        if (itemRequest.getFamily() == family) {
            String mess;
            if (itemRequest.getStatus().equals(ItemRequestStatus.newRequest)) {
                family.addMoney(itemRequest.getDeposit());
                familyRepository.save(family);

                logger.info(family.userNameAndFamilyName() + "deleted not satisfied item request");
                mess = "Вы удалили заявку на изготовление предмета " + itemRequest.desc();
            } else if (itemRequest.getStatus().equals(ItemRequestStatus.done)) {
                logger.info(family.userNameAndFamilyName() + "deleted satisfied item request");
                mess = "Заявка на изготовление предмета выполнена и удалена: " + itemRequest.desc();
            } else {
                logger.error(family.userNameAndFamilyName() + " remove item request with unknown status!");
                mess = "Статус заявки не ясен, но мы все равно удалили эту заявку";
            }
            redirectAttributes.addFlashAttribute("mess", mess);
            familyLogService.addToLog(family, mess);
            craftService.deleteItemRequest(itemRequest);
        } else {
            logger.error(family.userNameAndFamilyName() + "tries to delete item request of another family");
            redirectAttributes.addFlashAttribute("mess", "Эта заявка не принадлежит вашей семье");
        }
        return "redirect:/game/storage#itemRequests";
    }

    // /game/fulfillItemRequest
    @RequestMapping(value = "/game/fulfillItemRequest", method = RequestMethod.POST)
    public String fulfillItemRequest(ModelMap model, RedirectAttributes redirectAttributes,
                                     @ModelAttribute("itemRequestId") Long itemRequestId) {
        User user = userRepository.findByUserName(getAuthUser().getUsername());
        Family family = user.getCurrentFamily();

        ItemRequest itemRequest = craftService.getItemRequest(itemRequestId);
        if (itemRequest != null && itemRequest.getStatus() == ItemRequestStatus.newRequest) {
            Item item = family.itemToFulfillItemRequest(itemRequest);
            if (item != null) {
                Family itemRequestAuthor = itemRequest.getFamily();
                item.setFamily(itemRequestAuthor);
                houseService.saveItem(item);

                itemRequest.setStatus(ItemRequestStatus.done);
                itemRequest.setFulfiller(family);
                craftService.saveItemRequest(itemRequest);

                familyLogService.addToLog(itemRequestAuthor, "Ваша заявка на изготовление предмета " + itemRequest.desc() + " выполнена семьей " + family.link() + ". Получен предмет: " + item.link());

                family.addMoney(itemRequest.getDeposit());
                familyRepository.save(family);

                String mess = "Выполнена заявка семьи " + itemRequestAuthor.link() + " на изготовление предмета: " + itemRequest.desc() + " Передан предмет: " + item.link();
                familyLogService.addToLog(family, mess);
                logger.info(family.userNameAndFamilyName() + "fulfilled the item request: " + itemRequest.desc());
                redirectAttributes.addFlashAttribute("mess", mess);
            } else {
                logger.error(family.userNameAndFamilyName() + "tries to fulfill item request, but has not required item: " + itemRequest.desc());
                redirectAttributes.addFlashAttribute("mess", "На складе нет подходящего под условия заявки предмета.");
            }

        } else {
            logger.error(family.userNameAndFamilyName() + "tries to fulfill nonexisting or satisfied item request: " + itemRequestId);
            redirectAttributes.addFlashAttribute("mess", "Эта заявка была удалена автором или выполнена другим игроком..");
        }
        return "redirect:/game/news";
    }
}

/*Item item = new Item();
        item.setProject(project);
        item.setFamily(family);
        item.setAuthor(familyRepository.findOne(1L));
        item.setPlace(ItemPlace.storage);
        item.setInteriorId(0L);
        item.setCost(0);
        itemRepository.save(item);*/
