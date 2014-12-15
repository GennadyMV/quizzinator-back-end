package app.repositories;

import app.domain.PeerReview;
import app.domain.Quiz;
import app.domain.QuizAnswer;
import app.domain.User;
import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PeerReviewRepository extends JpaRepository<PeerReview, Long> {
    public List<PeerReview> findByQuizAnswer(QuizAnswer answer);
    public List<PeerReview> findByQuizAnswerIn(List<QuizAnswer> answers);
    public List<PeerReview> findByReviewer(User reviewer);
    /**
     * Find peer reviews for a user to be rated.
     * This query filters out user's own PeerReviews and PeerReviews already rated by the user
     * @param rater
     * @param quiz
     * @param pageable
     * @return 
     */
    @Query("select pr from PeerReview pr"
            + " left join pr.ratings rr"
            + " inner join pr.quizAnswer qa"
            + " where pr.reviewer <> :user and qa.quiz = :quiz"
            + " and pr not in ("
            + "   select rr2.review from ReviewRating rr2 where rater = :user"
            + ")"
            + " group by pr"
            + " order by count(distinct rr)")
    public List<PeerReview> findForRate(@Param("user") User rater, @Param("quiz") Quiz quiz, Pageable pageable);
}
