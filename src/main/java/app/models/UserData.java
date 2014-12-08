package app.models;

public class UserData {
    private String username;
    private Long numberOfAnswers;
    private boolean hasImproved;

    public UserData(String user, Long answers, boolean hasImproved) {
        username = user;
        numberOfAnswers = answers;
        this.hasImproved = hasImproved;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Long getNumberOfAnswers() {
        return numberOfAnswers;
    }

    public void setNumberOfAnswers(Long numberOfAnswers) {
        this.numberOfAnswers = numberOfAnswers;
    }

    public boolean isHasImproved() {
        return hasImproved;
    }

    public void setHasImproved(boolean hasImproved) {
        this.hasImproved = hasImproved;
    }

}
