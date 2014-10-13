
package app.repositories;

import app.domain.Like;
import app.domain.QuizAnswer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LikeRepository extends JpaRepository<Like, Long> {

    public Like findByQuizAnswer(QuizAnswer answer);

}
