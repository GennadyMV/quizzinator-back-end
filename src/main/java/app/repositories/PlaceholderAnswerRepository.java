package app.repositories;

import app.domain.PlaceholderAnswer;
import app.domain.Quiz;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;


public interface PlaceholderAnswerRepository extends JpaRepository<PlaceholderAnswer, Long> {
    public List<PlaceholderAnswer> findByQuiz(Quiz quiz);
}
