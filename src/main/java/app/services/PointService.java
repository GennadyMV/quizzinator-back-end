/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package app.services;

import app.domain.Quiz;
import app.domain.QuizAnswer;
import app.domain.User;
import app.domain.UserPointModel;
import app.repositories.PeerReviewRepository;
import app.repositories.QuizAnswerRepository;
import app.repositories.QuizRepository;
import app.repositories.UserRepository;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 *
 * @author albis
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
    
    public UserPointModel getPointsForUser(String id) {
        User user = userRepo.findOne(id);
        
        UserPointModel userPoint = new UserPointModel(user,
                                        answerRepo.findByUser(user).size(),
                                        reviewRepo.findByReviewer(user).size());
        
        return userPoint;
        
    }
    
    public List<UserPointModel> getPointsForQuiz(Long id) {
        List<UserPointModel> userPoints = new ArrayList<UserPointModel>();
        
        Quiz quiz = quizRepo.findOne(id);
        List<QuizAnswer> quizAnswers = quiz.getQuizAnswers();
        for (int i = 0; i < quizAnswers.size(); i++) {
            QuizAnswer quizAnswer = quizAnswers.get(i);
            User user = quizAnswer.getUser();
            
            UserPointModel userPoint = new UserPointModel(user, 1,
               reviewRepo.findByQuizAnswerAndReviewer(quizAnswer, user).size());
        }
        
        return userPoints;
    }
}
