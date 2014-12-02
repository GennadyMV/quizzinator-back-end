package app.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import org.springframework.data.jpa.domain.AbstractPersistable;

/**
 * Rating of a peer review.
 * Effectively 1 or -1
 */
@Entity
public class ReviewRating extends AbstractPersistable<Long> {
    @ManyToOne
    @JsonIgnore
    private PeerReview review;
    
    /**
     * The rating.
     * 1 implicates positive feedback and -1 negative feedback. These are shown
     * as thumbs in the web page.
     */
    @Max(1)
    @Min(-1)
    private Integer rating = 0;

    /**
     * User who rated the review.
     * Every user can rate a review only once.
     */
    @ManyToOne
    private User rater;

    public PeerReview getReview() {
        return review;
    }

    public void setReview(PeerReview review) {
        this.review = review;
    }

    public Integer getRating() {
        return rating;
    }

    public void setRating(Integer rating) {
        if (rating == 0) throw new IllegalArgumentException();
        this.rating = rating;
    }

    public User getRater() {
        return rater;
    }

    public void setRater(User rater) {
        this.rater = rater;
    }
}
