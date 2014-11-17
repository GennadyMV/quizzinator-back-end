package app.controllers;

import app.domain.ClickData;
import app.models.ClickDataModel;
import app.services.ClickDataService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class ClickDataController {
    @Autowired
    private ClickDataService clickDataService;
    
    @ResponseBody
    @RequestMapping(value = "clicks/user/{name}", method = RequestMethod.GET)
    public List<ClickData> getClicksForUser(@PathVariable String name) {
        return clickDataService.getUserClicks(name);
    }
    
    @ResponseBody
    @RequestMapping(value = "clicks/quiz/{id}", method = RequestMethod.GET)
    public List<ClickData> getClicksForQuiz(@PathVariable Long id) {
        return clickDataService.getQuizClicks(id);
    }
    
    @ResponseBody
    @RequestMapping(value = "clicks", method = RequestMethod.POST)
    public String addClicks(@RequestBody ClickDataModel clickData) {
        clickDataService.addClickData(clickData);
        return "";
    }
}
