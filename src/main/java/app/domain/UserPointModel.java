package app.domain;

public class UserPointModel {
    private final String username;
    private final Integer answerCount;
    private final Integer reviewCount;
    
    public UserPointModel(String username, Integer answerCount, Integer reviewCount) {
        this.username = username;
        this.answerCount = answerCount;
        this.reviewCount = reviewCount;
    }
    
    public String getUsername() {
        return username;
    }
    
    public Integer getAnswerCount() {
        return answerCount;
    }
    
    public Integer getReviewCount() {
        return reviewCount;
    }
}