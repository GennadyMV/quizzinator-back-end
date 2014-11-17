package app.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.sql.Timestamp;
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
    
    private Timestamp clickTime;
    private String element;
    private String action;
    private String status;
    @JsonProperty("child")
    private String childElement;
    
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

    public Long getQuizId() {
        return quizId;
    }

    public void setQuizId(Long quizId) {
        this.quizId = quizId;
    }
    
    public Timestamp getClickTime() {
        return clickTime;
    }

    public void setClickTime(Timestamp clickTime) {
        this.clickTime = clickTime;
    }

    public String getElement() {
        return element;
    }

    public void setElement(String element) {
        this.element = element;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getChildElement() {
        return childElement;
    }

    public void setChildElement(String childElement) {
        this.childElement = childElement;
    }
}