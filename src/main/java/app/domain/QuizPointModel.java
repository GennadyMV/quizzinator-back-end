/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package app.domain;

import java.util.List;

/**
 *
 * @author albis
 */
public class QuizPointModel {
    private final Quiz quiz;
    private final List<String> answerers;
    private final List<String> reviewers;
    
    public QuizPointModel(Quiz quiz, List<String> answerers, List<String> reviewers) {
        this.quiz = quiz;
        this.answerers = answerers;
        this.reviewers = reviewers;
    }
    
    public Quiz getQuiz() {
        return quiz;
    }
    
    public List<String> getAnswerers() {
        return answerers;
    }
    
    public List<String> getReviewers() {
        return reviewers;
    }
}
