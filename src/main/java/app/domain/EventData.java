package app.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.sql.Timestamp;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import org.springframework.data.jpa.domain.AbstractPersistable;

@Entity
@JsonIgnoreProperties(ignoreUnknown = true) 
public class EventData extends AbstractPersistable<Long> {
    @ManyToOne
    @JsonIgnore
    private Quiz quiz;
    
    @ManyToOne
    private User user;
    
    private Timestamp saveTime;
    
    private Timestamp actionTime;
    private String element;
    @Column(name = "event_action")
    private String action;
    @Column(name = "element_value")
    private String value;
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
        return this.quiz.getId();
    }
    
    public Timestamp getActionTime() {
        return actionTime;
    }

    public void setActionTime(Timestamp actionTime) {
        this.actionTime = actionTime;
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

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getChildElement() {
        return childElement;
    }

    public void setChildElement(String childElement) {
        this.childElement = childElement;
    }
}