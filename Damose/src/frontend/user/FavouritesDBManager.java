package frontend.user;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.swing.JOptionPane;
import javax.swing.JPanel;

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
		if (!rt.isEmpty() || !st.isEmpty()) {
			List<FavoriteRoute> routesFav = rt.get();
			List<FavoriteStop> stopsFav = st.get();

			for (FavoriteRoute fr : routesFav) {
				result.add(fr.getLineaSalvata());
			}
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
	}

	public static void addToFavourites(Linea l) {
		try {
			routes.addFavoriteRoute(MainFrame.getCurrentUser(), l, null);
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null, "Non è stato possibile connettersi al database, riprovare più tardi");
		}
	}

	public static void remove(Fermata f) {
		
	}

	public static void remove(Linea l) {

	}

}
