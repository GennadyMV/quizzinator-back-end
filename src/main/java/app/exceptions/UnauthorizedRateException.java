package app.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value=HttpStatus.FORBIDDEN, reason="user can only rate reviews given to them")
public class UnauthorizedRateException extends RuntimeException {

}
