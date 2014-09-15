package app.domain;

import javax.persistence.Entity;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.URL;
import org.springframework.data.jpa.domain.AbstractPersistable;

@Entity
public class QuizAnswer extends AbstractPersistable<Long> {
    @NotBlank
    private String ip;
    
    @URL
    private String url;
    
    @NotBlank
    private String answer;
    
    public String getIp() {
        return ip;
    }
    
    public void setIp(String ip) {
        this.ip = ip;
    }
    
    public String getUrl() {
        return url;
    }
    
    public void setUrl(String url) {
        this.url = url;
    }
    
    public String getAnswer() {
        return answer;
    }
    
    public void setItems(String answer) {
        this.answer = answer;
    }
}
