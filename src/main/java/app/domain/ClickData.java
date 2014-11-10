package app.domain;

import app.models.ClickModel;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.sql.Timestamp;
import java.util.List;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;
import org.springframework.data.jpa.domain.AbstractPersistable;

@Entity
@JsonIgnoreProperties(ignoreUnknown = true) 
public class ClickData extends AbstractPersistable<Long> {
    @ManyToOne
    private Quiz quiz;
    
    @Transient
    private Long quizId;
    
    @ManyToOne
    private User user;
    
    private Timestamp saveTime;
    
    @ElementCollection
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

    public Long getQuizId() {
        return quizId;
    }

    public void setQuizId(Long quizId) {
        this.quizId = quizId;
    }
}
