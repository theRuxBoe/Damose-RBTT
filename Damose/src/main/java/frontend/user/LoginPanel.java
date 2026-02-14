package main.java.frontend.user;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JOptionPane;

import main.java.backend.user.NoAccountExistsYet;

/**
 * The Class LoginPanel provides a graphic interface
 * for the user to insert his data to access
 * the program with a registered account.
 * 
 */
public class LoginPanel extends UserPanel {


	/**
	 * Instantiates a new login panel and opens the users' database.
	 */
	public LoginPanel() {
		super();
		openDB();
		addButtons();
		

	}


	

	/**
	 * Adds three buttons (login, register and enter as guest) to the panel in the bottom of the screen.
	 * <p>
	 * The login button calls the database to verify the user's name and password,
	 * then opens the database for favourites lines and stops, and finally updates the 
	 * {@link LoginToMainFrame} with the successful login information .
	 * <p>
	 * The register button calls the static method from Login to MainFrame class
	 * to open the register panel.
	 * <p>
	 * The enter as guest button calls {@link LoginToMainFrame} and opens
	 * the frame knowing that no user is logged in.
	 * 
	 *
	 */
	private void addButtons() {
		
		JButton log = new JButton("Login");
		JButton guest = new JButton("Entra come ospite");
		JButton reg = new JButton("Registrati");

		log.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				
				boolean successfulLogin = false;
				String user = getUsername().getText();
				try {
					
					successfulLogin = getDB().logIn(user, new String(getPwdField().getPassword()));
					
				}
				
				catch (IllegalArgumentException iarg) {
					JOptionPane.showMessageDialog(LoginPanel.this, iarg.getMessage());
					
				}
				catch (NoAccountExistsYet nyet) {
					JOptionPane.showMessageDialog(LoginPanel.this, nyet.getMessage());
				}
				if (successfulLogin) {
				FavouritesDBManager dbm = new FavouritesDBManager();
				dbm.openDBs();
					
				LoginToMainFrame.updateFromLogin(successfulLogin, user);
//				removeItself();
//				removeObserver();
				}
				
				
				
			}
		});

		guest.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				LoginToMainFrame.updateFromLogin(false, null);
					//we don't remove the observer because we could login later
//				removeItself();
				

			}
		});

		reg.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				LoginToMainFrame.openRegisteringPanel();
			}
		});

		getButtonSpace().add(log);
		getButtonSpace().add(reg);
		getButtonSpace().add(guest);

		repaint();
		revalidate();
	}
	

	

}
