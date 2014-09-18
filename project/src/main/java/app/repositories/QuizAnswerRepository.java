package app.repositories;

import app.domain.Quiz;
import app.domain.QuizAnswer;
import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuizAnswerRepository extends JpaRepository<QuizAnswer, Long> {
    public List<QuizAnswer> findByQuizAndUserNot(Quiz quiz, String user, Pageable pageable);
    public List<QuizAnswer> findByQuiz(Quiz quiz);
}
