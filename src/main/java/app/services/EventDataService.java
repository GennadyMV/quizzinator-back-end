package app.services;

import app.domain.EventData;
import app.domain.Quiz;
import app.domain.User;
import app.exceptions.InvalidParameterException;
import app.models.EventDataModel;
import app.models.EventDataModel.Event;
import app.repositories.EventDataRepository;
import app.repositories.QuizRepository;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Handles event tracking data recieved from user
 */
@Component
public class EventDataService {
    
    @Autowired
    private QuizRepository quizRepo;
    
    @Autowired
    private EventDataRepository eventRepo;
    
    @Autowired
    private UserService userService;
    
    /**
     * Get all user's events
     * @param name name of the user
     * @return 
     */
    public List<EventData> getUserEvents(String name) {
        User user = userService.getUser(name);
        
        if (user == null) {
            throw new InvalidParameterException("Invalid username");
        }
        
        return eventRepo.findByUser(user);
    }
    
    /**
     * Get all events related to a quiz
     * @param id quiz's id
     * @return 
     */
    public List<EventData> getQuizEvents(Long id) {
        Quiz quiz = quizRepo.findOne(id);
        
        if (quiz == null) {
            throw new InvalidParameterException("Invalid quiz id");
        }
        
        return eventRepo.findByQuiz(quiz);
    }
    
    /**
     * Save new data recieved from a user
     * @param model a DAO containing events
     */
    public void addEventData(EventDataModel model) {
        User user = userService.getOrCreateUser(model.getUser());
        Quiz quiz = quizRepo.findOne(model.getQuizId());
        Timestamp ts = new Timestamp(new Date().getTime());
        
        for (Event event : model.getEvents()) {
            EventData eventData = new EventData();
            eventData.setUser(user);
            eventData.setQuiz(quiz);
            eventData.setSaveTime(ts);
            
            eventData.setAction(event.getAction());
            eventData.setChildElement(event.getChild());
            eventData.setElement(event.getElement());
            eventData.setValue(event.getValue());
            eventData.setActionTime(event.getActionTime());

            eventRepo.save(eventData);
        }
    }
}