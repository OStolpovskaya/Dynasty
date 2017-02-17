package dyn.controllers;

/**
 * Created by OM on 17.02.2017.
 */


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Locale;

@Controller
public class IndexController {
    @Autowired
    MessageSource messageSource;

    @RequestMapping("/")
    public String index(ModelMap model) {
        Locale locale = LocaleContextHolder.getLocale();
        System.out.println("Index current locale: " + locale);
        System.out.println(messageSource.getMessage("welcome", null, locale));

        return "index";
    }

    @RequestMapping("/login")
    public String login() {
        return "login";
    }

    @RequestMapping("/about")
    public String about() {
        return "about";
    }

    @RequestMapping("/reg")
    public String reg() {
        return "reg";
    }

}
