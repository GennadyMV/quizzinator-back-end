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
import org.springframework.data.jpa.domain.AbstractPersistable;

@Entity
@JsonIgnoreProperties(value = "new", ignoreUnknown = true)
public class Quiz extends AbstractPersistable<Long> {
    @NotNull
    private String title;

    //should be lob, but for H2 length is required
    @Column(length = 4000)
    @Lob
    private String items;

    @JsonIgnore
    @OneToMany(mappedBy = "quiz")
    private List<QuizAnswer> quizAnswers;

    @NotNull
    private boolean reviewable;

    @Temporal(TemporalType.DATE)
    private Date answerDeadline;

    @Temporal(TemporalType.DATE)
    private Date reviewDeadline;
    
    @Temporal(TemporalType.DATE)
    private Date answerImproveStart;
    
    @Temporal(TemporalType.DATE)
    private Date answerImproveDeadline;

    @Transient
    private boolean answered;

    private Boolean isOpen;

    @Column(nullable = false)
    private Integer reviewRounds = 1;
    
    @Transient
    private QuizAnswer myLatestAnswer;

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

    @JsonProperty(value = "answered")
    public boolean isAnswered() {
        return answered;
    }

    @JsonProperty(value = "answeringExpired")
    public boolean answeringExpired() {
        return this.answerDeadline.before(new Date());
    }

    @JsonProperty(value = "reviewingExpired")
    public boolean reviewingExpired() {
        return this.reviewDeadline.before(new Date());
    }

    @JsonProperty(value = "answerImprovingPossible")
    public boolean answerImprovable() {
        Date now = new Date();
        return this.answerImproveStart.before(now) && this.answerImproveDeadline.before(now);
    }
    
    @JsonIgnore
    public void setAnswered(boolean answered) {
        this.answered = answered;
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
}
