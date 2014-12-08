package app.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import org.springframework.data.jpa.domain.AbstractPersistable;

/**
 * Peer review given to an answer.
 * Users can give textual peer reviews to answers submitted by other users
 */
@Entity
@JsonIgnoreProperties(value = "new")
public class PeerReview extends AbstractPersistable<Long> {
    /**
     * The answer this review is directed at
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
    @OneToMany(mappedBy = "review", fetch = FetchType.EAGER)
    private List<ReviewRating> ratings;
    
    //TODO: get rid of this. replace with a COUNT query when needed
    @Column(nullable = false)
    private Integer rateCount = 0;
    
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
    
    /**
     * Sums the ratings of this review.
     * If the value is negative, review has received more negative than positive
     * ratings.
     * @return a negative or positive integer. smaller is worse
     */
    public Integer getTotalRating() {
        int total = 0;
        if (ratings != null) {
            for (ReviewRating rating : ratings) {
                total += rating.getRating();
            }
        }
        return total;
    }
    
    public Long getAnswerId() {
        return this.quizAnswer.getId();
    }

    public Integer getRateCount() {
        return rateCount;
}

    public void setRateCount(Integer rateCount) {
        this.rateCount = rateCount;
    }

    public void incrementRateCount() {
        this.rateCount++;
    }
}
