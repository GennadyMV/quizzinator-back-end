package app.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Transient;
import org.springframework.data.jpa.domain.AbstractPersistable;

/**
 * Peer review given to an answer.
 * Users can give textual peer reviews to answers submitted by other users
 */
@Entity
@JsonIgnoreProperties(value = "new")
public class PeerReview extends AbstractPersistable<Long> {
    /**
     * The answer this review is directed at.
     */
    @ManyToOne
    @JsonIgnore
    private QuizAnswer quizAnswer;
    
    /**
     * The user who gave this review.
     */
    @ManyToOne
    private User reviewer;
    
    /**
     * Textual review of the answer.
     * No HTML, only text.
     */
    private String review;
    
    /**
     * Peer reviews can be rated as good or bad.
     */
    @JsonIgnore
    @OneToMany(mappedBy = "review")
    private List<ReviewRating> ratings;
    
    @Transient
    private Long rateCount;
    
    @Transient
    private Long totalRating;

    public PeerReview() {
    }
    
    public QuizAnswer getQuizAnswer() {
        return quizAnswer;
    }

    public void setQuizAnswer(QuizAnswer quizAnswer) {
        this.quizAnswer = quizAnswer;
    }

    public User getReviewer() {
        return reviewer;
    }

    public void setReviewer(User reviewer) {
        this.reviewer = reviewer;
    }

    public String getReview() {
        return review;
    }

    public void setReview(String review) {
        this.review = review;
    }
    
    public List<ReviewRating> getRatings() {
        return ratings;
    }

    public void setRatings(List<ReviewRating> ratings) {
        this.ratings = ratings;
    }
    
    public Long getTotalRating() {
        return totalRating == null ? 0 : totalRating;
    }

    public void setTotalRating(Long totalRating) {
        this.totalRating = totalRating;
    }

    public Long getRateCount() {
        return rateCount == null ? 0 : rateCount;
    }

    public void setRateCount(Long rateCount) {
        this.rateCount = rateCount;
    }
    
    public Long getAnswerId() {
        return this.quizAnswer.getId();
    }
}
