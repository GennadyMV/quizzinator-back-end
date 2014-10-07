package app.domain;

import com.fasterxml.jackson.annotation.JsonProperty;

public interface AnswerInterface {
    @JsonProperty("quiz_id")
    Long getQuizId();
    
    @JsonProperty("answer")
    String getAnswer();
}
