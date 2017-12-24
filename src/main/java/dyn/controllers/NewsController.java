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
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Map;

import static dyn.controllers.GameController.loc;

@Controller
public class NewsController {
    private static final Logger logger = LogManager.getLogger(NewsController.class);
    @Autowired
    FamilyLogService familyLogService;
    @Autowired
    HouseService houseService;
    @Autowired
    CraftService craftService;
    @Autowired
    MessageSource messageSource;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private FamilyRepository familyRepository;
    @Autowired
    private AchievementService achService;
    @Autowired
    private TownNewsService townNewsService;

    @RequestMapping("/game/news")
    public String news(ModelMap model, RedirectAttributes redirectAttributes,
                       @PageableDefault(size = 10, page = 0, direction = Sort.Direction.DESC, sort = {"date"}) @Qualifier("townNews") Pageable pageable,
                       @PageableDefault(size = 10, page = 0, direction = Sort.Direction.DESC, sort = {"date"}) @Qualifier("itemRequests") Pageable itemRequestsPageable
    ) {
        User user = userRepository.findByUserName(getAuthUser().getUsername());
        Family family = user.getCurrentFamily();

        if (family == null) {
            logger.debug(user.getUserName() + " doesn't have any family");
            redirectAttributes.addFlashAttribute("mess", messageSource.getMessage("new.user", null, loc()));
            return "redirect:/game/addNewFamily";
        }

        model.addAttribute("family", family);

        Map<User, Integer> acievementRatingMap = achService.getAcievementRatingMap();
        model.addAttribute("acievementRatingMap", acievementRatingMap);

        List<Family> familyMoneyRating = familyRepository.findTop10ByUserTypeOrderByMoneyDesc(UserType.player);
        model.addAttribute("familyMoneyRating", familyMoneyRating);

        List<Family> familyLevelRating = familyRepository.findTop10ByUserTypeOrderByLevelDesc(UserType.player);
        model.addAttribute("familyLevelRating", familyLevelRating);

        Page<TownNews> news = townNewsService.getNews(pageable);
        model.addAttribute("news", news);

        Page<ItemRequest> itemRequests = craftService.getItemRequestsOfFamily(itemRequestsPageable);
        model.addAttribute("itemRequests", itemRequests);

        Map<String, Integer> houseMap = houseService.getHouseStatistics();
        model.addAttribute("houseMap", houseMap);

        Map<String, Integer> buildingMap = houseService.getBuildingStatistics();
        model.addAttribute("buildingMap", buildingMap);

        return "game/news";
    }

    private UserDetails getAuthUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetail = (UserDetails) auth.getPrincipal();
        return userDetail;
    }
}
