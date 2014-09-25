package app.repositories;

import app.domain.Quiz;
import app.domain.QuizAnswer;
import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface QuizAnswerRepository extends JpaRepository<QuizAnswer, Long> {
    public List<QuizAnswer> findByQuizAndUserNot(Quiz quiz, String user, Pageable pageable);
    @Query("select qa from QuizAnswer qa "+
            "left join qa.peerReviews "+
            "where qa.quiz = :quiz and qa.user != :user "+
            "group by qa.id, qa.answer, qa.ip, qa.quiz, qa.url, qa.user " +
            "order by count(1) asc")
    public List<QuizAnswer> findByQuizAndUserNotOrderedByReviewCount(@Param("quiz") Quiz quiz, @Param("user") String user, Pageable pageable);
    public List<QuizAnswer> findByQuiz(Quiz quiz);
    public List<QuizAnswer> findByQuizAndUser(Quiz q, String user);
}
