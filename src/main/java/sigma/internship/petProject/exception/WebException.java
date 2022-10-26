package sigma.internship.petProject.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class WebException extends RuntimeException {
    private final HttpStatus status;

    public WebException(HttpStatus status, String message) {
        super(message);
        this.status = status;
    }

    public WebException(HttpStatus status, String message, Throwable cause) {
        super(message, cause);
        this.status = status;
    }

    public ResponseEntity<ExceptionResponse> toExceptionResponse() {
        return new ResponseEntity<>(new ExceptionResponse(status.value(), getMessage()), status);
    }
}
