package frontend.user;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import backend.user.AccountAlreadyExistsException;
import backend.user.UserDB;
import frontend.main.MainFrame;

public class RegisterPanel extends UserPanel{

	
	public RegisterPanel() {
		super();
		
		addButtons();
		
	}
	
	
	private void addButtons() {
		
		JButton back = new JButton("Back");
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
					db.createNewAccount(getUserName().getText(), new String(getPwdField().getPassword()));
					JOptionPane.showMessageDialog(RegisterPanel.this, "You have registered succesfully!");
					
				}
				
				catch (IOException er) {
					JOptionPane.showMessageDialog(RegisterPanel.this, "I/O error with the database !");
				}
				
				catch (AccountAlreadyExistsException aaee) {
					JOptionPane.showMessageDialog(RegisterPanel.this, aaee.getMessage());
				}
				
				catch (IllegalArgumentException iae) {
					JOptionPane.showMessageDialog(RegisterPanel.this, iae.getMessage());
				}
				
			}
		});
		
		getButtonSpace().add(back);
		getButtonSpace().add(reg);
		repaint();
		revalidate();
		
	}
	
}
