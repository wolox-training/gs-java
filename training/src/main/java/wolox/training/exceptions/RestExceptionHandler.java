package wolox.training.exceptions;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler({BookNotFoundException.class})
    protected ResponseEntity<Object> handleNotFound(
        RuntimeException ex, WebRequest request) {
        return handleExceptionInternal(ex, "Book not found",
            new HttpHeaders(), HttpStatus.NOT_FOUND, request);
    }


    @ExceptionHandler({UserNotFoundException.class})
    protected ResponseEntity<Object> handleUserNotFound(
        RuntimeException ex, WebRequest request) {
        return handleExceptionInternal(ex, "User not found",
            new HttpHeaders(), HttpStatus.NOT_FOUND, request);
    }

    @ExceptionHandler({BookAlreadyOwnedException.class})
    protected ResponseEntity<Object> handleBookAlreadyOwnedException(
        RuntimeException ex, WebRequest request) {
        return handleExceptionInternal(ex, "the book already belongs to the owner",
            new HttpHeaders(), HttpStatus.NOT_FOUND, request);
    }

    @ExceptionHandler({BookNotOwnedException.class})
    protected ResponseEntity<Object> handleBookNotOwnedException(
        RuntimeException ex, WebRequest request) {
        return handleExceptionInternal(ex, "the book not belongs to the owner",
            new HttpHeaders(), HttpStatus.NOT_FOUND, request);
    }

    @ExceptionHandler({BookIdMismatchException.class,
    })
    public ResponseEntity<Object> handleBadRequest(
        Exception ex, WebRequest request) {
        return handleExceptionInternal(ex, "the id of book is different of parameter",
            new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
    }

}
