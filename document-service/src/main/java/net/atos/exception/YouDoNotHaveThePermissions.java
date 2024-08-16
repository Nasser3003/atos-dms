package net.atos.exception;

public class YouDoNotHaveThePermissions extends RuntimeException {
    public YouDoNotHaveThePermissions(String message) {
        super(message);
    }
}
