package app.services;

import app.domain.ClickData;
import app.domain.Quiz;
import app.domain.User;
import app.exceptions.InvalidParameterException;
import app.repositories.ClickDataRepository;
import app.repositories.QuizRepository;
import java.sql.Timestamp;
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
    
    public void addClickData(ClickData clickData) {
        User user = userService.getOrCreateUser(clickData.getUser().getName());
        clickData.setUser(user);
        
        clickData.setQuiz(quizRepo.findOne(clickData.getQuizId()));
        
        Date date = new Date();
        clickData.setSaveTime(new Timestamp(date.getTime()));
        
        clickRepo.save(clickData);
    }
}