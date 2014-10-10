package app.models;

import app.domain.QuizAnswer;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;


//TODO: edit the api to make more sense and get rid of this
public class ReviewResponseModel {
    @JsonProperty("answers")
    private List<QuizAnswer> answers;
    
    //returned hash should be answer rather than specific
    private String userhash;

    public List<QuizAnswer> getAnswerForReview() {
        return answers;
    }

    public void setAnswerForReview(List<QuizAnswer> answerForReview) {
        this.answers = answerForReview;
    }

    public String getUserhash() {
        return userhash;
    }

    public void setUserhash(String userhash) {
        this.userhash = userhash;
    }
    
    
}
