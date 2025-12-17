package az.test.moviebookingapp.exception;

import az.test.moviebookingapp.model.view.ErrorView;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MovieNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorView handleMovieNotFound(MovieNotFoundException ex) {
        return buildErrorView(HttpStatus.NOT_FOUND, ex.getMessage(), null);
    }

    @ExceptionHandler(TheaterNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorView handleTheaterNotFound(TheaterNotFoundException ex) {
        return buildErrorView(HttpStatus.NOT_FOUND, ex.getMessage(), null);
    }

    @ExceptionHandler(ShowNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorView handleShowNotFound(ShowNotFoundException ex) {
        return buildErrorView(HttpStatus.NOT_FOUND, ex.getMessage(), null);
    }

    @ExceptionHandler(BookingNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorView handleBookingNotFound(BookingNotFoundException ex) {
        return buildErrorView(HttpStatus.NOT_FOUND, ex.getMessage(), null);
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ErrorView> handleUserNotFound(UserNotFoundException ex) {
        ErrorView errorView = buildErrorView(HttpStatus.NOT_FOUND, ex.getMessage(), null);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorView);
    }

    @ExceptionHandler({
            ShowConflictException.class,
            BookingConflictException.class,
            NotEnoughSeatsException.class,
            UserNameAlreadyExistException.class,
            EmailAlreadyExistException.class
    })
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorView handleConflict(RuntimeException ex) {
        return buildErrorView(HttpStatus.CONFLICT, ex.getMessage(), null);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorView handleValidationErrors(MethodArgumentNotValidException ex) {
        List<String> errors = ex.getBindingResult().getFieldErrors()
                .stream()
                .map(error -> error.getField() + " " + error.getDefaultMessage())
                .collect(Collectors.toList());

        return buildErrorView(HttpStatus.BAD_REQUEST, "Validation failed", errors);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorView handleConstraintViolation(ConstraintViolationException ex) {
        List<String> errors = ex.getConstraintViolations()
                .stream()
                .map(v -> v.getPropertyPath() + " " + v.getMessage())
                .collect(Collectors.toList());

        return buildErrorView(HttpStatus.BAD_REQUEST, "Validation failed", errors);
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorView handleAllUncaught(Exception ex) {
        return buildErrorView(HttpStatus.INTERNAL_SERVER_ERROR, "Unexpected error occurred", List.of(ex.getMessage()));
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<?> handleBadCredentials(BadCredentialsException ex) {
        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(Map.of("error", "Invalid username or password"));
    }

    private ErrorView buildErrorView(HttpStatus status, String message, List<String> errors) {
        return ErrorView.builder()
                .errorCode(status.value())
                .errorMessage(message)
                .errors(errors)
                .timestamp(LocalDateTime.now())
                .build();
    }
}
