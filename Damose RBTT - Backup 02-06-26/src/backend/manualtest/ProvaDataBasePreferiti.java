package backend.manualtest;

import java.util.Optional;

import backend.favorite.FavoriteRouteDB;
import backend.favorite.FavoriteStopDB;
import backend.model.Linea;
import backend.model.RisultatoLinea;
import backend.parser.GTFSStaticRepository;
import backend.service.TransitServiceImpl;
import backend.user.User;
import backend.user.UserDB;


/**
 * The Class ProvaDataBasePreferiti.
 */
public class ProvaDataBasePreferiti {
	
	/**
	 * The main method.
	 *
	 * @param args the arguments
	 */
	public static void main(String[] args) {
		
		try {
			
			FavoriteRouteDB favRouteDB = new FavoriteRouteDB();
			FavoriteStopDB favStopDB = new FavoriteStopDB();
			UserDB userDB = new UserDB();
			
			System.out.println("Creazione account");
			userDB.createNewAccount("utente2", "676767676767");
			Optional<User> o1 = userDB.findUserByName("utente2");
			User u1 = null;
			

			
			if (o1.isPresent()) {
				
				System.out.println("Ottengo user name con successo");
				u1 = o1.get();
				favRouteDB.addFavoriteRoute(u1.getId(), new RisultatoLinea(new Linea("899", "899", "CHEESE", "a caso", 3), "via dalle palle"), "2 test");
			}
			
			userDB.createNewAccount("MacDonaldPro", "afrikaChicken");
			Optional<User> o2 = userDB.findUserByName("MacDonaldPro");
			User u2 = null;
			
			if (o2.isPresent()) {
				
				System.out.println("Ottengo user name con successo");
				u2 = o2.get();
			}
			
			
		}
		
		catch (Exception e) {
			
			e.printStackTrace();
		}
	}
	

}
