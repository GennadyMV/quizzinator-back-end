package app.repositories;

import app.domain.Admin;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdminRepository extends JpaRepository<Admin, Long> {
    public List<Admin> findByName(String name);
}
