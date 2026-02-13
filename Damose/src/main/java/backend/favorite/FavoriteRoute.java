package main.java.backend.favorite;

import main.java.backend.model.Linea;

public class FavoriteRoute {
	
	private String userId;
	private Linea lineaSalvata;
	private String commento;
	
	public FavoriteRoute(String userId, Linea lineaSalvata, String commento) {
		
		this.userId = userId;
		this.lineaSalvata = lineaSalvata;
		this.commento = commento;
	}

	public String getUserId() {
		return userId;
	}

	public Linea getLineaSalvata() {
		return lineaSalvata;
	}

	public String getCommento() {
		return commento;
	}

}
