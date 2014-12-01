package app.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.sql.Timestamp;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import org.springframework.data.jpa.domain.AbstractPersistable;

/**
 * Class for tracking user events in front-end
 */
@Entity
@JsonIgnoreProperties(ignoreUnknown = true)
public class EventData extends AbstractPersistable<Long> {
    /**
     * All events are related to a single quiz.
     */
    @ManyToOne
    @JsonIgnore
    private Quiz quiz;
    
    /**
     * Events are performed by some user.
     */
    @ManyToOne
    private User user;
    
    /**
     * Server time when events were received and saved.
     */
    private Timestamp saveTime;
    
    /**
     * Timestamp of the action, reported by user.
     * This is based on client machine's clock and can't be trusted.
     * Can be used for ordering events.
     */
    private Timestamp actionTime;
    
    /**
     * User performs an action on some element.
     * e.g. open_question_1, scale_question_3, window
     */
    private String element;
    
    /**
     * The action user did to the element.
     * e.g. change, click, focus, blur, close
     */
    @Column(name = "event_action")
    private String action;
    
    /**
     * Value of the element after the action.
     * e.g. text in open question box, numeric value of the clicked box in scale question
     */
    @Column(name = "element_value")
    private String value;
    
    /**
     * Some elements have child elements.
     * This is used e.g. for the index of the child of scale question.
     */
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