package main.java.frontend.user;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.swing.JOptionPane;
import javax.swing.JPanel;

import main.java.backend.favorite.FavoriteAlreadyExistingException;
import main.java.backend.favorite.FavoriteRoute;
import main.java.backend.favorite.FavoriteRouteDB;
import main.java.backend.favorite.FavoriteStop;
import main.java.backend.favorite.FavoriteStopDB;
import main.java.backend.model.DatoGTF;
import main.java.backend.model.Fermata;
import main.java.backend.model.Linea;
import main.java.backend.model.RisultatoLinea;
import main.java.frontend.utilities.ListToScrollConverter;

/**
 * The Class FavouritesDBManager controls the access to the database
 * of favourites lines and stops
 */
public class FavouritesDBManager {

	/** The favourite routes. */
	private static FavoriteRouteDB lines;
	
	/** The favourites stops. */
	private static FavoriteStopDB stops;

	/**
	 * Opens the two favourites databases.
	 */
	public void openDBs() {
		if (lines == null && stops == null) {
			try {
				lines = new FavoriteRouteDB();
				stops = new FavoriteStopDB();

			}

			catch (IOException e) {
				JOptionPane.showMessageDialog(null, e.getMessage());
			}
		}

	}

	/**
	 * Gets a list of favorites as panels for the user given in input.
	 *
	 * @param user the user
	 * @return the favourites' panels
	 */
	public static List<JPanel> getFavourites(String user) {
		Optional<List<FavoriteRoute>> rt = lines.findFavoriteRoutesByUserId(user);

		Optional<List<FavoriteStop>> st = stops.findFavoriteStopsByUserId(user);
		List<DatoGTF> result = new ArrayList<>();
		
		if (!st.isEmpty()) {
			List<FavoriteStop> stopsFav = st.get();
			for (FavoriteStop fs : stopsFav) {
				result.add(fs.getFermataSalvata());
			}
		
		}
		
		if (!rt.isEmpty()) {
			List<FavoriteRoute> linesFav = rt.get();
			for (FavoriteRoute fr : linesFav) {
				result.add(fr.getLineaSalvata());
			}
		}

		return ListToScrollConverter.convertList(result);
		

		
	}

	/**
	 * Adds a stop the to favourites bus stops.
	 *
	 * @param f the bus stop
	 */
	public static void addToFavourites(Fermata f) {
		try {
			stops.addFavoriteStop(LoginToMainFrame.getCurrentUser(), f, null);
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null, "Non è stato possibile connettersi al database, riprovare più tardi");
		}
	
		catch (FavoriteAlreadyExistingException excep) {
			JOptionPane.showMessageDialog(null, excep.getMessage());
		}
	}

	/**
	 * Adds a line the to favourites lines.
	 *
	 * @param l the line
	 */
	public static void addToFavourites(RisultatoLinea l) {
		try {
			lines.addFavoriteRoute(LoginToMainFrame.getCurrentUser(), l, null);
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null, "Non è stato possibile connettersi al database, riprovare più tardi");
		
		}
			catch (FavoriteAlreadyExistingException excep) {
				JOptionPane.showMessageDialog(null, excep.getMessage());
		}
	}

	/**
	 * Removes a specific bus stop.
	 *
	 * @param f the stop
	 */
	public static void remove(Fermata f) {
		try {
		stops.deleteFavoriteStop(LoginToMainFrame.getCurrentUser(), f.getStopId());
		}
		catch (IOException ex) {
			JOptionPane.showMessageDialog(null, ex.getMessage());
		}
	}

	/**
	 * Removes a specific route.
	 *
	 * @param l the line
	 */
	public static void remove(RisultatoLinea l) {
		try {
			lines.deleteFavoriteRoute(LoginToMainFrame.getCurrentUser(), l.getRouteId(), l.getDirectionName());
			}
			catch (IOException ex) {
				JOptionPane.showMessageDialog(null, ex.getMessage());
			}
	}

	/**
	 * Checks if a line is present.
	 *
	 * @param l the line
	 * @return true, if the line is present in the DB
	 */
	public static boolean isPresent(RisultatoLinea l) {
		return lines.isFavoriteRoutePresent(LoginToMainFrame.getCurrentUser(), l.getRouteId(), l.getDirectionName());
	}
	
	/**
	 * Checks if a stop is present.
	 *
	 * @param f the stop
	 * @return true, if the stop is present in the DB
	 */
	public static boolean isPresent(Fermata f) {
		return stops.isFavoriteStopPresent(LoginToMainFrame.getCurrentUser(), f.getStopId());
	}
}
