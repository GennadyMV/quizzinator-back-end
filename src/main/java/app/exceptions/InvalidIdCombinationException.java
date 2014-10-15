package app.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value=HttpStatus.BAD_REQUEST, reason="bad id combination")
public class InvalidIdCombinationException extends RuntimeException {
    public InvalidIdCombinationException(String message) {
        super(message);
    }
}
