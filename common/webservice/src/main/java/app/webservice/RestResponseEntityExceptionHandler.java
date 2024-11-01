package app.webservice;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.client.ResourceAccessException;
import jakarta.validation.ConstraintViolationException;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import app.domain.common.EntityAlreadyExistsException;
import app.domain.common.ForbiddenException;

@ControllerAdvice
public class RestResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {
    @ExceptionHandler(value = {EntityAlreadyExistsException.class})
    protected ResponseEntity<Object> handleConflict(
            final EntityAlreadyExistsException ex,
            final WebRequest request) {
        return handleExceptionInternal(
                ex,
                ex.getMessage(),
                new HttpHeaders(),
                HttpStatus.CONFLICT,
                request
        );
    }

    @ExceptionHandler(value = {ForbiddenException.class})
    protected ResponseEntity<Object> handleConflict(
            final ForbiddenException ex,
            final WebRequest request) {
        return handleExceptionInternal(
                ex,
                ex.getMessage(),
                new HttpHeaders(),
                HttpStatus.FORBIDDEN,
                request
        );
    }

    @ExceptionHandler(value = {RuntimeException.class})
    protected ResponseEntity<Object> handleConflict(
            final RuntimeException ex,
            final WebRequest request) {
        return handleExceptionInternal(
                ex,
                ex.getMessage(),
                new HttpHeaders(),
                HttpStatus.INTERNAL_SERVER_ERROR,
                request
        );
    }


    @ExceptionHandler(value = {ConstraintViolationException.class})
    protected ResponseEntity<Object> handleConflict(
            final ConstraintViolationException ex,
            final WebRequest request) {
        return handleExceptionInternal(
                ex,
                ex.getMessage(),
                new HttpHeaders(),
                HttpStatus.BAD_REQUEST,
                request
        );
    }


    @ExceptionHandler(value = {RestClientResponseException.class})
    protected ResponseEntity<Object> handleRestClientException(
            final RestClientResponseException ex,
            final WebRequest request) {
        return handleExceptionInternal(
                ex,
                ex.getMessage(),
                new HttpHeaders(),
                ex.getStatusCode(),
                request
        );
    }

    @ExceptionHandler(value = {ResourceAccessException.class})
    protected ResponseEntity<Object> handleResourceAccessException(
            final ResourceAccessException ex,
            final WebRequest request) {
        return handleExceptionInternal(
                ex,
                ex.getMessage(),
                new HttpHeaders(),
                HttpStatus.NOT_ACCEPTABLE,
                request
        );
    }
}
