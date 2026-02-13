package main.java.backend.user;

public class NoAccountExistsYet extends RuntimeException {
    public NoAccountExistsYet(String message) {
        super(message);
    }
}
