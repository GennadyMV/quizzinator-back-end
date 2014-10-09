/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package app.services;

import app.domain.PeerReview;
import app.domain.Quiz;
import app.domain.QuizAnswer;
import app.models.QuizPointModel;
import app.domain.User;
import app.models.UserPointModel;
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
        
        return new UserPointModel(user.getName(),
                                answerRepo.findByUser(user).size(),
                                reviewRepo.findByReviewer(user).size());
        
    }
    
    public QuizPointModel getPointsForQuiz(Long id) {
        Quiz quiz = quizRepo.findOne(id);
        List<String> answerers = new ArrayList<String>();
        List<String> reviewers = new ArrayList<String>();
        
        List<QuizAnswer> quizAnswers = quiz.getQuizAnswers();
        for (int i = 0; i < quizAnswers.size(); i++) {
            QuizAnswer answer = quizAnswers.get(i);
            
            if (!answerers.contains(answer.getUser().getName())) {
                answerers.add(answer.getUser().getName());
            }
            
            List<PeerReview> reviews = answer.getPeerReviews();
            for (int j = 0; j < reviews.size(); j++) {
                if (!reviewers.contains(reviews.get(j).getReviewer().getName())) {
                    reviewers.add(reviews.get(j).getReviewer().getName());
                }
            }
        }
        
        return new QuizPointModel(quiz, answerers, reviewers);
    }
}
