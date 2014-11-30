package app.domain;

import app.deserializers.UserJsonDeserializer;
import app.serializers.UserJsonSerializer;
import app.services.HashService;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.IdClass;
import org.springframework.data.jpa.domain.AbstractPersistable;
//TODO: this doesnt make much sense yet.
//hash is (was?) supposed to protect peoples answers

@Entity(name = "review_user")
@JsonIgnoreProperties(value = "new")
@JsonSerialize(using = UserJsonSerializer.class)
@JsonDeserialize(using = UserJsonDeserializer.class)
public class User extends AbstractPersistable<Long> {
    @Column(unique = true)
    private String name;
    
    @Column(name = "user_hash", unique = true)
    private String hash;
    
    //determines the amount of reviews given to user
    //eg.  reviewWeight=2, user gets twice as much reviews as other users
    @Column(nullable = false)
    private Double reviewWeight = 1.0;
    
    public String getHash() {
        return hash;
    }
    
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.hash = HashService.HashUsername(name);
        this.name = name;
    }

    public Double getReviewWeight() {
        return reviewWeight;
    }

    public void setReviewWeight(Double reviewWeight) {
        this.reviewWeight = reviewWeight;
    }
}
