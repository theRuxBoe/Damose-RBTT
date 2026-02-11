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
import frontend.main.MainFrame;
import frontend.utilities.ListToScrollConverter;

public class FavouritesDBManager {

	private static FavoriteRouteDB routes;
	private static FavoriteStopDB stops;

	public void openDBs() {
		if (routes == null && stops == null) {
			try {
				routes = new FavoriteRouteDB();
				stops = new FavoriteStopDB();

			}

			catch (IOException e) {
				JOptionPane.showMessageDialog(null, e.getMessage());
			}
		}

	}

	public static List<JPanel> getFavourites(String user) {
		Optional<List<FavoriteRoute>> rt = routes.findFavoriteRouteByUserId(user);

		Optional<List<FavoriteStop>> st = stops.findFavoritesByUserId(user);
		List<DatoGTF> result = new ArrayList<>();
		if (!rt.isEmpty()) {
			List<FavoriteRoute> routesFav = rt.get();
			for (FavoriteRoute fr : routesFav) {
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

	public static void addToFavourites(Fermata f) {
		try {
			stops.addFavoriteStop(MainFrame.getCurrentUser(), f, null);
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null, "Non è stato possibile connettersi al database, riprovare più tardi");
		}
	
		catch (FavoriteAlreadyExistingException excep) {
			JOptionPane.showMessageDialog(null, excep.getMessage());
		}
	}

	public static void addToFavourites(Linea l) {
		try {
			routes.addFavoriteRoute(MainFrame.getCurrentUser(), l, null);
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null, "Non è stato possibile connettersi al database, riprovare più tardi");
		
		}
			catch (FavoriteAlreadyExistingException excep) {
				JOptionPane.showMessageDialog(null, excep.getMessage());
		}
	}

	public static void remove(Fermata f) {
		try {
		stops.deleteFavoriteStop(MainFrame.getCurrentUser(), f.getStopId());
		}
		catch (IOException ex) {
			JOptionPane.showMessageDialog(null, ex.getMessage());
		}
	}

	public static void remove(Linea l) {
		try {
			routes.deleteFavoriteRoute(MainFrame.getCurrentUser(), l.getRouteId());
			}
			catch (IOException ex) {
				JOptionPane.showMessageDialog(null, ex.getMessage());
			}
	}

	public static boolean isPresent(Linea l) {
		return routes.isFavoriteRoutePresent(MainFrame.getCurrentUser(), l.getRouteId());
	}
	
	public static boolean isPresent(Fermata f) {
		return stops.isFavoriteStopPresent(MainFrame.getCurrentUser(), f.getStopId());
	}
}
