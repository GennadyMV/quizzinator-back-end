package app.repositories;

import app.domain.PeerReview;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PeerReviewRepository extends JpaRepository<PeerReview, Long> {
    
}
