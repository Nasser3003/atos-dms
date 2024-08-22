package net.atos.exception;

public class PasswordTooShortException extends AuthenticationException {
    public PasswordTooShortException() {
        super("Password must be at least 8 characters long");
    }
}