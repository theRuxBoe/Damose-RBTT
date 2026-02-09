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
		openDB();
		

	}

//	private void addLabel() {
//		JLabel lab = new JLabel("Damose", JLabel.CENTER);
//		lab.setForeground(Color.WHITE);
//		lab.setFont(new Font("Monospaced", Font.BOLD, 40));
////		lab.setPreferredSize(new Dimension(100, 100));
//		gbc.gridx = 0;
//		gbc.gridy = 0;
//		gbc.weightx = 0.5;
//		gbc.weighty = 0.5;
//		gbc.fill = GridBagConstraints.HORIZONTAL;
//		
//		add(lab, gbc);
//	}

//	private void addInnerPanel() {
//		JPanel innerPanel = new JPanel();
//		innerPanel.setLayout(new BoxLayout(innerPanel, BoxLayout.PAGE_AXIS));
//		innerPanel.setBackground(defaultColor);
//		
//		JTextField name = new JTextField("Name", 20);
//		this.name = name;
//		name.setHorizontalAlignment(JTextField.CENTER);
//		name.addFocusListener(new FocusListener() {
//
//			@Override
//			public void focusLost(FocusEvent e) {
//			}
//
//			@Override
//			public void focusGained(FocusEvent e) {
//				name.setText("");
//			}
//		});
//
//		JPasswordField pwd = new JPasswordField("Password", 20);
//		this.pwd = pwd;
//		pwd.setHorizontalAlignment(JTextField.CENTER);
//		pwd.addFocusListener(new FocusListener() {
//
//			@Override
//			public void focusLost(FocusEvent e) {
//			}
//
//			@Override
//			public void focusGained(FocusEvent e) {
//				pwd.setText("");
//			}
//		});
//		gbc.gridx = 0;
//		gbc.gridy = 1;
//		gbc.gridheight = 4;
//		gbc.weightx = 0.2;
//		gbc.weighty = 0.2;
//		gbc.fill = GridBagConstraints.NONE;
//		
//		innerPanel.add(name);
//		innerPanel.add(pwd);
//		
//		add(innerPanel, gbc);
//	}

	

	private void addButtons() {
		
		JButton log = new JButton("Login");
		JButton guest = new JButton("Enter as Guest");
		JButton reg = new JButton("Register");

		log.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				
				boolean succesfulLogin = false;
				try {
					succesfulLogin = getDB().logIn(getUserName().getText(), new String(getPwdField().getPassword()));
					
				}
				
				catch (IllegalArgumentException iarg) {
					JOptionPane.showMessageDialog(LoginPanel.this, iarg.getMessage());
					
				}
				catch (NoAccountExistsYet nyet) {
					JOptionPane.showMessageDialog(LoginPanel.this, nyet.getMessage());
				}
				if (succesfulLogin) {
				getObserver().update(succesfulLogin);
				removeItself();
				removeObserver();
				}
				
				
				
			}
		});

		guest.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				getObserver().update(false);
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
//		this.add(p, gbc);
	}
	
	private void removeItself() {
		getObserver().remove(this);
	}


	public void removeObserver() {
		setObserver(null);
	}

	
//	private boolean isUserValid() {
//		db.logIn(name, name)
//		
//		return true;
	

//	public getName() {
//		return name;
//	}

}
