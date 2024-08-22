package net.atos.exception;

public class EmptyFieldException extends AuthenticationException {
    public EmptyFieldException(String fieldName) {
        super(fieldName + " cannot be empty");
    }
}