package app.models;

import app.domain.AnswerInterface;
import app.domain.QuizAnswer;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;


//TODO: edit the api to make more sense and get rid of this
public class ReviewResponseModel {
    @JsonProperty("answers")
    private List<AnswerInterface> answers;
    
    //returned hash should be answer rather than specific
    private String userhash;

    public List<AnswerInterface> getAnswerForReview() {
        return answers;
    }

    public void setAnswerForReview(List<AnswerInterface> answerForReview) {
        this.answers = answerForReview;
    }

    public String getUserhash() {
        return userhash;
    }

    public void setUserhash(String userhash) {
        this.userhash = userhash;
    }
    
    
}
