package dyn.controllers;

/**
 * Created by OM on 17.02.2017.
 */


import dyn.model.Family;
import dyn.model.User;
import dyn.repository.FamilyRepository;
import dyn.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.List;

@Controller
public class GameController {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private FamilyRepository familyRepository;


    @RequestMapping("/game")
    public String main(ModelMap model) {
        User user = userRepository.findByUserName(getAuthUser().getUsername());
        model.addAttribute("user", user);
        System.out.println(user.getUserName());

        List<Family> families = user.getFamilies();
        if (families.size() == 0) {
            System.out.println("User doesn't have any family");
            return "redirect:/addNewFamily";
        } else {
            System.out.println("User has " + families.size() + " families");
            model.addAttribute("families", families);
        }

        return "game";
    }

    @RequestMapping(value = "/logout", method = RequestMethod.GET)
    public String logout(HttpServletRequest request, HttpServletResponse response) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth != null) {
            new SecurityContextLogoutHandler().logout(request, response, auth);
            request.getSession().invalidate();
        }
        return "redirect:/";
    }

    @GetMapping("/addNewFamily")
    public String createNewFamily(ModelMap model) {
        model.addAttribute("familyForm", new Family());
        return "addNewFamily";
    }

    @PostMapping("/addNewFamily")
    public String addNewFamily(ModelMap model, @ModelAttribute("familyForm") @Valid Family family, BindingResult result) {
        System.out.println("post add new family");

        if (result.hasErrors()) {
            return "addNewFamily";
        }

        User user = userRepository.findByUserName(getAuthUser().getUsername());

        List<Family> families = user.getFamilies();
        for (Family existingFamily : families) {
            existingFamily.setCurrent(false);
            familyRepository.save(existingFamily);
        }

        family.setUser(user);
        family.setCurrent(true);
        family.setLevel(0);
        System.out.println("SAVE:" + family.toString());
        familyRepository.save(family);

        //TODO: generate characters

        return "redirect:/game";
    }


    private UserDetails getAuthUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetail = (UserDetails) auth.getPrincipal();
        System.out.println(userDetail);
        return userDetail;
    }

}
