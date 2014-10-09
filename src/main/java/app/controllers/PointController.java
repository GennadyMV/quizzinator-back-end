package app.controllers;

import app.domain.UserPointModel;
import app.services.PointService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class PointController {
    
    @Autowired
    private PointService pointService;
    
    @RequestMapping(value = "points/user/{id}", method = RequestMethod.GET)
    public UserPointModel pointsForUser(@PathVariable String id) {
        return pointService.getPointsForUser(id);
    }
    
    @RequestMapping(value = "points/quiz/{id}", method = RequestMethod.GET)
    public List<UserPointModel> pointsForQuiz(@PathVariable Long id) {
        return pointService.getPointsForQuiz(id);
    }
}
