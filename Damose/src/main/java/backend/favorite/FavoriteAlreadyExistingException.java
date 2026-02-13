package main.java.backend.favorite;

public class FavoriteAlreadyExistingException extends RuntimeException {
	
	public FavoriteAlreadyExistingException(String message) {
		super(message);
	}

}
