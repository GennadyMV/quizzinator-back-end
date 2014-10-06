package app.models;

import app.domain.PeerReview;
import app.domain.QuizAnswer;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

public class UsersReviewModel {
    private Long quizId;
    private String title;
    @JsonProperty("yourAnswer")
    private QuizAnswer answer;
    private List<PeerReview> reviews;

    public Long getQuizId() {
        return quizId;
    }

    public void setQuizId(Long quizId) {
        this.quizId = quizId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public QuizAnswer getAnswer() {
        return answer;
    }

    public void setAnswer(QuizAnswer answer) {
        this.answer = answer;
    }

    public List<PeerReview> getReviews() {
        return reviews;
    }

    public void setReviews(List<PeerReview> reviews) {
        this.reviews = reviews;
    }
    
    
    
}
