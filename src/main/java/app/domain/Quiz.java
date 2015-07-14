package app.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Date;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import org.hibernate.annotations.Type;
import org.springframework.data.jpa.domain.AbstractPersistable;

@Entity
@JsonIgnoreProperties(value = "new", ignoreUnknown = true)
public class Quiz extends AbstractPersistable<Long> {
    @NotNull
    private String title;

    /**
     * A json lob used by the front-end to render quiz fields, questions, items.
     * Back-end never interprets this data, only passed to the front as is
     * Should be lob in database, but H2 requires defining a length
     * Type-annotation is for PostgreSQL
     */
    @Column(length = 4000)
    @Type(type = "text")
    @Lob
    private String items;

    @JsonIgnore
    @OneToMany(mappedBy = "quiz")
    private List<QuizAnswer> quizAnswers;

    /**
     * Quiz answers can be peer reviewed.
     */
    @NotNull
    private boolean reviewable;

    /**
     * Last day to submit answers.
     * Answers can be submitted after this deadline is answer improving is
     * allowed by answerImproveStart and answerImproveDeadline.
     */
    @Temporal(TemporalType.DATE)
    private Date answerDeadline;

    /**
     * Last day to submit reviews for quiz answers.
     */
    @Temporal(TemporalType.DATE)
    private Date reviewDeadline;
    
    /**
     * Answer improving period starts this day.
     * Answers may be improved after the initial deadline (answerDeadline), 
     * but no new answers are allowed after it.
     */
    @Temporal(TemporalType.DATE)
    private Date answerImproveStart;
    
    /**
     * Answer improving period ends this day.
     * After this no answers can be submitted at all.
     */
    @Temporal(TemporalType.DATE)
    private Date answerImproveDeadline;

    /**
     * True if the user has answered the quiz already as least once.
     * Not stored to database, set separately for each request in service.
     */
    @Transient
    private boolean answered;

    /**
     * Quiz-box in site is expanded by default.
     */
    private Boolean isOpen;
    
    /**
     * Link to open when the student is finished with the quiz.
     */
    private String answerForward;
    
    /**
     * Number of answer pairs given to the user to be reviewed.
     * By default user is offered only one pair of answers and they pick one and
     * review it.
     */
    @Column(nullable = false)
    private Integer reviewRounds = 1;
    
    /**
     * User's last answer to this quiz.
     * This is not stored to database, but fetched separately in QuizService if
     * needed
     */
    @Transient
    private QuizAnswer myLatestAnswer;
    
    private String owner;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getItems() {
        return items;
    }

    public void setItems(String items) {
        this.items = items;
    }

    public List<QuizAnswer> getQuizAnswers() {
        return quizAnswers;
    }

    public void setQuizAnswers(List<QuizAnswer> quizAnswers) {
        this.quizAnswers = quizAnswers;
    }

    public boolean isReviewable() {
        return reviewable;
    }

    public void setReviewable(boolean reviewable) {
        this.reviewable = reviewable;
    }

    @JsonProperty(value = "answeringExpired")
    public boolean answeringExpired() {
        if (answerDeadline == null) {
            return false;
        }
        return answerDeadline.before(new Date());
    }

    @JsonProperty(value = "reviewingExpired")
    public boolean reviewingExpired() {
        if (reviewDeadline == null) {
            return false;
        }
        return reviewDeadline.before(new Date());
    }

    @JsonProperty(value = "answerImprovingPossible")
    public boolean improvingPossible() {
        boolean started = false;
        boolean ended = true;
        Date now = new Date();
        
        if (answerImproveStart == null || answerImproveStart.before(now)) {
            started = true;
        }
        
        if (answerImproveDeadline == null || answerImproveDeadline.after(now)) {
            ended = false;
        }
        return started && !ended;
    }
    
    @JsonIgnore
    public void setAnswered(boolean answered) {
        this.answered = answered;
    }

    @JsonProperty(value = "answered")
    public boolean isAnswered() {
        return answered;
    }

    public void setIsOpen(Boolean isOpen) {
        this.isOpen = isOpen;
    }

    public Boolean getIsOpen() {
        return isOpen;
    }

    public Integer getReviewRounds() {
        return reviewRounds;
    }

    public void setReviewRounds(Integer reviewRounds) {
        this.reviewRounds = reviewRounds;
    }
    
    public QuizAnswer getMyLatestAnswer() {
        return myLatestAnswer;
    }
    
    public void setMyLatestAnswer(QuizAnswer myLatestAnswer) {
        this.myLatestAnswer = myLatestAnswer;
    }

    public Date getAnswerDeadline() {
        return answerDeadline;
    }

    public void setAnswerDeadline(Date answerDeadline) {
        this.answerDeadline = answerDeadline;
    }

    public Date getReviewDeadline() {
        return reviewDeadline;
    }

    public void setReviewDeadline(Date reviewDeadline) {
        this.reviewDeadline = reviewDeadline;
    }
    
    public Date getAnswerImproveStart() {
        return answerImproveStart;
    }

    public void setAnswerImproveStart(Date answerImproveStart) {
        this.answerImproveStart = answerImproveStart;
    }

    public Date getAnswerImproveDeadline() {
        return answerImproveDeadline;
    }

    public void setAnswerImproveDeadline(Date answerImproveDeadline) {
        this.answerImproveDeadline = answerImproveDeadline;
    }
    
    public String getOwner() {
        return owner;
    }
    
    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getAnswerForward() {
        return answerForward;
    }

    public void setAnswerForward(String answerForward) {
        this.answerForward = answerForward;
    }
}
