package net.atos.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.naming.AuthenticationException;
import java.nio.file.FileAlreadyExistsException;
import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<Object> handleAccessDeniedException(AccessDeniedException ex) {
        logger.error("Access denied error: ", ex);
        return buildErrorResponse(HttpStatus.FORBIDDEN, "Access denied", ex.getMessage());
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<Object> handleAuthenticationException(AuthenticationException ex) {
        logger.error("Authentication error: ", ex);
        return buildErrorResponse(HttpStatus.UNAUTHORIZED, "Authentication failed", ex.getMessage());
    }

    @ExceptionHandler(FileNotFoundException.class)
    public ResponseEntity<Object> handleFileNotFoundException(FileNotFoundException ex) {
        logger.error("File not found: ", ex);
        return buildErrorResponse(HttpStatus.NOT_FOUND, "File not found", ex.getMessage());
    }

    @ExceptionHandler(DocumentNotFoundException.class)
    public ResponseEntity<Object> handleDocumentNotFoundException(DocumentNotFoundException ex) {
        logger.error("Document not found: ", ex);
        return buildErrorResponse(HttpStatus.NOT_FOUND, "Document not found", ex.getMessage());
    }

    @ExceptionHandler(FileStorageException.class)
    public ResponseEntity<Object> handleFileStorageException(FileStorageException ex) {
        logger.error("File storage error: ", ex);
        return buildErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, "File storage error", ex.getMessage());
    }

    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<Object> handleYouDoNotHaveThePermissions(UnauthorizedException ex) {
        logger.error("Permission denied: ", ex);
        return buildErrorResponse(HttpStatus.FORBIDDEN, "Permission denied", ex.getMessage());
    }

    @ExceptionHandler(AttributeException.class)
    public ResponseEntity<Object> handleAttributeException(AttributeException ex) {
        logger.error("Unexpected error: ", ex);
        return buildErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Issue with attribute", "Please contact support if the problem persists");
    }
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Object> handleIllegalArgumentException(IllegalArgumentException ex) {
        logger.error("Unexpected error: ", ex);
        return buildErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Issue with", ex.getMessage());
    }

    @ExceptionHandler(FileAlreadyExistsException.class)
    public ResponseEntity<Object> handleFileAlreadyExistsException(FileAlreadyExistsException ex) {
        logger.error("Unexpected error: ", ex);
        return buildErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, "An unexpected error occurred", ex.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleException(Exception ex) {
        logger.error("Unexpected error: ", ex);
        return buildErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, "An unexpected error occurred", "Please contact support if the problem persists");
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