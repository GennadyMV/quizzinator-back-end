package app.controllers;

import app.domain.PeerReview;
import app.domain.QuizAnswer;
import app.domain.User;
import app.exceptions.InvalidParameterException;
import app.models.UsersReviewModel;
import app.repositories.PeerReviewRepository;
import app.repositories.QuizRepository;
import app.repositories.UserRepository;
import app.services.QuizService;
import app.services.ReviewService;
import app.services.UserService;
import java.util.List;
import javax.transaction.Transactional;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class PeerReviewController {
    @Autowired
    private PeerReviewRepository reviewRepo;
    
    @Autowired
    private UserService userService;
    
    @Autowired
    private UserRepository userRepo;
    
    @Autowired
    private QuizRepository quizRepo;
    
    @Autowired
    private QuizService quizService;
    
    @Autowired
    private ReviewService reviewService;
    
    @ResponseBody
    @RequestMapping(value = "/review", method = RequestMethod.GET, produces="application/json")
    @Transactional
    public List<PeerReview> getReviews() {
        return reviewRepo.findAll();
    }
    
    @ResponseBody
    @RequestMapping(value = "/quiz/{quizId}/reviews", method = RequestMethod.GET, produces="application/json")
    @Transactional
    public List<PeerReview> getReviewsByQuiz(@PathVariable Long quizId, @RequestParam Integer reviewCount, @RequestParam String username) {
        return reviewService.getReviewsByQuizForRating(quizId, reviewCount, username);
    }
    
    /**
     * 
     * @param quizId id of the quiz we want answers for
     * @param username the user whose answers' reviews we are interested in
     * @return reviews given to the username
     */
    @ResponseBody
    @RequestMapping(value = "/quiz/{quizId}/myReviews", method = RequestMethod.GET, produces="application/json")
    @Transactional
    public List<PeerReview> getReviewsByQuizAndReviewee(@PathVariable Long quizId, @RequestParam String username) {
        return reviewService.getReviewsByQuizAndReviewee(quizId, username);
    }
    
    @ResponseBody
    @RequestMapping(value = "/quiz/{quizId}/answer/{answerId}/review", method = RequestMethod.GET, produces="application/json")
    @Transactional
    public List<PeerReview> getAnswerReviews(@PathVariable Long quizId, @PathVariable Long answerId) {
        return quizService.getReviewsByAnswer(answerId, quizId);
    }
    
    @ResponseBody
    @RequestMapping(value = "/quiz/{quizId}/review_answers", method = RequestMethod.GET, produces="application/json")
    @Transactional
    public List<QuizAnswer> getAnswersForReview(
            @PathVariable Long quizId, 
            @RequestParam(required = true) String username, 
            @RequestParam(required = false) Integer count) {
        if (count==null) {
            return quizService.getAnswersForReview(quizRepo.findOne(quizId), userService.getOrCreateUser(username));
        } else {
            return quizService.getAnswersForReview(quizRepo.findOne(quizId), userService.getOrCreateUser(username), count);
        }
    }
    
    @ResponseBody
    @RequestMapping(value = "/quiz/{quizId}/answer/{answerId}/review", method = RequestMethod.POST, consumes = "application/json")
    @Transactional
    public void newReview(
            @Valid @RequestBody PeerReview review,
            @PathVariable Long quizId,
            @PathVariable Long answerId) {
        reviewService.saveNewReview(review, answerId, quizId);
    }
    
    @ResponseBody
    @RequestMapping(value = "/reviews/{userhash}", method = RequestMethod.GET, produces = "application/json")
    @Transactional
    public List<UsersReviewModel> userPeerReviews(@PathVariable String userhash) {
        return reviewService.getUserReviews(userhash);
    }
    
    @ResponseBody
    @RequestMapping(value = "/reviews", method = RequestMethod.GET, produces = "application/json")
    @Transactional
    public List<UsersReviewModel> userPeerReviewsByUsername(@RequestParam String username) {
        String userhash = userRepo.findByName(username).getHash();
        return reviewService.getUserReviews(userhash);
    }
    
    @ResponseBody
    @RequestMapping(value = "/quiz/{quizId}/answer/{answerId}/review/{reviewId}/rate", method = RequestMethod.POST)
    @Transactional
    public void rateReview(
            @PathVariable Long quizId, 
            @PathVariable Long answerId, 
            @PathVariable Long reviewId, 
            @RequestParam(required = false) String userhash, 
            @RequestParam(required = false) String username, 
            @RequestParam Integer rating) {
        
        User user;
        if (userhash != null && !userhash.isEmpty()) {
            user = userRepo.findByHash(userhash);
        } else if (username != null) {
            user = userService.getOrCreateUser(username);
        } else {
            throw new InvalidParameterException("username or userhash parameter expected");
        }
        reviewService.rateReview(quizId, answerId, reviewId, user, rating);
    }
    
    @ResponseBody
    @RequestMapping(value = "/preferredUsers", method = RequestMethod.POST, consumes = "application/json")
    @Transactional
    public String addPreferredUsers(@RequestBody List<String> usernames) {
        userService.setUsersWeight(usernames, 2.0);
        return "";
    }
    
    @ResponseBody
    @RequestMapping(value = "/preferredUsers", method = RequestMethod.DELETE, consumes = "application/json")
    @Transactional
    public String deletePreferredUsers(@RequestBody List<String> usernames) {
        userService.setUsersWeight(usernames, 1.0);
        return "";
    }
    
    @ResponseBody
    @RequestMapping(value = "/preferredUsers", method = RequestMethod.GET, produces = "application/json")
    @Transactional
    public List<User> listPreferredUsers() {
        return userService.getUsersWithWeight();
    }
}