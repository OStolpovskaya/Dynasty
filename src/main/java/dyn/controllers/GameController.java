package dyn.controllers;

/**
 * Created by OM on 17.02.2017.
 */


import dyn.model.User;
import dyn.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class GameController {
    @Autowired
    private UserRepository userRepository;

    @RequestMapping("/game")
    public String main(ModelMap model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetail = (UserDetails) auth.getPrincipal();
        System.out.println(userDetail);


        User user = userRepository.findByUserName(userDetail.getUsername());
        System.out.println(user.getUserName());
        return "game";
    }


}
