package app.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import org.springframework.data.jpa.domain.AbstractPersistable;

@Entity(name = "review_like")
@JsonIgnoreProperties(value = "new")
public class Like extends AbstractPersistable<Long> {
    @Min(-1)
    @Max(1)
    @Column(name = "like_value")
    private Integer value;
    
    @ManyToOne
    private User liker;
    
    @ManyToOne
    private PeerReview review;

    public Integer getValue() {
        return value;
    }

    public void setValue(Integer value) {
        this.value = value;
    }

    public User getLiker() {
        return liker;
    }

    public void setLiker(User liker) {
        this.liker = liker;
    }

    public PeerReview getReview() {
        return review;
    }

    public void setReview(PeerReview review) {
        this.review = review;
    }
}
