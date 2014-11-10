package app.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.OneToMany;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import org.springframework.data.jpa.domain.AbstractPersistable;

@Entity
@JsonIgnoreProperties(value = "new")
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

    private String answerDeadline;

    private String reviewDeadline;

    @Transient
    private boolean answered;

    private Boolean isOpen;

    @Column(nullable = false)
    private Integer reviewRounds = 1;

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
    public boolean isAnsweringExpired() throws ParseException{
        if(this.answerDeadline == null){
          return false;
        }else{
          Date today = new Date();
          Date deadline = new SimpleDateFormat("MM/dd/yyyy", Locale.ENGLISH).parse(this.answerDeadline);

          return (today.getTime() - deadline.getTime() > 0);
        }
    }

    @JsonProperty(value = "reviewingExpired")
    public boolean isReviewingExpired() throws ParseException{
      if(this.reviewDeadline == null){
        return false;
      }else{
        Date today = new Date();
        Date deadline = new SimpleDateFormat("MM/dd/yyyy", Locale.ENGLISH).parse(this.reviewDeadline);

        return (today.getTime() - deadline.getTime() > 0);
      }
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

    public String getAnswerDeadline(){
        return this.answerDeadline;
    }

    public void setAnswerDeadline(String answerDeadline){
        this.answerDeadline = answerDeadline;
    }

    public String getReviewDeadline(){
        return this.reviewDeadline;
    }

    public void setReviewDeadline(String reviewDeadline){
        this.reviewDeadline = reviewDeadline;
    }
}
