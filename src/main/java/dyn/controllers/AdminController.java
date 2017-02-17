package dyn.controllers;

/**
 * Created by OM on 17.02.2017.
 */


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class AdminController {

    @RequestMapping("/admin")
    public String admin() {
        return "admin";
    }

}
