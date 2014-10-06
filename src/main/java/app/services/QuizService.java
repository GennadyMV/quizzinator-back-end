package app.services;

import app.domain.AnswerInterface;
import app.repositories.UserRepository;
import app.domain.PeerReview;
import app.domain.PlaceholderAnswer;
import app.domain.Quiz;
import app.domain.QuizAnswer;
import app.models.ReviewResponseModel;
import app.domain.User;
import app.repositories.PeerReviewRepository;
import app.repositories.PlaceholderAnswerRepository;
import app.repositories.QuizAnswerRepository;
import app.repositories.QuizRepository;
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
    private PlaceholderAnswerRepository placeholderAnswerRepo;
    
    @Autowired
    private QuizRepository quizRepo;
    
    @Autowired
    private PeerReviewRepository reviewRepo;
    
    @Autowired
    private UserRepository userRepo;
    
    public ReviewResponseModel sumbitAnswer(QuizAnswer answer, Long quizId) {
        User u;
        String username = answer.getUsername();
        
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
            model.setAnswerForReview((List<AnswerInterface>) getAnswersForReview(q, u));
            
            model.setUserhash(u.getHash());
        }
        
        return model;
    }
    
    public List<? extends AnswerInterface> getAnswersForReview(Quiz quiz, User user) {
        int answerCount = 2;
        
        if (quiz.getQuizAnswers().size() >= answerCount) {
            return getAnswersForReview(quiz, user, answerCount);
        } else {
            //return placeholder answers
            return placeholderAnswerRepo.findByQuiz(quiz);
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
        
        if (u==null || answerRepo.findByQuizAndUser(q, u).isEmpty()) {
            q.setAnswered(false);
        } else {
            q.setAnswered(true);
        }
            
        return q;
    }
    
    public void addPlaceholderAnswer(String quizAnswer, Long quizId) {
        Quiz quiz = quizRepo.findOne(quizId);
        
        PlaceholderAnswer answer = new PlaceholderAnswer();
        answer.setAnswerData(quizAnswer);
        answer.setQuiz(quiz);
        
        placeholderAnswerRepo.save(answer);
        //quizRepo.save(quiz);
    }

    public List<PeerReview> getReviewsByUserHash(String hash) {
        User u = userRepo.findOne(hash);
        
        List<QuizAnswer> answers = answerRepo.findByUser(u);
        
        return reviewRepo.findByQuizAnswerIn(answers);
    }
}
