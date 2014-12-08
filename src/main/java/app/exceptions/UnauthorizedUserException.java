package app.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value=HttpStatus.FORBIDDEN, reason="Quiz not found or user not authorized")
public class UnauthorizedUserException extends RuntimeException {
    
}
