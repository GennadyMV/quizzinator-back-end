package app.deserializers;

import app.domain.User;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import java.io.IOException;

/**
 * Special JSON deserializer for User class.
 * creates a User object with username from a json string
 * @author aaro
 */
public class UserJsonDeserializer extends JsonDeserializer<User> {

    @Override
    public User deserialize(JsonParser jp, DeserializationContext dc) throws IOException, JsonProcessingException {
        User u = new User();
        String username = jp.getText();
        u.setName(username);
        return u;
    }

}
