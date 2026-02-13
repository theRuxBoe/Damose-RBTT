package backend.user;

/**
 * The Class NoAccountExistsYet -> it activates when an account doesn't exist in the database.
 */
public class NoAccountExistsYet extends RuntimeException {
    
    /**
     * Instantiates a new no account exists yet.
     *
     * @param message the message
     */
    public NoAccountExistsYet(String message) {
        super(message);
    }
}
