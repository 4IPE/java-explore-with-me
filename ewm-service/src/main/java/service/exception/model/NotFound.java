package service.exception.model;

public class NotFound extends RuntimeException {
    public NotFound(String message) {
        super(message);
    }
}
