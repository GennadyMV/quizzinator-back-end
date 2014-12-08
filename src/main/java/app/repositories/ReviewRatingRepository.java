package app.repositories;

import app.domain.PeerReview;
import app.domain.ReviewRating;
import app.domain.User;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ReviewRatingRepository extends JpaRepository<ReviewRating, Long>  {
    List<ReviewRating> findByRater(User rater);
    List<ReviewRating> findByReview(PeerReview peerReview);
    List<ReviewRating> findByReviewAndRater(PeerReview peerReview, User rater);
    
    public Long countByReview(PeerReview peerReview);
    @Query("select sum(rr.rating) from ReviewRating rr where review = :review")
    public Long sumRatingByReview(@Param("review") PeerReview peerReview);
}
