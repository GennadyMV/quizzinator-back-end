package app.models;

import app.domain.QuizAnswer;

public class NewAnswerResponseModel {
    private String userhash;
    
    //the saved answer with id
    private QuizAnswer answer;

    public String getUserhash() {
        return userhash;
    }

    public void setUserhash(String userhash) {
        this.userhash = userhash;
    }

    public QuizAnswer getAnswer() {
        return answer;
    }

    public void setAnswer(QuizAnswer answer) {
        this.answer = answer;
    }
}
