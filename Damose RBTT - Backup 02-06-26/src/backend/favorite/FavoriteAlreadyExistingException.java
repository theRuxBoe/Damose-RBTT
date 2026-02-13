package backend.favorite;

/**
 * The Class FavoriteAlreadyExistingException -> it activates when a favorite object is already existing in the database.
 */
public class FavoriteAlreadyExistingException extends RuntimeException {
	
	/**
	 * Instantiates a new favorite already existing exception.
	 *
	 * @param message the message
	 */
	public FavoriteAlreadyExistingException(String message) {
		super(message);
	}

}
