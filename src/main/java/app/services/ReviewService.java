package app.services;

import app.repositories.ReviewRatingRepository;
import app.domain.PeerReview;
import app.domain.Quiz;
import app.domain.QuizAnswer;
import app.domain.ReviewRating;
import app.domain.User;
import app.exceptions.InvalidIdCombinationException;
import app.exceptions.InvalidParameterException;
import app.exceptions.UnauthorizedRateException;
import app.models.UsersReviewModel;
import app.repositories.PeerReviewRepository;
import app.repositories.QuizAnswerRepository;
import app.repositories.UserRepository;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ReviewService {
    @Autowired
    private QuizService quizService;
    
    @Autowired
    private QuizAnswerRepository answerRepo;
    
    @Autowired
    private PeerReviewRepository reviewRepo;
    @Autowired
    
    private UserRepository userRepo;
    
    @Autowired
    private UserService userService;
    
    @Autowired
    private ReviewRatingRepository ratingRepo;
    
    public boolean isValidAnswerReviewCombination(Long answerId, Long reviewId) {
        QuizAnswer qa = answerRepo.findOne(answerId);
        PeerReview pr = reviewRepo.findOne(reviewId);
        
        if (qa == null || pr == null || pr.getQuizAnswer() == null || !pr.getQuizAnswer().getId().equals(answerId)) {
            return false;
        } else {
            return true;
        }
    }
    
    public List<UsersReviewModel> getUserReviews(String hash) {
        List<UsersReviewModel> ret = new ArrayList<UsersReviewModel>();
        
        User u = userRepo.findOne(hash);
        
        List<QuizAnswer> userAnswers = answerRepo.findByUser(u);
        
        for (QuizAnswer answer : userAnswers) {
            Quiz quiz = answer.getQuiz();
            
            UsersReviewModel answersReviews = new UsersReviewModel();
            answersReviews.setQuizId(quiz.getId());
            answersReviews.setTitle(quiz.getTitle());
            answersReviews.setAnswer(answer);
            
            List<PeerReview> reviews = reviewRepo.findByQuizAnswer(answer);
            answersReviews.setReviews(reviews);
            
            ret.add(answersReviews);
        }
        
        return ret;
    }
    
    public PeerReview saveNewReview(PeerReview review, Long answerId, Long quizId) {
        if (!quizService.isValidAnswerQuizCombination(answerId, quizId)) {
            throw new InvalidIdCombinationException("bad answerId, quizId combination!");
        }
        
        QuizAnswer qa = answerRepo.findOne(answerId);
        qa.setReviewCount(qa.getReviewCount()+1);
        review.setQuizAnswer(qa);
        
        review.setReviewer(userService.getUser(review.getReviewer()));
        
        PeerReview newReview = reviewRepo.save(review);
        
        return newReview;
    }

    public void rateReview(Long quizId, Long answerId, Long reviewId, String user, Integer rating) {
        if (!quizService.isValidAnswerQuizCombination(answerId, quizId)) {
            throw new InvalidIdCombinationException("bad answerId, quizId combination!");
        }
        
        if (!this.isValidAnswerReviewCombination(answerId, reviewId)) {
            throw new InvalidIdCombinationException("bad answerId, reviewId combination!");
        }
        
        if (rating==0) {
            throw new InvalidParameterException("like value must be -1 or 1");
        }
        
        
        ReviewRating reviewRating = new ReviewRating();
        User u = userRepo.findOne(user);
        PeerReview review = reviewRepo.findOne(reviewId);
        
        if (!review.getQuizAnswer().getUser().equals(u)) {
            throw new UnauthorizedRateException();
        }
        
        reviewRating.setReview(review);
        reviewRating.setRater(u);
        reviewRating.setRating(rating);
        ratingRepo.save(reviewRating);
        reviewRepo.save(review);
    }
}
