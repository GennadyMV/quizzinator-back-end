package app.repositories;

import app.domain.Kysely;
import org.springframework.data.jpa.repository.JpaRepository;

public interface KyselyRepository extends JpaRepository<Kysely, Long> {

}
