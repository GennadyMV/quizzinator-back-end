package app.repositories;

import app.domain.EventData;
import app.domain.Quiz;
import app.domain.User;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EventDataRepository extends JpaRepository<EventData, Long> {
    public List<EventData> findByUser(User user);
    public List<EventData> findByQuiz(Quiz quiz);
}
