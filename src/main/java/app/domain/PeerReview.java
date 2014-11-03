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

@Entity
@JsonIgnoreProperties(value = "new")
public class PeerReview extends AbstractPersistable<Long> {
    @ManyToOne
    @JsonIgnore
    private QuizAnswer quizAnswer;
    
    @ManyToOne
    private User reviewer;
    
    private String review;
    
    @JsonIgnore
    @OneToMany(mappedBy = "review", fetch = FetchType.EAGER)
    private List<ReviewRating> ratings;

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
    
    public Integer getTotalRating() {
        int total = 0;
        for (ReviewRating rating : ratings) {
            total += rating.getRating();
        }
        return total;
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
