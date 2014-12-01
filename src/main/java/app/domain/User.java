package app.domain;

import app.deserializers.UserJsonDeserializer;
import app.serializers.UserJsonSerializer;
import app.services.HashService;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import javax.persistence.Column;
import javax.persistence.Entity;
import org.springframework.data.jpa.domain.AbstractPersistable;

/**
 * Users are course students or other unauthenticated participants who are able
 * to answer quizzes, give and get peer review and rate reviews.
 * 
 * TODO: Some of this doesn't make much sense.
 * hash is (was?) supposed to be used as a token to access one's own answers, but
 * there are ways to gain unauthenticated access to these things and no real
 * authentication of users
 */
@Entity(name = "review_user")
@JsonIgnoreProperties(value = "new")
@JsonSerialize(using = UserJsonSerializer.class)
@JsonDeserialize(using = UserJsonDeserializer.class)
public class User extends AbstractPersistable<Long> {
    /**
     * username.
     * Must be unique.
     */
    @Column(unique = true)
    private String name;
    
    /**
     * Hash of the username and salt.
     * Not really used in any sensible manner at the moment.
     */
    @Column(name = "user_hash", unique = true)
    private String hash;
    
    /**
     * Determines the amount of reviews given to user.
     * eg. reviewWeight=2, user gets twice as much reviews as other users
     */
    @Column(nullable = false)
    private Double reviewWeight = 1.0;
    
    public String getHash() {
        return hash;
    }
    
    public String getName() {
        return name;
    }

    /**
     * Sets also the hash of the user using HashService
     * @param name username
     */
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
