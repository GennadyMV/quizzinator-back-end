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
import app.repositories.QuizRepository;
import app.repositories.UserRepository;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

@Component
public class ReviewService {
    @Autowired
    private QuizService quizService;
    
    @Autowired
    private QuizRepository quizRepo;
    
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
        
        User reviewer = userService.getOrCreateUser(review.getReviewer().getName());
        review.setReviewer(reviewer);
        
        PeerReview newReview = reviewRepo.save(review);
        
        return newReview;
    }

    public void rateReview(Long quizId, Long answerId, Long reviewId, User user, Integer rating) {
        if (!quizService.isValidAnswerQuizCombination(answerId, quizId)) {
            throw new InvalidIdCombinationException("bad answerId, quizId combination!");
        }
        
        if (!this.isValidAnswerReviewCombination(answerId, reviewId)) {
            throw new InvalidIdCombinationException("bad answerId, reviewId combination!");
        }
        
        if (rating==0) {
            throw new InvalidParameterException("like value must be -1 or 1");
        }
        
        ReviewRating reviewRating;
        PeerReview review = reviewRepo.findOne(reviewId);
        
        //user shouldn't rate their own review
        if (review.getReviewer().equals(user)) {
            throw new UnauthorizedRateException();
        }
        
        //replace user's old rating
        List<ReviewRating> oldRating = ratingRepo.findByReviewAndRater(review, user);
        if (!oldRating.isEmpty()) {
            reviewRating = oldRating.get(0);
        } else {
            reviewRating = new ReviewRating();
            review.incrementRateCount();
        }
        
        reviewRating.setReview(review);
        reviewRating.setRater(user);
        reviewRating.setRating(rating);
        ratingRepo.save(reviewRating);
        reviewRepo.save(review);
    }

    public List<PeerReview> getReviewsByQuizForRating(Long quizId, Integer reviewCount, String username) {
        User u = userService.getOrCreateUser(username);
        Quiz q = quizRepo.findOne(quizId);
        
        PageRequest pageRequest = new PageRequest(0, reviewCount, Sort.Direction.ASC, "rateCount");
        List<PeerReview> reviews = reviewRepo.findForRate(u, q, pageRequest);
        
        return reviews;
    }
}
