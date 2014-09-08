package app.repositories;

import app.domain.OpenQuestion;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OpenQuestionRepository extends JpaRepository<OpenQuestion, Long> {

}
