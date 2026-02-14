package main.java.backend.favorite;

import main.java.backend.model.Fermata;

/**
 * The Class FavoriteStop -> identifies an object representing a favorite stop saved by a certain user.
 */
public class FavoriteStop extends Favorite {
	
	/** The user id. */
	private String userId;
	
	/** The saved stop. */
	private Fermata fermataSalvata;
	
	/** The comment (it can be blank). */
	private String commento;
	
	/**
	 * Instantiates a new favorite stop.
	 *
	 * @param userId the user id
	 * @param fermataSalvata the fermata salvata
	 * @param commento the commento
	 */
	public FavoriteStop(String userId, Fermata fermataSalvata, String commento) {
		
		this.userId = userId;
		this.fermataSalvata = fermataSalvata;
		this.commento = commento;
	}

	/**
	 * Gets the user id.
	 *
	 * @return the user id
	 */
	public String getUserId() {
		return userId;
	}

	/**
	 * Gets the saved stop.
	 *
	 * @return the saved stop
	 */
	public Fermata getFermataSalvata() {
		return fermataSalvata;
	}

	/**
	 * Gets the comment.
	 *
	 * @return the comment
	 */
	public String getCommento() {
		return commento;
	}

}
