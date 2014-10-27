package app.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import org.springframework.data.jpa.domain.AbstractPersistable;

@Entity
public class ReviewRating extends AbstractPersistable<Long>  {
    @ManyToOne
    @JsonIgnore
    private PeerReview review;
    
    @Max(1)
    @Min(-1)
    private Integer rating = 0;

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
