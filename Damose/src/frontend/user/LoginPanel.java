package frontend.user;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.io.IOException;

import javax.swing.*;

import backend.favorite.FavoriteRouteDB;
import backend.user.NoAccountExistsYet;
import backend.user.User;
import backend.user.UserDB;
import frontend.main.MainFrame;

public class LoginPanel extends UserPanel {

	private static UserDB db;

	public LoginPanel() {
		super();
//		setLayout(new GridBagLayout());
//		
//		setPreferredSize(new Dimension(300, 300));
////		setLocation(new Point(800, 300));
//		setBackground(defaultColor);
//		try {
//			UserDB dab = new UserDB();
//			db = dab;
//		}
//		catch (IOException e) {
//			JOptionPane.showMessageDialog(observer, "There was an error with the DB", "DB error", JOptionPane.ERROR_MESSAGE);
//		}
//		
//		addLabel();
//
//		addInnerPanel();
//
		addButtons();
		
		

	}


	

	private void addButtons() {
		
		JButton log = new JButton("Login");
		JButton guest = new JButton("Enter as Guest");
		JButton reg = new JButton("Register");

		log.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				openDB();
				boolean succesfulLogin = false;
				String user = getUserName().getText();
				try {
					
					succesfulLogin = getDB().logIn(user, new String(getPwdField().getPassword()));
					
				}
				
				catch (IllegalArgumentException iarg) {
					JOptionPane.showMessageDialog(LoginPanel.this, iarg.getMessage());
					
				}
				catch (NoAccountExistsYet nyet) {
					JOptionPane.showMessageDialog(LoginPanel.this, nyet.getMessage());
				}
				if (succesfulLogin) {
				FavouritesDBManager dbm = new FavouritesDBManager();
				dbm.openDBs();
					
				getObserver().update(succesfulLogin, user);
				removeItself();
				removeObserver();
				}
				
				
				
			}
		});

		guest.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				getObserver().update(false, null);
					//we don't remove the observer because we could login later
				removeItself();

			}
		});

		reg.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				LoginToMainFrame.openRegisteringPanel(getObserver());
			}
		});

		getButtonSpace().add(log);
		getButtonSpace().add(reg);
		getButtonSpace().add(guest);

		repaint();
		revalidate();
	}
	
	private void removeItself() {
		getObserver().remove(this);
	}


	public void removeObserver() {
		setObserver(null);
	}

	

}
