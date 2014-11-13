package app.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value=HttpStatus.FORBIDDEN, reason="user is not allowed to rate this review")
public class UnauthorizedRateException extends RuntimeException {

}
