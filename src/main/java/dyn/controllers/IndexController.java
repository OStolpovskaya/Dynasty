package dyn.controllers;

/**
 * Created by OM on 17.02.2017.
 */


import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.ErrorAttributes;
import org.springframework.boot.autoconfigure.web.ErrorController;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Locale;
import java.util.Map;


@Controller
public class IndexController implements ErrorController {
    private static final String PATH = "/error";
    private static final Logger logger = LogManager.getLogger(IndexController.class);
    @Autowired
    MessageSource messageSource;
    @Autowired
    private ErrorAttributes errorAttributes;

    @RequestMapping("/")
    public String index() {
        Locale locale = LocaleContextHolder.getLocale();
        logger.debug("Index current locale: " + locale + ". Message: " + messageSource.getMessage("welcome", null, locale));
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

    @RequestMapping(value = PATH)
    public String error(ModelMap model, HttpServletRequest request, RedirectAttributes redirectAttributes) {
        Map<String, Object> errorAttributes = getErrorAttributes(request, false);
        System.out.println("ERROR HANDLING");
        for (String key : errorAttributes.keySet()) {
            System.out.println("key = " + key + ": value = " + errorAttributes.get(key));
        }
        model.addAttribute("reqAttr", errorAttributes);
        if (errorAttributes.get("error").equals("Forbidden")) {
            redirectAttributes.addFlashAttribute("mess", "Необходима авторизация!");
            return "redirect:/login";
        }
        return "errorHandling";
    }

    @Override
    public String getErrorPath() {
        return PATH;
    }

    private Map<String, Object> getErrorAttributes(HttpServletRequest request, boolean includeStackTrace) {
        RequestAttributes requestAttributes = new ServletRequestAttributes(request);
        return errorAttributes.getErrorAttributes(requestAttributes, includeStackTrace);
    }
}
