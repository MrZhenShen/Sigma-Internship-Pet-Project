package sigma.internship.petProject.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class DefaultExceptionHandler {
    @ExceptionHandler(WebException.class)
    public ResponseEntity<ExceptionResponse> webException(WebException exception) {
        return exception.toExceptionResponse();
    }
}
