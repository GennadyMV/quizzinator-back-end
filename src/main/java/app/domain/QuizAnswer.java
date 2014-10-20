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
    
    @JsonIgnore
    @ManyToOne(fetch = FetchType.EAGER)
    private User user;
    
    @Transient
    private String username;
    
    @JsonIgnore
    private String ip;
    
    @URL
    @JsonIgnore
    private String url;
    
    @NotBlank
    //should be lob, but for H2 length is required
    @Column(length = 4000)
    @Lob
    private String answer;

    @JsonIgnore
    @Column(nullable = false)
    private Integer reviewCount = 0;
    
    @JsonIgnore
    @Column(nullable = false)
    private Boolean placeholder = false;
    
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date answerDate;
    
    @OneToOne(optional = true)
    @JsonIgnore
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

    public Integer getReviewCount() {
        return reviewCount;
    }

    public void setReviewCount(Integer reviewCount) {
        this.reviewCount = reviewCount;
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
