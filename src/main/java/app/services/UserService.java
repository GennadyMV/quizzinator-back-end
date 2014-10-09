package app.services;

import app.domain.User;
import app.repositories.UserRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UserService {
    @Autowired
    private UserRepository userRepo;
    
    public User getUser(String username) {
        if (username==null) return null;
        List<User> users = userRepo.findByName(username);
        return users.isEmpty() ? null : users.get(0);
    }
    
    public User getUser(User user) {
        if (user == null) return null;
        return userRepo.findOne(user.getId());
    }

    public User getOrCreateUser(String username) {
        User u;
        List<User> users = userRepo.findByName(username);
        if (users.isEmpty()) {
            u = new User();
            u.setName(username);
            u = userRepo.save(u);
        } else {
            u = users.get(0);
        }
        return u;
    }

}
