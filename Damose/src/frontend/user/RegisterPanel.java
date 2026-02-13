package frontend.user;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JOptionPane;

import backend.user.AccountAlreadyExistsException;
import backend.user.UserDB;

/**
 * The Class RegisterPanel provides a graphic interface
 * to the user who wants to register himself through 
 * an account.
 */
public class RegisterPanel extends UserPanel{

	
	/**
	 * Instantiates a new register panel.
	 */
	public RegisterPanel() {
		super();
		addButtons();
		
	}
	
	
	/**
	 * Adds the two buttons, one is the back button to return to
	 * the login panel and the other one is the register button.
	 * <p>
	 * The back button calls the login to main frame class
	 * and opens the login panel.
	 * <p>
	 * The register button calls the users' database and
	 * attempts to create a new account with the given informations.
	 */
	private void addButtons() {
		
		JButton back = new JButton("Torna indietro");
		back.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				LoginToMainFrame.openLogin(getObserver());
			}
		});
		
		JButton reg = new JButton("Register");
		reg.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				UserDB db = getDB();
				try {
					db.createNewAccount(getUsername().getText(), new String(getPwdField().getPassword()));
					JOptionPane.showMessageDialog(RegisterPanel.this, "You have registered succesfully!");
					
				}
				
				catch (IOException e1) {
					JOptionPane.showMessageDialog(RegisterPanel.this, "I/O error with the database !");
				}
				
				catch (AccountAlreadyExistsException e2) {
					JOptionPane.showMessageDialog(RegisterPanel.this, e2.getMessage());
				}
				
				catch (IllegalArgumentException e3) {
					JOptionPane.showMessageDialog(RegisterPanel.this, e3.getMessage());
				}
				
			}
		});
		
		getButtonSpace().add(back);
		getButtonSpace().add(reg);
		repaint();
		revalidate();
		
	}
	
}
