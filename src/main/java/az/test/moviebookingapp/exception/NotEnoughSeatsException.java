package az.test.moviebookingapp.exception;

public class NotEnoughSeatsException extends RuntimeException {
    public NotEnoughSeatsException() {
        super("There are not enough seats available for this shows");
    }
}
