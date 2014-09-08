package app.domain;

import javax.persistence.Entity;
import javax.persistence.Transient;
import org.springframework.data.jpa.domain.AbstractPersistable;

@Entity
public class OpenQuestion extends AbstractPersistable<Long> {
    private String question;
    private Integer itemOrder;

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public Integer getItemOrder() {
        return itemOrder;
    }

    public void setItemOrder(Integer order) {
        this.itemOrder = order;
    }
}
