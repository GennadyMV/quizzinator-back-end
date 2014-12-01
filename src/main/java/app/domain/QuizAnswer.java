package app.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Date;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.Transient;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.URL;
import org.springframework.data.jpa.domain.AbstractPersistable;

/**
 * Answer of a quiz.
 */
@Entity
@Table(name = "answers")
@JsonIgnoreProperties(value = "new")
public class QuizAnswer extends AbstractPersistable<Long> {
    @ManyToOne
    @JsonIgnore
    private Quiz quiz;
    
    @OneToMany(mappedBy = "quizAnswer", fetch = FetchType.EAGER)
    @JsonIgnore
    private List<PeerReview> peerReviews;
    
    /**
     * The answerer user.
     */
    @JsonIgnore
    @ManyToOne(fetch = FetchType.EAGER)
    private User user;
    
    /**
     * A string filed for username to be used when deserializing quiz from JSON
     */
    @Transient
    private String username;
    
    /**
     * IP address of the user who submitted this answer.
     * Saved for security reasons and to help removing malicious spam
     */
    @JsonIgnore
    private String ip;
    
    /**
     * URL of the answer submitting POST-request.
     */
    @JsonIgnore
    private String url;
    
    /**
     * The answer in JSON format.
     * Contains an array of elements for every Quiz item. Never parsed or 
     * interpreted in back-end. Passed to the front-end as it.
     * Should be lob in database, but H2 requires defining a length
     */
    @Column(length = 40000)
    @NotBlank
    @Lob
    private String answer;
    
    /**
     * Is this answer a placeholder.
     * Placeholder answers can be created by the Quiz administrator to give the
     * first answerers something to give review to.
     */
    @JsonIgnore
    @Column(nullable = false)
    private Boolean placeholder = false;
    
    /**
     * Timestamp of the answer.
     * Set in back-end before persisting.
     */
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date answerDate;
    
    /**
     * Link to user's previous answer.
     * If a user answers multiple times to the quiz, answers are linked together
     */
    @JsonIgnore
    @OneToOne(optional = true)
    private QuizAnswer previousAnswer;

    public Quiz getQuiz() {
        return quiz;
    }

    public void setQuiz(Quiz quiz) {
        this.quiz = quiz;
    }

    public List<PeerReview> getPeerReviews() {
        return peerReviews;
    }

    public void setPeerReviews(List<PeerReview> peerReviews) {
        this.peerReviews = peerReviews;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
    
    public String getIp() {
        return ip;
    }
    
    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
    
    public String getAnswer() {
        return answer;
    }
    
    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public String getUsername() {
        return username;
    }

    @JsonProperty("user")
    public void setUsername(String username) {
        this.username = username;
    }
    
    @JsonProperty("user")
    public String getUsernameForJSON() {
        if (this.user == null) return null;
        return this.user.getName();
    }

    public Boolean getPlaceholder() {
        return placeholder;
    }

    public void setPlaceholder(Boolean placeholder) {
        this.placeholder = placeholder;
    }

    public QuizAnswer getPreviousAnswer() {
        return previousAnswer;
    }

    public void setPreviousAnswer(QuizAnswer previousAnswer) {
        this.previousAnswer = previousAnswer;
    }
    
    @JsonProperty("previousAnswerId")
    public Long getPreviousAnswerId() {
        return this.previousAnswer == null ? null : this.previousAnswer.getId();
    }
    
    @PrePersist
    private void setAnswerDate() {
        this.answerDate = new Date();
    }

    public Date getAnswerDate() {
        return answerDate;
    }
}
