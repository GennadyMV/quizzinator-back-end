package app.controllers;

import app.domain.EventData;
import app.models.EventDataModel;
import app.services.EventDataService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class EventDataController {
    @Autowired
    private EventDataService eventDataService;
    
    @ResponseBody
    @RequestMapping(value = "events/user/{name}", method = RequestMethod.GET)
    public List<EventData> getEventsForUser(@PathVariable String name) {
        return eventDataService.getUserEvents(name);
    }
    
    @ResponseBody
    @RequestMapping(value = "events/quiz/{id}", method = RequestMethod.GET)
    public List<EventData> getEventsForQuiz(@PathVariable Long id) {
        return eventDataService.getQuizEvents(id);
    }
    
    @ResponseBody
    @RequestMapping(value = "events", method = RequestMethod.POST)
    public String addEvents(@RequestBody EventDataModel eventData) {
        eventDataService.addEventData(eventData);
        return "";
    }
}
