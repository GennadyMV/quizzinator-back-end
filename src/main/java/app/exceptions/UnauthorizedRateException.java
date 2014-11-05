package app.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value=HttpStatus.FORBIDDEN, reason="users can not rate reviews given by them")
public class UnauthorizedRateException extends RuntimeException {

}
