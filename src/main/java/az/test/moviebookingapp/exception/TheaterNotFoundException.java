package az.test.moviebookingapp.exception;

public class TheaterNotFoundException extends RuntimeException {
    public TheaterNotFoundException() {
        super("There is no theater");
    }
}
