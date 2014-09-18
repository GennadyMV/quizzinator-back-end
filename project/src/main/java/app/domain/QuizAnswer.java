package app.domain;

import java.util.List;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.URL;
import org.springframework.data.jpa.domain.AbstractPersistable;

@Entity
@Table(name = "answers")
public class QuizAnswer extends AbstractPersistable<Long> {
    @ManyToOne
    private Quiz quiz;
    
    @OneToMany(mappedBy = "quizAnswer")
    private List<PeerReview> peerReviews;
    
    @NotBlank
    private String user;
    
    @NotBlank
    private String ip;
    
    @URL
    private String url;
    
    @NotBlank
    private String answer;

    public Quiz getQuiz() {
        return quiz;
    }

    public void setQuiz(Quiz quiz) {
        this.quiz = quiz;
    }

    public List<PeerReview> getPeerReviews() {
        return peerReviews;
    }

    public void setPeerReviews(List<PeerReview> peerReviews) {
        this.peerReviews = peerReviews;
    }
    
    public String getUser() {
        return user;
    }
    
    public void setUser(String user) {
        this.user = user;
    }
    
    public String getIp() {
        return ip;
    }
    
    public void setIp(String ip) {
        this.ip = ip;
    }
    
    public String getUrl() {
        return url;
    }
    
    public void setUrl(String url) {
        this.url = url;
    }
    
    public String getAnswer() {
        return answer;
    }
    
    public void setItems(String answer) {
        this.answer = answer;
    }
}
