package backend.favorite;

import backend.model.Fermata;

public class Favorite {
	
	private String userId;
	private Fermata fermataSalvata;
	private String commento;
	
	public Favorite(String userId, Fermata fermataSalvata, String commento) {
		
		this.userId = userId;
		this.fermataSalvata = fermataSalvata;
		this.commento = commento;
	}

	public String getUserId() {
		return userId;
	}

	public Fermata getFermataSalvata() {
		return fermataSalvata;
	}

	public String getCommento() {
		return commento;
	}

}
