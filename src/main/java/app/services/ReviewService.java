package app.services;

import app.repositories.ReviewRatingRepository;
import app.domain.PeerReview;
import app.domain.Quiz;
import app.domain.QuizAnswer;
import app.domain.ReviewRating;
import app.domain.User;
import app.exceptions.DeadlinePassedException;
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

    public void validateAnswerReviewCombination(Long answerId, Long reviewId) {
        if (!this.isValidAnswerReviewCombination(answerId, reviewId)) {
            throw new InvalidIdCombinationException("bad answerId, reviewId combination!");
        }
    }
    
    public List<UsersReviewModel> getUserReviews(String hash) {
        List<UsersReviewModel> ret = new ArrayList<UsersReviewModel>();
        
        User u = userRepo.findByHash(hash);
        
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
        quizService.validateAnswerQuizCombination(answerId, quizId);
        
        if (quizRepo.findOne(quizId).reviewingExpired()) {
            throw new DeadlinePassedException();
        }
        
        QuizAnswer qa = answerRepo.findOne(answerId);
        review.setQuizAnswer(qa);
        
        User reviewer = userService.getOrCreateUser(review.getReviewer().getName());
        review.setReviewer(reviewer);
        
        PeerReview newReview = reviewRepo.save(review);
        
        return newReview;
    }

    
    /**
     * Saves new peer review rating.
     * Checks for parameter sanity and replaces existing rating if user tries to
     * rate multiple times
     * @param quizId
     * @param answerId
     * @param reviewId
     * @param user
     * @param rating
     */
    public void rateReview(Long quizId, Long answerId, Long reviewId, User user, Integer rating) {
        quizService.validateAnswerQuizCombination(answerId, quizId);
        validateAnswerReviewCombination(answerId, reviewId);
        
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
        
        PageRequest pageRequest = new PageRequest(0, reviewCount);
        List<PeerReview> reviews = reviewRepo.findForRate(u, q, pageRequest);
        
        return fillRateInfoFields(reviews);
    }

    public List<PeerReview> getReviewsByUsername(String username) {
        User u = userService.getOrCreateUser(username);
        
        List<QuizAnswer> answers = answerRepo.findByUser(u);
        List<PeerReview> reviews = reviewRepo.findByQuizAnswerIn(answers);
        
        return fillRateInfoFields(reviews);
    }
    
    public List<PeerReview> getReviewsByAnswer(Long answerId, Long quizId) {
        quizService.validateAnswerQuizCombination(answerId, quizId);
        
        QuizAnswer qa = answerRepo.findOne(answerId);
        return fillRateInfoFields(reviewRepo.findByQuizAnswer(qa));
    }

    /**
     * Finds reviews by reviewee username and quiz id
     * @param quizId
     * @param username reviewee username
     * @return 
     */
    public List<PeerReview> getReviewsByQuizAndReviewee(Long quizId, String username) {
        User u = userService.getOrCreateUser(username);
        Quiz q = quizRepo.findOne(quizId);
        
        List<QuizAnswer> answers = answerRepo.findByQuizAndUser(q, u);
        List<PeerReview> reviews;
        
        if (answers.isEmpty()) {
            //empty list
            reviews = new ArrayList<PeerReview>(0);
        } else {
            reviews = reviewRepo.findByQuizAnswerIn(answers);
        }
        return fillRateInfoFields(reviews);
    }
    
    /**
     * Fills totalRating and ratingCount fields.
     * Gets data for fields from the database for every PeerReview object in list
     * @param reviews list of PeerReviews to handle
     * @return the same list with same objects
     */
    public List<PeerReview> fillRateInfoFields(List<PeerReview> reviews) {
        for (PeerReview review : reviews) {
            review.setTotalRating(ratingRepo.sumRatingByReview(review));
            review.setRateCount(ratingRepo.countByReview(review));
        }
        return reviews;
    }

    /**
     * Get single PeerReview by id.
     * Uses findOne from repo and fills transient fields totalRating and rateCount
     * @param reviewId
     * @return object from database
     */
    public PeerReview getReview(Long reviewId) {
        PeerReview review = reviewRepo.findOne(reviewId);
        review.setTotalRating(ratingRepo.sumRatingByReview(review));
        review.setRateCount(ratingRepo.countByReview(review));
        return review;
    }
}
