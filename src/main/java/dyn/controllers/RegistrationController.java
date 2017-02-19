package dyn.controllers;

/**
 * Created by OM on 17.02.2017.
 */


import dyn.model.User;
import dyn.model.UserRole;
import dyn.repository.UserRepository;
import dyn.repository.UserRolesRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;


@Controller
public class RegistrationController {
    private static final Logger logger = LoggerFactory.getLogger(RegistrationController.class);

    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    UserRepository userRepository;
    @Autowired
    UserRolesRepository userRolesRepository;
    @Autowired
    private AuthenticationManager authenticationManager;

    @GetMapping("/reg")
    public String registration(ModelMap model) {
        model.addAttribute("regUser", new User());

        return "reg";
    }

    @PostMapping("/reg")
    public String reg(ModelMap model, @ModelAttribute("regUser") @Valid User regUser, BindingResult result, HttpServletRequest request) {
        System.out.println(regUser.toString());
        if (!regUser.getPassword().equals(regUser.getPasswordConfirm())) {
            result.rejectValue("passwordConfirm", "password.doNotMatch");
        }
        if (result.hasErrors()) {
            return "reg";
        }

        regUser.setEnabled(true);
        regUser.setPassword(passwordEncoder.encode(regUser.getPassword()));
        System.out.println("SAVE:" + regUser.toString());

        userRepository.save(regUser);
        System.out.println("SAVED:" + regUser.toString());

        UserRole userRole = new UserRole();
        userRole.setUserid(regUser.getUserid());
        userRole.setRole("ROLE_USER");
        System.out.println("SAVE:" + userRole.toString());

        userRolesRepository.save(userRole);

        System.out.println("SAVED:" + userRole.toString());

        System.out.println("Autologin " + regUser.getUserName() + " " + regUser.getPasswordConfirm());
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(regUser.getUserName(), regUser.getPasswordConfirm());

        // generate session if one doesn't exist
        request.getSession();

        token.setDetails(new WebAuthenticationDetails(request));
        Authentication authenticatedUser = authenticationManager.authenticate(token);

        SecurityContextHolder.getContext().setAuthentication(authenticatedUser);

        return "redirect:/game";

    }

}
