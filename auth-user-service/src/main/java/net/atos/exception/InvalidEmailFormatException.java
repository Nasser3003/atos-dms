package net.atos.exception;

public class InvalidEmailFormatException extends AuthenticationException {
    public InvalidEmailFormatException() {
        super("Invalid email format");
    }
}