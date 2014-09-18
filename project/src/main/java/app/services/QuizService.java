package app.services;

import app.domain.Quiz;
import app.domain.QuizAnswer;
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
//    @Autowired
//    private PeerReviewRepository reviewRepo;
    
    public List<QuizAnswer> sumbitAnswer(QuizAnswer answer, Long quizId) {
        Quiz q = quizRepo.findOne(quizId);
        answer.setQuiz(q);
        answer = answerRepo.save(answer);
        
        if (true || q.isReviewable()) {
            return getAnswersForReview(q, answer.getUser());
        } else {
            return null;
        }
    }
    
    public List<QuizAnswer> getAnswersForReview(Quiz quiz, String user) {
        return getAnswersForReview(quiz, user, 2);
    }
    
    public List<QuizAnswer> getAnswersForReview(Quiz quiz, String user, int answerCount) {
        Pageable pageable = new PageRequest(0, answerCount);
        return answerRepo.findByQuizAndUserNot(quiz, user, pageable);
    }
}
