package app.services;

import app.domain.ClickData;
import app.domain.Quiz;
import app.domain.User;
import app.exceptions.InvalidParameterException;
import app.models.ClickDataModel;
import app.models.EventModel;
import app.repositories.ClickDataRepository;
import app.repositories.QuizRepository;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ClickDataService {
    
    @Autowired
    private QuizRepository quizRepo;
    
    @Autowired
    private ClickDataRepository clickRepo;
    
    @Autowired
    private UserService userService;
    
    public List<ClickData> getUserClicks(String name) {
        User user = userService.getUser(name);
        
        if (user == null) {
            throw new InvalidParameterException("Invalid user name");
        }
        
        return clickRepo.findByUser(user);
    }
    
    public List<ClickData> getQuizClicks(Long id) {
        Quiz quiz = quizRepo.findOne(id);
        
        if (quiz == null) {
            throw new InvalidParameterException("Invalid quiz id");
        }
        
        return clickRepo.findByQuiz(quiz);
    }
    
    public void addClickData(ClickDataModel model) {
        User user = userService.getOrCreateUser(model.getUser());
        Quiz quiz = quizRepo.findOne(model.getQuizId());
        Timestamp ts = new Timestamp(new Date().getTime());
        
        for (EventModel event : model.getClicks()) {
            ClickData clickData = new ClickData();
            clickData.setUser(user);
            clickData.setQuiz(quiz);
            clickData.setSaveTime(ts);
            
            clickData.setAction(event.getAction());
            clickData.setChildElement(event.getElement());
            clickData.setElement(event.getElement());
            clickData.setStatus(event.getValue());
            clickData.setClickTime(event.getClickTime());

            clickRepo.save(clickData);
        }
    }
}