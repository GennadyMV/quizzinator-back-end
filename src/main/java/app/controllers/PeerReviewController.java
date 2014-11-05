package app.controllers;

import app.domain.PeerReview;
import app.domain.QuizAnswer;
import app.models.UsersReviewModel;
import app.repositories.PeerReviewRepository;
import app.repositories.QuizRepository;
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
    @RequestMapping(value = "/reviews/{hash}", method = RequestMethod.GET, produces = "application/json")
    @Transactional
    public List<UsersReviewModel> userPeerReviews(@PathVariable String hash) {
        return reviewService.getUserReviews(hash);
    }
    
    @ResponseBody
    @RequestMapping(value = "/quiz/{quizId}/answer/{answerId}/review/{reviewId}/rate", method = RequestMethod.POST)
    @Transactional
    public void rateReview(
            @PathVariable Long quizId, 
            @PathVariable Long answerId, 
            @PathVariable Long reviewId, 
            @RequestParam String userhash, 
            @RequestParam Integer rating) {
        
        reviewService.rateReview(quizId, answerId, reviewId, userhash, rating);
    }
}