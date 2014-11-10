package app.domain;

import app.models.ClickModel;
import java.sql.Timestamp;
import java.util.List;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import org.springframework.data.jpa.domain.AbstractPersistable;

@Entity
public class ClickData extends AbstractPersistable<Long> {
    
    @ManyToOne
    private Quiz quiz;
    
    @ManyToOne
    private User user;
    
    private Timestamp saveTime;
    
    @ElementCollection(targetClass=ClickModel.class)
    private List<ClickModel> clicks;
    
    public Quiz getQuiz() {
        return quiz;
    }
    
    public void setQuiz(Quiz quiz) {
        this.quiz = quiz;
    }
    
    public User getUser() {
        return user;
    }
    
    public void setUser(User user) {
        this.user = user;
    }
    
    public Timestamp getSaveTime() {
        return saveTime;
    }
    
    public void setSaveTime(Timestamp saveTime) {
        this.saveTime = saveTime;
    }
    
    public List<ClickModel> getClicks() {
        return clicks;
    }
    
    public void setClicks(List<ClickModel> clicks) {
        this.clicks = clicks;
    }
}
