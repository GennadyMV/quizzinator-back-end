package app.repositories;

import app.domain.ReviewRating;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewRatingRepository extends JpaRepository<ReviewRating, Long>  {
}
