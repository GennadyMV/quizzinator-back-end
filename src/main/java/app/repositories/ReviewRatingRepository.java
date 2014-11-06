package app.repositories;

import app.domain.PeerReview;
import app.domain.ReviewRating;
import app.domain.User;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewRatingRepository extends JpaRepository<ReviewRating, Long>  {
    List<ReviewRating> findByRater(User rater);
    List<ReviewRating> findByReview(PeerReview peerReview);
    List<ReviewRating> findByReviewAndRater(PeerReview peerReview, User rater);
}
