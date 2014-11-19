package app.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value=HttpStatus.FORBIDDEN, reason="deadline has passed or answering has not started yet")
public class DeadlinePassedException extends RuntimeException {

}
