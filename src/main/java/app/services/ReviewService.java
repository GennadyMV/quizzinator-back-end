package app.services;

import app.domain.PeerReview;
import app.domain.Quiz;
import app.domain.QuizAnswer;
import app.domain.User;
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
    
    public boolean isValidAnswerReviewCombination(Long answerId, Long reviewId) {
        QuizAnswer qa = answerRepo.findOne(answerId);
        PeerReview pr = reviewRepo.findOne(reviewId);
        
        if (qa == null || pr == null || qa.getQuiz() == null || pr.getQuizAnswer().getId().equals(answerId)) {
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
            answersReviews.setReviews(reviewRepo.findByQuizAnswer(answer));
            
            ret.add(answersReviews);
        }
        
        return ret;
    }
    
    public PeerReview saveNewReview(PeerReview review, Long answerId, Long quizId) {
        if (!quizService.isValidAnswerQuizCombination(answerId, quizId)) 
            throw new IllegalArgumentException("bad answerId, quizId combination!");
        
        QuizAnswer qa = answerRepo.findOne(answerId);
        qa.setReviewCount(qa.getReviewCount()+1);
        review.setQuizAnswer(qa);
        
        review.setReviewer(userService.getUser(review.getReviewer()));
        
        PeerReview newReview = reviewRepo.save(review);
        
        return newReview;
    }

    public void rateReview(Long quizId, Long answerId, Long reviewId, String user, Integer rating) {
        if (!quizService.isValidAnswerQuizCombination(answerId, quizId)) 
            throw new IllegalArgumentException("bad answerId, quizId combination!");
        
        if (!this.isValidAnswerReviewCombination(answerId, reviewId))
            throw new IllegalArgumentException("bad answerId, reviewId combination!");
        
        
        if (rating==0)
            throw new IllegalArgumentException("like value must be -1 or 1");
        
        User u = userRepo.findOne(user);
        PeerReview review = reviewRepo.findOne(reviewId);
        
        if (!review.getQuizAnswer().getUser().equals(u))
            throw new IllegalArgumentException("user can only rate reviews given to them");
        
        review.setRating(rating);
        reviewRepo.save(review);
    }
}
