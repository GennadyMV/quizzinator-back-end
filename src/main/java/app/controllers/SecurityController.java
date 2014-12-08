package app.controllers;

import app.domain.Admin;
import app.repositories.AdminRepository;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class SecurityController {
    
    @Autowired
    private AdminRepository adminRepo;
    
    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String login(Model model,
                @RequestParam(required = false) String error,
		@RequestParam(required = false) String logout) {
        
        if (error != null) {
            model.addAttribute("error", "Invalid username and password!");
        }
        if (logout != null) {
            model.addAttribute("msg", "You've been logged out successfully.");
        }

        return "login";
    }
    
    @RequestMapping(value = "/registration", method = RequestMethod.GET)
    public String registration() {
        return "registration";
    }
    
    @RequestMapping(value = "/registration", method = RequestMethod.POST)
    public String registerAdmin(HttpServletRequest request,
                                @RequestParam String username,
                                @RequestParam String password,
                                @RequestParam String confirmPassword) {
        
        if (!password.equals(confirmPassword)) {
            request.setAttribute("error", "Confirm password didn't match!");
            return "registration";
        }
        if (!adminRepo.findByName(username).isEmpty())  {
            request.setAttribute("error", "Username is already taken!");
            return "registration";
        }
        
        Admin admin = new Admin();
        admin.setName(username);
        admin.setPassword(password);
        
        adminRepo.save(admin);
        
        return "redirect:/login";
    }
}