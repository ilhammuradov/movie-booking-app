package az.test.moviebookingapp.exception;

public class UserNameAlreadyExistException extends RuntimeException {
    public UserNameAlreadyExistException() {
        super("User name is already taken");
    }
}
