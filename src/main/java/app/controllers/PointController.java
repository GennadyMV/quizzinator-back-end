package app.controllers;

import app.models.QuizPointModel;
import app.models.UserPointModel;
import app.services.PointService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class PointController {
    
    @Autowired
    private PointService pointService;
    
    @ResponseBody
    @RequestMapping(value = "/points/user/{id}", method = RequestMethod.GET)
    public UserPointModel pointsForUser(@PathVariable String id) {
        return pointService.getPointsForUser(id);
    }
    
    @ResponseBody
    @RequestMapping(value = "/points/quiz/{id}", method = RequestMethod.GET)
    public QuizPointModel pointsForQuiz(@PathVariable Long id) {
        return pointService.getPointsForQuiz(id);
    }
}
