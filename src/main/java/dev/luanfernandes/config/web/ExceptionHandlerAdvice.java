package dev.luanfernandes.config.web;

import static dev.luanfernandes.domain.constants.ExceptionHandlerAdviceConstants.STACKTRACE_PROPERTY;
import static dev.luanfernandes.domain.constants.ExceptionHandlerAdviceConstants.TIMESTAMP_PROPERTY;
import static java.lang.String.format;
import static java.time.LocalTime.now;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.CONFLICT;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.ProblemDetail.forStatusAndDetail;
import static org.springframework.http.ResponseEntity.status;

import dev.luanfernandes.domain.exception.AdminUserDeletionException;
import dev.luanfernandes.domain.exception.EntityAlreadyExistsException;
import dev.luanfernandes.domain.exception.NotFoundException;
import jakarta.validation.ConstraintViolationException;
import java.time.Instant;
import java.util.List;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.server.ResponseStatusException;

@RestControllerAdvice
public class ExceptionHandlerAdvice {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    ResponseEntity<ProblemDetail> handleMethodArgumentNotValidException(MethodArgumentNotValidException exception) {
        ProblemDetail problemDetail = forStatusAndDetail(BAD_REQUEST, "Validation failed for argument");
        List<String> errors = exception.getBindingResult().getFieldErrors().stream()
                .map(error -> format("%s: %s", error.getField(), error.getDefaultMessage()))
                .toList();
        problemDetail.setProperty(TIMESTAMP_PROPERTY, Instant.now());
        problemDetail.setProperty(STACKTRACE_PROPERTY, errors);

        return status(BAD_REQUEST).body(problemDetail);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    ResponseEntity<ProblemDetail> handleIllegalArgumentException(IllegalArgumentException exception) {
        return status(BAD_REQUEST)
                .body(exceptionToProblemDetailForStatusAndDetail(BAD_REQUEST, exception.getMessage()));
    }

    @ExceptionHandler(HttpClientErrorException.class)
    ResponseEntity<ProblemDetail> handleHttpClientErrorException(HttpClientErrorException exception) {
        return status(exception.getStatusCode())
                .body(exceptionToProblemDetailForStatusAndDetail(exception.getStatusCode(), exception.getMessage()));
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    ResponseEntity<ProblemDetail> handleMethodArgumentTypeMismatchException(
            MethodArgumentTypeMismatchException exception) {
        return status(BAD_REQUEST)
                .body(exceptionToProblemDetailForStatusAndDetail(BAD_REQUEST, exception.getMessage()));
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    ResponseEntity<ProblemDetail> handleMissingServletRequestParameterException(
            MissingServletRequestParameterException exception) {
        return status(BAD_REQUEST)
                .body(exceptionToProblemDetailForStatusAndDetail(BAD_REQUEST, exception.getMessage()));
    }

    @ExceptionHandler(AdminUserDeletionException.class)
    ResponseEntity<ProblemDetail> handleAdminUserDeletionException(AdminUserDeletionException exception) {
        return status(CONFLICT).body(exceptionToProblemDetailForStatusAndDetail(CONFLICT, exception.getMessage()));
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ProblemDetail> handleUserException(NotFoundException exception) {
        return status(NOT_FOUND).body(exceptionToProblemDetailForStatusAndDetail(NOT_FOUND, exception.getMessage()));
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ProblemDetail> handleHttpMessageNotReadableException(
            HttpMessageNotReadableException exception) {
        return status(BAD_REQUEST)
                .body(exceptionToProblemDetailForStatusAndDetail(BAD_REQUEST, exception.getMessage()));
    }

    @ExceptionHandler(EntityAlreadyExistsException.class)
    public ResponseEntity<ProblemDetail> handleEntityNotFoundException(EntityAlreadyExistsException exception) {
        return status(CONFLICT).body(exceptionToProblemDetailForStatusAndDetail(CONFLICT, exception.getMessage()));
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ProblemDetail> handleConstraintViolationException(ConstraintViolationException exception) {
        ProblemDetail problemDetail = exceptionToProblemDetailForStatusAndDetail(BAD_REQUEST, exception.getMessage());
        return status(BAD_REQUEST).body(problemDetail);
    }

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<ProblemDetail> handleResponseStatusException(ResponseStatusException exception) {
        ProblemDetail problemDetail =
                exceptionToProblemDetailForStatusAndDetail(exception.getStatusCode(), exception.getReason());
        return status(exception.getStatusCode()).body(problemDetail);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    protected ResponseEntity<ProblemDetail> handleDataIntegrityViolation(DataIntegrityViolationException eq) {
        return status(HttpStatus.CONFLICT).body(exceptionToProblemDetailForStatusAndDetail(CONFLICT, eq.getMessage()));
    }

    private ProblemDetail exceptionToProblemDetailForStatusAndDetail(HttpStatusCode status, String detail) {
        ProblemDetail problemDetail = forStatusAndDetail(status, detail);
        problemDetail.setProperty(TIMESTAMP_PROPERTY, now());
        return problemDetail;
    }
}
