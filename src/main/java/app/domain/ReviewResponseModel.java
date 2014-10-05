package app.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;


//TODO: edit the api to make more sense and get rid of this
public class ReviewResponseModel {
    @JsonProperty("answers")
    private List<QuizAnswer> answerForReview;
    
    //returned hash should be answer rather than specific
    private String userhash;

    public List<QuizAnswer> getAnswerForReview() {
        return answerForReview;
    }

    public void setAnswerForReview(List<QuizAnswer> answerForReview) {
        this.answerForReview = answerForReview;
    }

    public String getUserhash() {
        return userhash;
    }

    public void setUserhash(String userhash) {
        this.userhash = userhash;
    }
    
    
}
