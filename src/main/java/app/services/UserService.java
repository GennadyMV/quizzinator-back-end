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
        User u = userRepo.findByName(username);
        return u;
    }
    
    public User getUser(User user) {
        if (user == null) return null;
        return userRepo.findByHash(user.getHash());
    }

    public User getOrCreateUser(String username) {
        User u;
        u = userRepo.findByName(username);
        if (u == null) {
            u = new User();
            u.setName(username);
            u = userRepo.save(u);
        }
        return u;
    }

    public List<User> getUsersWithWeight() {
        return userRepo.findByReviewWeightGreaterThan(1.0);
    }

    public void setUsersWeight(List<User> users, Double weight) {
        for (User user : users) {
            user.setReviewWeight(weight);
        }
        userRepo.save(users);
    }
}
