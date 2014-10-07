package app.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import org.springframework.data.jpa.domain.AbstractPersistable;

@Entity
@Table(name = "placeholder_answers")
@JsonIgnoreProperties(value = "new")
public class PlaceholderAnswer extends AbstractPersistable<Long> implements AnswerInterface {
    @ManyToOne
    private Quiz quiz;
    
    @JsonProperty("answer")
    private String answerData;

    @JsonIgnore
    public Quiz getQuiz() {
        return quiz;
    }

    public void setQuiz(Quiz quiz) {
        this.quiz = quiz;
    }

    public String getAnswerData() {
        return answerData;
    }

    public void setAnswerData(String answerData) {
        this.answerData = answerData;
    }

    @JsonIgnore
    @Override
    public Long getQuizId() {
        return this.quiz.getId();
    }

    @JsonIgnore
    @Override
    public String getAnswer() {
        return this.answerData;
    }
}
