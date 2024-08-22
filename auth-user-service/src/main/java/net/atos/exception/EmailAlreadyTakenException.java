package net.atos.exception;

public class EmailAlreadyTakenException extends AuthenticationException {
    public EmailAlreadyTakenException() {
        super("Email is already taken");
    }
}