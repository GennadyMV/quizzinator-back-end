package app.services;

import app.repositories.UserRepository;
import app.domain.PeerReview;
import app.domain.Quiz;
import app.domain.QuizAnswer;
import app.domain.User;
import app.exceptions.DeadlinePassedException;
import app.exceptions.InvalidIdCombinationException;
import app.models.NewAnswerResponseModel;
import app.models.UserDataModel;
import app.models.UserDataModel.UserData;
import app.repositories.PeerReviewRepository;
import app.repositories.QuizAnswerRepository;
import app.repositories.QuizRepository;
import java.util.List;
import java.util.Date;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
    
    public NewAnswerResponseModel submitAnswer(QuizAnswer answer, Long quizId) {
        User u = userService.getOrCreateUser(answer.getUsername());
        answer.setUser(u);
        
        Quiz q = quizRepo.findOne(quizId);
        answer.setQuiz(q);
        
        //find previous answer and mark this as an improvement
        PageRequest pr = new PageRequest(0, 1, Sort.Direction.DESC, "answerDate");
        List<QuizAnswer> previousAnswers = answerRepo.findByQuizAndUser(q, u, pr);
        
        if (!previousAnswers.isEmpty()) {
            //user has answered before
            if (q.answeringExpired() && !q.improvingPossible()) {
                throw new DeadlinePassedException();
            }
            
            answer.setPreviousAnswer(previousAnswers.get(0));
        } else {
            //user tries to answer the quiz for the first time
            if (q.answeringExpired()) {
                throw new DeadlinePassedException();
            }
            
            answer.setPreviousAnswer(null);
        }
        
        QuizAnswer newAnswer = answerRepo.save(answer);
        
        NewAnswerResponseModel model = null;
        
        if (q.isReviewable()) {
            model = new NewAnswerResponseModel();
            model.setAnswer(newAnswer);
            model.setUserhash(newAnswer.getUser().getHash());
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

    public Quiz getQuizForUsername(Long id, String username) {
        Quiz q = quizRepo.findOne(id);
        User u = userRepo.findByName(username);
        
        if (q == null) {
            throw new app.exceptions.NotFoundException();
        }
        
        if (u == null) {
            q.setAnswered(false);
        } else {
            PageRequest pr = new PageRequest(0, 1, Sort.Direction.DESC, "answerDate");
            List<QuizAnswer> previousAnswers = answerRepo.findByQuizAndUser(q, u, pr);
            if (previousAnswers.isEmpty()) {
                q.setAnswered(false);
            } else {
                q.setAnswered(true);
                q.setMyLatestAnswer(previousAnswers.get(0)); 
            }
        }
            
        return q;
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
        
        //this fixes links, for every answer linking to this, make them link to this.previousAnswer 
        List<QuizAnswer> answers = answerRepo.findByPreviousAnswer(qa);
        for (QuizAnswer answer : answers) {
            answer.setPreviousAnswer(qa.getPreviousAnswer());
        }
        answerRepo.delete(qa);
        return qa;
    }
    
    public UserData[] getUserData(long quizId) {
        UserDataModel model = new UserDataModel();
        Quiz quiz = quizRepo.findOne(quizId);
        List<User> users = answerRepo.findUserByQuiz(quiz);
        
        for(int i=0;i<users.size();i++) {
            User user = users.get(i);
            boolean improved = false;
            QuizAnswer answer = answerRepo.findByQuizAndUserOrderByDateDesc(quiz, user).get(i);
            while(!improved){
                if(answer.getPreviousAnswer() != null && answer.getPeerReviews() != null) {
                    improved = true;
                } else if(answer.getPreviousAnswer() == null) {
                    break;
                }
            }
            model.addData(new UserData(user.getName(), answerRepo.countByUserAndQuiz(user,quiz), improved));
            
        }
        
        return model.getData();
    }
}