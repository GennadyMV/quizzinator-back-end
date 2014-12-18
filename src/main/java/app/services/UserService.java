package app.services;

import app.domain.User;
import app.repositories.UserRepository;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Service for unauthorized user's
 */
@Component
public class UserService {
    @Autowired
    private UserRepository userRepo;
    
    /**
     * Find a user by name.
     * Reutrn null if not found
     * @param username
     * @return 
     */
    public User getUser(String username) {
        if (username==null) return null;
        User u = userRepo.findByName(username);
        return u;
    }
    
    /**
     * Get a user from database.
     * @param user
     * @return 
     */
    public User getUser(User user) {
        if (user == null) return null;
        return userRepo.findByHash(user.getHash());
    }

    /**
     * Get a user from the database by name or create a user and return it
     * @param username
     * @return 
     */
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

    /**
     * Find users who get more reviews than average
     * @return 
     */
    public List<User> getUsersWithWeight() {
        return userRepo.findByReviewWeightGreaterThan(1.0);
    }

    /**
     * Set user's review weight
     * @param usernames
     * @param weight 
     */
    public void setUsersWeight(List<String> usernames, Double weight) {
        List<User> users = new ArrayList<User>(usernames.size());
        for (String username : usernames) {
            User u = getOrCreateUser(username);
            users.add(u);
            u.setReviewWeight(weight);
        }
        userRepo.save(users);
    }
}
