package app.repositories;

import app.domain.PeerReview;
import app.domain.QuizAnswer;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PeerReviewRepository extends JpaRepository<PeerReview, Long> {
    public List<PeerReview> findByQuizAnswer(QuizAnswer answer);
}
