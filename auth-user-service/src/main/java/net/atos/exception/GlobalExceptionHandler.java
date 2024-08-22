package net.atos.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<Object> handleAuthenticationException(AuthenticationException ex) {
        logger.error("Authentication error occurred: ", ex);
        return buildErrorResponse(HttpStatus.UNAUTHORIZED, "Authentication Failed", ex.getMessage());
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<Object> handleAccessDeniedException(AccessDeniedException ex) {
        logger.error("Access denied error occurred: ", ex);
        return buildErrorResponse(HttpStatus.FORBIDDEN, "Access Denied", "You don't have permission to access this resource");
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<Object> handleBadCredentialsException(BadCredentialsException ex) {
        logger.error("Bad credentials error occurred: ", ex);
        return buildErrorResponse(HttpStatus.UNAUTHORIZED, "Authentication Failed", "Invalid username or password");
    }

    @ExceptionHandler(EmailAlreadyTakenException.class)
    public ResponseEntity<Object> handleEmailAlreadyTakenException(EmailAlreadyTakenException ex) {
        logger.error("Email already taken: ", ex);
        return buildErrorResponse(HttpStatus.CONFLICT, "Registration Failed", ex.getMessage());
    }

    @ExceptionHandler({InvalidEmailFormatException.class, EmptyFieldException.class, PasswordTooShortException.class})
    public ResponseEntity<Object> handleValidationException(net.atos.exception.AuthenticationException ex) {
        logger.error("Validation error: ", ex);
        return buildErrorResponse(HttpStatus.BAD_REQUEST, "Validation Failed", ex.getMessage());
    }

    @ExceptionHandler(NullAuthDtoException.class)
    public ResponseEntity<Object> handleNullAuthDtoException(NullAuthDtoException ex) {
        logger.error("Null AuthDto: ", ex);
        return buildErrorResponse(HttpStatus.BAD_REQUEST, "Invalid Request", ex.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleAllUncaughtException(Exception ex) {
        logger.error("Unexpected error occurred: ", ex);
        return buildErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Internal Server Error", "An unexpected error occurred");
    }

    private ResponseEntity<Object> buildErrorResponse(HttpStatus status, String error, String message) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("status", status.value());
        body.put("error", error);
        body.put("message", message);
        return new ResponseEntity<>(body, status);
    }
}