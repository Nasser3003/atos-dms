package net.atos.exception;

public class NullAuthDtoException extends AuthenticationException {
    public NullAuthDtoException() {
        super("Authentication data is null");
    }
}