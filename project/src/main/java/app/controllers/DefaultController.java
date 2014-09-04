package app.controllers;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class DefaultController {
    @Value("${application.message}")
    private String defaultMessage;
    
    @ResponseBody
    @RequestMapping(value = "/*")
    public String showMessage() {
        return defaultMessage;
    }
}
