package main.java.backend.favorite;

import main.java.backend.model.Linea;

/**
 * The Class FavoriteRoute -> identifies an object representing a favorite route saved by a certain user.
 */
public class FavoriteRoute extends Favorite {
	
	/** The user id. */
	private String userId;
	
	/** The saved route. */
	private Linea lineaSalvata;
	
	/** The comment (it can be blank). */
	private String commento;
	
	/**
	 * Instantiates a new favorite route.
	 *
	 * @param userId the user id
	 * @param lineaSalvata the linea salvata
	 * @param commento the commento
	 */
	public FavoriteRoute(String userId, Linea lineaSalvata, String commento) {
		
		this.userId = userId;
		this.lineaSalvata = lineaSalvata;
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
	 * Gets the saved route.
	 *
	 * @return the saved route
	 */
	public Linea getLineaSalvata() {
		return lineaSalvata;
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
