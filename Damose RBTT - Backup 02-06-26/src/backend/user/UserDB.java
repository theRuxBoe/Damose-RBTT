package backend.user;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class UserDB {
	
	private final File usersFileDB;
	private final List<User> usersList;
	
	public UserDB() throws IOException {
		
		usersFileDB = new File("UsersFileDB.txt");
		
		if (!usersFileDB.exists()) {
			
			usersFileDB.createNewFile();
		}
		
		usersList = new ArrayList<User>();
		loadUsersFromFile();
	}
	
	//popola la lista degli utenti dal file quando si crea l'istanza UserDB
	private void loadUsersFromFile() throws IOException {
		
		try (BufferedReader reader = new BufferedReader(new FileReader(usersFileDB))) {
			
			String riga;
			while ((riga = reader.readLine()) != null) {
				
				String idRiga = riga;
				String nomeRiga = reader.readLine();
				String passwordRiga = reader.readLine();
				
				reader.readLine(); //salta riga vuota
				
				if (idRiga == null || nomeRiga == null || passwordRiga == null) {
					
					break;
				}
				
				try {
					
					String id = idRiga.substring(idRiga.indexOf(":")+1).trim();
					String nome = nomeRiga.substring(nomeRiga.indexOf(":")+1).trim();
					String hashedPassword = passwordRiga.substring(passwordRiga.indexOf(":")+1).trim();
					usersList.add(new User(id, nome, hashedPassword));
				}
				
				catch (Exception e) {
					
					e.printStackTrace();
				}
			}
		}
	}
	
	public synchronized void createNewAccount(String userName, String password) throws IOException {
		
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
		
		if (!usersList.isEmpty()) {
			
			for (User u : usersList) {
				
				if (u.getUserName().equals(userName)) {
					
					throw new AccountAlreadyExistsException("Esiste già un account con questo nome utente. Accedi o crea un nuovo account.");
				}
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
		
		usersList.add(newUser);

	}
	
	public synchronized boolean logIn(String userName, String password) {
		
		userName = userName.trim();
		password = password.trim();
		
		if (userName.isEmpty()|| password.isEmpty()) {
			System.out.println("Login fallito. Il nome utente o la password sono errati.");
		    return false;
		}
		
		if (userName == null || password == null) {
		    throw new IllegalArgumentException("Testo nullo non consentito.");
		}
		
		if (usersList.isEmpty()) {
			
			throw new NoAccountExistsYet("Non esiste ancora nessun account inserito. Creane uno.");
		}
		
		for (User u : usersList) {
			
			if  (u.getUserName().equals(userName) && u.checkPassword(password)) {
				
				System.out.println("Login effettuato con successo.");
				return true;
			}
			
		}
		
		System.out.println("Login fallito. Il nome utente o la password sono errati.");
		return false;
	}

	public Optional<User> findUserByName(String username) {
		
		if (username == null) {
			
			throw new IllegalArgumentException("Testo nullo non consentito.");
		}
		
		for (User u : usersList) {
			
			if (u.getUserName().equals(username)) {
				
				return Optional.of(u);
			}
		}
		
		return Optional.empty();
	}
}
