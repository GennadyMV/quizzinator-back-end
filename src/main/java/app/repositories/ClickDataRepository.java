package app.repositories;

import app.domain.ClickData;
import app.domain.User;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClickDataRepository extends JpaRepository<ClickData, Long> {
    List<ClickData> findByUser(User user);
}
