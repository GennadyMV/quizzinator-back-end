package app.repositories;

import app.domain.ClickData;
import app.domain.Quiz;
import app.domain.User;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClickDataRepository extends JpaRepository<ClickData, Long> {
    public List<ClickData> findByUser(User user);
    public List<ClickData> findByQuiz(Quiz quiz);
}
