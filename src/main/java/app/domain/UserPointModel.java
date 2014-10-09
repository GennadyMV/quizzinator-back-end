/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package app.domain;

/**
 *
 * @author albis
 */
public class UserPointModel {
    private User user;
    private Integer answerCount;
    private Integer reviewCount;
    
    public UserPointModel() {
    }
    
    public UserPointModel(User user, Integer answerCount, Integer reviewCount) {
        this.user = user;
        this.answerCount = answerCount;
        this.reviewCount = reviewCount;
    }
    
    public void setUser(User user) {
        this.user = user;
    }
    
    public User getUser() {
        return user;
    }
    
    public void setAnswerCount(Integer answerCount) {
        this.answerCount = answerCount;
    }
    
    public Integer getAnswerCount() {
        return answerCount;
    }
    
    public void setReviewCount(Integer reviewCount) {
        this.reviewCount = reviewCount;
    }
    
    public Integer getReviewCount() {
        return reviewCount;
    }
}