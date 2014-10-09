package app.repositories;

import app.domain.PeerReview;
import app.domain.QuizAnswer;
import app.domain.User;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PeerReviewRepository extends JpaRepository<PeerReview, Long> {
    public List<PeerReview> findByQuizAnswer(QuizAnswer answer);
    public List<PeerReview> findByQuizAnswerIn(List<QuizAnswer> answers);
    public List<PeerReview> findByReviewer(User reviewer);
    public List<PeerReview> findByQuizAnswerAndReviewer(QuizAnswer quizAnswer, User reviewer);
}
