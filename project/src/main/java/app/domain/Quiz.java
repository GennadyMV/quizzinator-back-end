package app.domain;

import java.util.List;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;
import org.springframework.data.jpa.domain.AbstractPersistable;

@Entity
public class Quiz extends AbstractPersistable<Long> {
    @NotNull
    private String title;
    
    @OneToMany(fetch = FetchType.EAGER)
    private List<OpenQuestion> openQuestions;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<OpenQuestion> getOpenQuestions() {
        return openQuestions;
    }

    public void setOpenQuestions(List<OpenQuestion> openQuestions) {
        this.openQuestions = openQuestions;
    }
}
