package frontend.user;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.swing.JOptionPane;
import javax.swing.JPanel;

import backend.favorite.FavoriteAlreadyExistingException;
import backend.favorite.FavoriteRoute;
import backend.favorite.FavoriteRouteDB;
import backend.favorite.FavoriteStop;
import backend.favorite.FavoriteStopDB;
import backend.model.DatoGTF;
import backend.model.Fermata;
import backend.model.Linea;
import frontend.utilities.ListToScrollConverter;

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
		Optional<List<FavoriteRoute>> rt = lines.findFavoriteRouteByUserId(user);

		Optional<List<FavoriteStop>> st = stops.findFavoritesByUserId(user);
		List<DatoGTF> result = new ArrayList<>();
		if (!rt.isEmpty()) {
			List<FavoriteRoute> linesFav = rt.get();
			for (FavoriteRoute fr : linesFav) {
				result.add(fr.getLineaSalvata());
			}
		}
		if (!st.isEmpty()) {
			List<FavoriteStop> stopsFav = st.get();
			for (FavoriteStop fs : stopsFav) {
				result.add(fs.getFermataSalvata());
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
	public static void addToFavourites(Linea l) {
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
	public static void remove(Linea l) {
		try {
			lines.deleteFavoriteRoute(LoginToMainFrame.getCurrentUser(), l.getRouteId());
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
	public static boolean isPresent(Linea l) {
		return lines.isFavoriteRoutePresent(LoginToMainFrame.getCurrentUser(), l.getRouteId());
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
