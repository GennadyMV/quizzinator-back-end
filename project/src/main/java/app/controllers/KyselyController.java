package app.controllers;

import app.domain.Kysely;
import app.repositories.KyselyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class KyselyController {
    @Autowired
    private KyselyRepository kyselyRepo;
    
    @RequestMapping(value = "/kyselyt")
    public String haeKysely(Model model) {
        model.addAttribute("kyselyt", kyselyRepo.findAll());
        return "kyselyt";
    }
    
    @ResponseBody
    @RequestMapping(value = "/kysely/{id}", produces="application/json")
    public Kysely haeKysely(@PathVariable(value = "id") Long id) {
        Kysely k = kyselyRepo.findOne(id);
        
        return k;
    }
    
    @RequestMapping(value = "/kysely", method = RequestMethod.POST)
    public String uusiKysely(@RequestParam(required = true) String kysymys) {
        Kysely k = new Kysely();
        k.setKysymys(kysymys);
        Long id = kyselyRepo.save(k).getId();
        
        return "redirect:/kysely/" + id;
    }
}
