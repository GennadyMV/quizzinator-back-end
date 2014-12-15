package app.models;

/**
 * Data access object for user's points
 */
public class UserPointModel {
    private final String username;
    private final Integer answerCount;
    private final Integer reviewCount;
    private final Integer ratingCount;
    
    public UserPointModel(String username, Integer answerCount, Integer reviewCount, Integer ratingCount) {
        this.username = username;
        this.answerCount = answerCount;
        this.reviewCount = reviewCount;
        this.ratingCount = ratingCount;
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
    
    public Integer getRatingCount() {
        return ratingCount;
    }
}