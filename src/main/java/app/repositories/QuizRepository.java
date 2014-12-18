package app.repositories;

import app.domain.Quiz;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuizRepository extends JpaRepository<Quiz, Long> {
    public List<Quiz> findByOwner(String owner);
}
