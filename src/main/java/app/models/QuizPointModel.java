package app.models;

import app.domain.Quiz;
import java.util.List;

public class QuizPointModel {
    private final Quiz quiz;
    private final List<String> answerers;
    private final List<String> reviewers;
    private final List<String> raters;
    
    public QuizPointModel(Quiz quiz, List<String> answerers, List<String> reviewers, List<String> raters) {
        this.quiz = quiz;
        this.answerers = answerers;
        this.reviewers = reviewers;
        this.raters = raters;
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
    
    public List<String> getRaters() {
        return raters;
    }
}
