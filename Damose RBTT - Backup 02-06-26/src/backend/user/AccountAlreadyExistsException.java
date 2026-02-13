package backend.user;

/**
 * The Class AccountAlreadyExistsException -> it activates when a user account is already existing in the database..
 */
public class AccountAlreadyExistsException extends RuntimeException {
    
    /**
     * Instantiates a new account already exists exception.
     *
     * @param message the message
     */
    public AccountAlreadyExistsException(String message) {
        super(message);
    }
}
