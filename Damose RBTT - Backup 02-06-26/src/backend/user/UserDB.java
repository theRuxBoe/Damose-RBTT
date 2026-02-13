package backend.user;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

/**
 * The Class UserDB -> represents a database (a txt file) containing all the user accounts.
 */
public class UserDB {
	
	/** The users file DB. */
	private final File usersFileDB;
	
	/** The users indexed by their user id in a map. */
	private final Map<String, User> usersById;
	
	/**
	 * Instantiates a new user DB.
	 *
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public UserDB() throws IOException {
		
		usersFileDB = new File("UsersFileDB.txt");
		
		if (!usersFileDB.exists()) {
			
			usersFileDB.createNewFile();
		}
		
		this.usersById = new HashMap<String, User>();
		loadUsersFromFile();
	}
	
	/**
	 * Loads saved users from the database file to fill the usersById map when a new instance of UserDB is created.
	 *
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	private void loadUsersFromFile() throws IOException {
		
		try (BufferedReader reader = new BufferedReader(new FileReader(usersFileDB))) {
			
			String riga;
			while ((riga = reader.readLine()) != null) {
				
				String idRiga = riga;
				String nomeRiga = reader.readLine();
				String passwordRiga = reader.readLine();
				
				reader.readLine(); // Salta riga vuota
				
				if (idRiga == null || nomeRiga == null || passwordRiga == null) {
					
					break;
				}
				
				try {
					
					String id = idRiga.substring(idRiga.indexOf(":")+1).trim();
					String nome = nomeRiga.substring(nomeRiga.indexOf(":")+1).trim();
					String hashedPassword = passwordRiga.substring(passwordRiga.indexOf(":")+1).trim();
					usersById.put(id, new User(id, nome, hashedPassword));
				}
				
				catch (Exception e) {
					
					e.printStackTrace();
				}
			}
		}
	}
	
	/**
	 * Creates a new user account and it adds it in the DB and in the usersById.
	 *
	 * @param userName the user name
	 * @param password the password
	 * @throws IOException Signals that an I/O exception has occurred.
	 * @throws IllegalArgumentException the illegal argument exception
	 * @throws AccountAlreadyExistsException the account already exists exception
	 */
	public synchronized void createNewAccount(String userName, String password) throws IOException, IllegalArgumentException, AccountAlreadyExistsException {
		
		if (userName == null || password == null) {
		    throw new IllegalArgumentException("Testo nullo non consentito.");
		}
		
		userName = userName.trim();
		password = password.trim();
		
		if (password.isEmpty()) {
			
			throw new IllegalArgumentException("La password non può essere vuota");
		}
		
		if (password.length() < 8) {
			
			throw new IllegalArgumentException("La password deve contenere almeno 8 caratteri.");
		}
		
        if (userName.length() == 0) {
            throw new IllegalArgumentException("Nome vuoto non consentito.");
        }
		
		if (!usersById.isEmpty()) {
			
			for (User u : usersById.values()) {
				
				if (u.getUserName().equals(userName)) {
					
					throw new AccountAlreadyExistsException("Esiste già un account con questo nome utente. Accedi o crea un nuovo account.");
				}
			
		}
		
		String id = UUID.randomUUID().toString();
		User newUser = User.fromPlainPassword(id, userName, password);
		
		try (BufferedWriter writer = new BufferedWriter(new FileWriter(usersFileDB, true))) {
			
			writer.write("ID: "+id);
			writer.newLine();
			writer.write("User name: "+userName);
			writer.newLine();
			writer.write("Password: "+PasswordUtil.hash(password));
			writer.newLine();
			writer.newLine();
			
		}
		
		usersById.put(id, newUser); 
		
		}

	}
	
	/**
	 * Log in method.
	 *
	 * @param userName the user name
	 * @param password the password
	 * @return true, if successful
	 * @throws IllegalArgumentException the illegal argument exception
	 * @throws NoAccountExistsYet the no account exists yet
	 */
	public synchronized boolean logIn(String userName, String password) throws IllegalArgumentException, NoAccountExistsYet {
		
		userName = userName.trim();
		password = password.trim();
		
		if (userName.isEmpty()|| password.isEmpty()) {
			System.out.println("Login fallito. Il nome utente o la password sono errati.");
		    return false;
		}
		
		if (userName == null || password == null) {
		    throw new IllegalArgumentException("Testo nullo non consentito.");
		}
		
		if (usersById.isEmpty()) {
			
			throw new NoAccountExistsYet("Non esiste ancora nessun account inserito. Creane uno.");
		}
		
		for (User u : usersById.values()) {
			
			if  (u.getUserName().equals(userName) && u.checkPassword(password)) {
				
				System.out.println("Login effettuato con successo.");
				return true;
			}
			
		}
		
		System.out.println("Login fallito. Il nome utente o la password sono errati.");
		return false;
	}

	/**
	 * Finds a user by his username.
	 *
	 * @param username the username
	 * @return the optional
	 * @throws IllegalArgumentException the illegal argument exception
	 */
	public Optional<User> findUserByName(String username) throws IllegalArgumentException {
		
		if (username == null || username.isBlank()) {
			
			throw new IllegalArgumentException("Testo nullo non consentito.");
		}
		
		for (User u : usersById.values()) {
			
			if (u.getUserName().equals(username)) {
				
				return Optional.of(u);
			}
		}
		
		return Optional.empty();
	}
}
