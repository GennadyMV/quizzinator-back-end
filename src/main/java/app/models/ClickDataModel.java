package app.models;

import java.util.List;

public class ClickDataModel {
    private String user;
    private Long quizId;
    
    private List<EventModel> clicks;
    
    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public Long getQuizId() {
        return quizId;
    }

    public void setQuizId(Long quizId) {
        this.quizId = quizId;
    }

    public List<EventModel> getClicks() {
        return clicks;
    }

    public void setClicks(List<EventModel> clicks) {
        this.clicks = clicks;
    }
}
