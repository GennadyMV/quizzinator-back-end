package app.services;

import app.domain.PeerReview;
import app.domain.Quiz;
import app.domain.QuizAnswer;
import app.domain.ReviewRating;
import app.models.QuizPointModel;
import app.domain.User;
import app.exceptions.InvalidParameterException;
import app.models.UserPointModel;
import app.repositories.PeerReviewRepository;
import app.repositories.QuizAnswerRepository;
import app.repositories.QuizRepository;
import app.repositories.ReviewRatingRepository;
import app.repositories.UserRepository;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Service for retrieving points and stats
 */
@Component
public class PointService {
    
    @Autowired
    private QuizRepository quizRepo;
    
    @Autowired
    private QuizAnswerRepository answerRepo;
    
    @Autowired
    private PeerReviewRepository reviewRepo;
    
    @Autowired
    private UserRepository userRepo;
    
    @Autowired
    private ReviewRatingRepository ratingRepo;
    
    /**
     * Find user data and present it in a DAO
     * @param userhash user's hash
     * @return 
     */
    public UserPointModel getPointsForUser(String userhash) {
        User user = userRepo.findByHash(userhash);
        
        if (user == null) {
            throw new InvalidParameterException("Invalid user hash");
        }
        
        return new UserPointModel(user.getName(),
                                answerRepo.findByUser(user).size(),
                                reviewRepo.findByReviewer(user).size(),
                                ratingRepo.findByRater(user).size());
    }
    
    /**
     * Find data about quiz's answers, reviews and review's ratings and
     * return it in a DAO
     * @param id id of the quiz
     * @return 
     */
    public QuizPointModel getPointsForQuiz(Long id) {
        Quiz quiz = quizRepo.findOne(id);
        
        if (quiz == null) {
            throw new InvalidParameterException("Invalid quiz id");
        }
        
        List<String> answerers = new ArrayList<String>();
        List<String> reviewers = new ArrayList<String>();
        List<String> raters = new ArrayList<String>();
        
        List<QuizAnswer> quizAnswers = quiz.getQuizAnswers();
        for (QuizAnswer answer : quizAnswers) {
            if (!answerers.contains(answer.getUser().getName())) {
                answerers.add(answer.getUser().getName());
            }
            
            List<PeerReview> reviews = answer.getPeerReviews();
            for (PeerReview review : reviews) {
                if (!reviewers.contains(review.getReviewer().getName())) {
                    reviewers.add(review.getReviewer().getName());
                }
                List<ReviewRating> ratings = ratingRepo.findByReview(review);
                for (ReviewRating rating : ratings) {
                    if (!raters.contains(rating.getRater().getName())) {
                        raters.add(rating.getRater().getName());
                    }
                }
            }
        }
        
        return new QuizPointModel(quiz, answerers, reviewers, raters);
    }
}
