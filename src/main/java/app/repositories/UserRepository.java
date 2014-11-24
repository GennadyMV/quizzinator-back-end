package app.repositories;

import app.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;


public interface UserRepository extends JpaRepository<User, String> {
    public User findByName(String username);
    public User findByHash(String hash);
}
