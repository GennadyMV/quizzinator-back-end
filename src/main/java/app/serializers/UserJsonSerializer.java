package app.serializers;

import app.domain.User;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import java.io.IOException;

/**
 * A special serializer for User object.
 * Ouputs only the user's name as string
 */
public class UserJsonSerializer extends JsonSerializer<User> {
    @Override
    public void serialize(User user, JsonGenerator jgen, SerializerProvider provider) throws IOException {
        jgen.writeString(user.getName());
    }
}