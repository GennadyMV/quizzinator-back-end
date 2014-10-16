package app.services;

import app.repositories.UserRepository;
import app.domain.PeerReview;
import app.domain.Quiz;
import app.domain.QuizAnswer;
import app.models.ReviewResponseModel;
import app.domain.User;
import app.exceptions.InvalidIdCombinationException;
import app.repositories.PeerReviewRepository;
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
    private QuizRepository quizRepo;
    
    @Autowired
    private PeerReviewRepository reviewRepo;
    
    @Autowired
    private UserRepository userRepo;
    
    @Autowired
    private UserService userService;
    
    
    public ReviewResponseModel sumbitAnswer(QuizAnswer answer, Long quizId) {
        User u = userService.getOrCreateUser(answer.getUsername());
        answer.setUser(u);
        
        Quiz q = quizRepo.findOne(quizId);
        answer.setQuiz(q);
        
        QuizAnswer quizAnswer = answerRepo.save(answer);
        
        ReviewResponseModel model = null;
        
        if (q.isReviewable()) {
            model = new ReviewResponseModel();
            model.setAnswers(getAnswersForReview(q, u));
            model.setAnswerId(quizAnswer.getId());
            model.setUserhash(u.getHash());
        }
        
        return model;
    }
    
    public List<QuizAnswer> getAnswersForReview(Quiz quiz, User user) {
        int answerCount = quiz.getReviewRounds()*2;
        return getAnswersForReview(quiz, user, answerCount);
    }
    
    public List<QuizAnswer> getAnswersForReview(Quiz quiz, User user, int answerCount) {
        Pageable pageable = new PageRequest(0, answerCount);
        List<QuizAnswer> qas = answerRepo.findQuizzesToReview(quiz, user, pageable);
        
        return qas;
    }
    
    public List<PeerReview> getReviewsByAnswer(Long answerId, Long quizId) {
        if (!isValidAnswerQuizCombination(answerId, quizId)) {
            throw new InvalidIdCombinationException("bad answerId, quizId combination!");
        }
        
        QuizAnswer qa = answerRepo.findOne(answerId);
        return reviewRepo.findByQuizAnswer(qa);
    }
    
    public void validateAnswerQuizCombination(Long answerId, Long quizId) {
        if (!isValidAnswerQuizCombination(answerId, quizId)) {
            throw new InvalidIdCombinationException("bad answerId, quizId combination!");
        }
    }
    
    public boolean isValidAnswerQuizCombination(Long answerId, Long quizId) {
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
        List<User> users = userRepo.findByName(username);
        
        if (users.isEmpty() || answerRepo.findByQuizAndUser(q, users.get(0)).isEmpty()) {
            q.setAnswered(false);
        } else {
            q.setAnswered(true);
        }
            
        return q;
    }

    public List<PeerReview> getReviewsByUserHash(String hash) {
        User u = userRepo.findOne(hash);
        
        List<QuizAnswer> answers = answerRepo.findByUser(u);
        
        return reviewRepo.findByQuizAnswerIn(answers);
    }

    public void addPlaceHolderAnswer(String quizAnswer, Long quizId) {
        Quiz quiz = quizRepo.findOne(quizId);
        QuizAnswer answer = new QuizAnswer();
        answer.setQuiz(quiz);
        answer.setAnswer(quizAnswer);
        answer.setPlaceholder(Boolean.TRUE);
        
        answerRepo.save(answer);
    }
    
    public List<QuizAnswer> getPlaceholderAnswers(Long quizId) {
        return answerRepo.findByQuizAndPlaceholderIsTrue(quizRepo.findOne(quizId));
    }

    public QuizAnswer deleteAnswer(Long quizId, Long answerId) {
        validateAnswerQuizCombination(answerId, quizId);
        
        QuizAnswer qa = answerRepo.findOne(answerId);
        answerRepo.delete(qa);
        return qa;
    }
}
