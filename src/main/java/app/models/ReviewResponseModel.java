package app.models;

import app.domain.QuizAnswer;
import java.util.List;


//TODO: edit the api to make more sense and get rid of this
public class ReviewResponseModel {
    private List<QuizAnswer> answers;
    
    //returned hash should be answer rather than specific
    private String userhash;
    
    //id of the saved answer
    private Long answerId;

    public List<QuizAnswer> getAnswers() {
        return answers;
    }

    public void setAnswers(List<QuizAnswer> answerForReview) {
        this.answers = answerForReview;
    }

    public String getUserhash() {
        return userhash;
    }

    public void setUserhash(String userhash) {
        this.userhash = userhash;
    }

    public Long getAnswerId() {
        return answerId;
    }

    public void setAnswerId(Long answerId) {
        this.answerId = answerId;
    }
}
