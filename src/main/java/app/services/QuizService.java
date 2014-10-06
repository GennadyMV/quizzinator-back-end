package app.services;

import app.repositories.UserRepository;
import app.domain.PeerReview;
import app.domain.Quiz;
import app.domain.QuizAnswer;
import app.domain.ReviewResponseModel;
import app.domain.User;
import app.repositories.PeerReviewRepository;
import app.repositories.QuizAnswerRepository;
import app.repositories.QuizRepository;
import com.google.gson.Gson;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

@Component
public class QuizService {
    @Autowired
    private QuizAnswerRepository answerRepo;
    
    @Autowired
    private QuizRepository quizRepo;
    
    @Autowired
    private PeerReviewRepository reviewRepo;
    
    @Autowired
    private UserRepository userRepo;
    
    public ReviewResponseModel sumbitAnswer(QuizAnswer answer, Long quizId) {
        User u;
        String username = answer.getUsername();
        System.out.println("submitAnswer called with username: " + username);
        
        List<User> users = userRepo.findByName(username);
        if (users.isEmpty()) {
            u = new User();
            u.setName(username);
            userRepo.save(u);
        } else {
            u = users.get(0);
        }
        
        answer.setUser(u);
        
        Quiz q = quizRepo.findOne(quizId);
        answer.setQuiz(q);
        answerRepo.save(answer);
        
        ReviewResponseModel model = null;
        
        if (q.isReviewable()) {
            model = new ReviewResponseModel();
            model.setAnswerForReview(getAnswersForReview(q, u));
            
            model.setUserhash(u.getHash());
        }
        
        return model;
    }
    
    public List<QuizAnswer> getAnswersForReview(Quiz quiz, User user) {
        int answerCount = 2;
        
        if (quiz.getQuizAnswers().size() >= answerCount) {
            return getAnswersForReview(quiz, user, answerCount);
        } else {
            Gson gson = new Gson();
            return gson.fromJson(quiz.getPlaceholderAnswers(), List.class);
        }
    }
    
    public List<QuizAnswer> getAnswersForReview(Quiz quiz, User user, int answerCount) {
        Pageable pageable = new PageRequest(0, answerCount);
        List<QuizAnswer> qas = answerRepo.findByQuizAndUserNotOrderedByReviewCount(quiz, user, pageable);
        
        return qas;
    }
    
    public List<PeerReview> getReviewsForAnAnswer(Long answerId, Long quizId) {
        if (!isValidAnswerQuizCombination(answerId, quizId)) 
            throw new IllegalArgumentException("bad answerId, quizId combination!");
        
        QuizAnswer qa = answerRepo.findOne(answerId);
        return reviewRepo.findByQuizAnswer(qa);
    }
    
    public PeerReview saveNewReview(PeerReview review, Long answerId, Long quizId) {
        if (!isValidAnswerQuizCombination(answerId, quizId)) 
            throw new IllegalArgumentException("bad answerId, quizId combination!");
        
        QuizAnswer qa = answerRepo.findOne(answerId);
        review.setQuizAnswer(qa);
        
//        User u = review.getReviewer();
        
        PeerReview newReview = reviewRepo.save(review);
        
        return newReview;
    }
    
    private boolean isValidAnswerQuizCombination(Long answerId, Long quizId) {
        QuizAnswer qa = answerRepo.findOne(answerId);
        Quiz q = quizRepo.findOne(quizId);
        
        if (qa == null || q == null || qa.getQuiz() == null || !qa.getQuiz().getId().equals(quizId)) {
            return false;
        } else {
            return true;
        }
    }

    public Quiz getQuizForUsername(Long id, String username) {
        Quiz q = quizRepo.findOne(id);
        User u = userRepo.findOne(username);
        
        if (answerRepo.findByQuizAndUser(q, u).isEmpty()) {
            q.setAnswered(false);
        } else {
            q.setAnswered(true);
        }
            
        return q;
    }
    
    public void addPlaceholderAnswer(QuizAnswer quizAnswer, Long quizId) {
        Quiz quiz = quizRepo.findOne(quizId);
        Gson gson = new Gson();
        
        List<QuizAnswer> placeholderAnswers = gson.fromJson(quiz.getPlaceholderAnswers(), List.class);
        if (placeholderAnswers == null) {
            placeholderAnswers = new ArrayList<QuizAnswer>();
        }
        
        placeholderAnswers.add(quizAnswer);
        quiz.setPlaceholderAnswers(gson.toJson(placeholderAnswers));
    }

    public List<PeerReview> getReviewsByUserHash(String hash) {
        User u = userRepo.findOne(hash);
        
        List<QuizAnswer> answers = answerRepo.findByUser(u);
        
        return reviewRepo.findByQuizAnswerIn(answers);
    }
}
