package app.domain;

import java.util.List;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Column;
import javax.persistence.Lob;
import javax.validation.constraints.NotNull;
import org.springframework.data.jpa.domain.AbstractPersistable;

@Entity
public class Quiz extends AbstractPersistable<Long> {
    @NotNull
    private String title;
    
    @NotNull
    @Column(columnDefinition = "CLOB") @Lob
    private String items;
    
    @OneToMany
    private List<QuizAnswer> answers;
    
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
    
    public String getItems() {
        return items;
    }
    
    public void setItems(String items) {
        this.items = items;
    }
    
    public List<QuizAnswer> getQuizAnswers() {
        return answers;
    }
    
    public void setQuizAnswers(List<QuizAnswer> answers) {
        this.answers = answers;
    }
}