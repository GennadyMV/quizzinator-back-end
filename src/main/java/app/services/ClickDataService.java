package app.services;

import app.domain.ClickData;
import app.domain.User;
import app.repositories.ClickDataRepository;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ClickDataService {
    
    @Autowired
    private ClickDataRepository clickRepo;
    
    @Autowired
    private UserService userService;
    
    public List<ClickData> getUserClicks(String name) {
        User user = userService.getUser(name);
        return clickRepo.findByUser(user);
    }
    
    public void addClickData(ClickData clickData) {
        User user = userService.getOrCreateUser(clickData.getUser().getName());
        clickData.setUser(user);
        
        Date date = new Date();
        clickData.setSaveTime(new Timestamp(date.getTime()));
    }
}