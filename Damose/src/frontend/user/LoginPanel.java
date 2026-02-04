package frontend.user;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

import javax.swing.*;

import frontend.LoginToMainFrame;
import frontend.MainFrame;

public class LoginPanel extends JPanel {

	private String name;
	private char[] pwd;
	private MainFrame observer;
//	private RegisterPanel registerPanel;

	public LoginPanel() {
		super(new BorderLayout());
		setPreferredSize(new Dimension(300, 300));
//		setLocation(new Point(800, 300));
		setBackground(new Color(0x7851a9));
		addLabel();

		addInnerPanel();

		addButtons();
//		this.requestFocus();

	}

	private void addLabel() {
		JLabel lab = new JLabel("Damose - Rome Bus Transit Tracker", JLabel.CENTER);
		lab.setFont(new Font("Monospaced", Font.BOLD, 15));

		lab.setPreferredSize(new Dimension(100, 100));

		this.add(lab, BorderLayout.NORTH);
	}

//	private void addRegisterPanel() {
//		RegisterPanel regi = new RegisterPanel();
//		this.registerPanel = regi;
//		regi.openPanel();
//		
//	}

	private void addInnerPanel() {
		JPanel innerPanel = new JPanel(new FlowLayout());
		innerPanel.setBackground(new Color(0x7851a9));

		JTextField name = new JTextField("Name", 20);
		name.setHorizontalAlignment(JTextField.CENTER);
		name.addFocusListener(new FocusListener() {

			@Override
			public void focusLost(FocusEvent e) {
			}

			@Override
			public void focusGained(FocusEvent e) {
				name.setText("");
			}
		});

		JPasswordField pwd = new JPasswordField("Password", 20);
		pwd.setHorizontalAlignment(JTextField.CENTER);
		pwd.addFocusListener(new FocusListener() {

			@Override
			public void focusLost(FocusEvent e) {
			}

			@Override
			public void focusGained(FocusEvent e) {
				pwd.setText("");
			}
		});

		this.name = name.getText();
		this.pwd = pwd.getPassword();
		innerPanel.add(name);
		innerPanel.add(pwd);

		this.add(innerPanel, BorderLayout.CENTER);
	}

	private void removeItself() {
		observer.remove(this);
	}

	private void addButtons() {
		JPanel p = new JPanel();
		p.setLayout(new FlowLayout());
		p.setBackground(new Color(0x7851a9));
		JButton log = new JButton("Login");
		JButton guest = new JButton("Enter as Guest");
		JButton reg = new JButton("Register");

		log.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				if (isUserValid()) {
					observer.update(true);
					removeItself();
					removeObserver();
				} else {
					JOptionPane.showMessageDialog(p, "I dati inseriti non sono validi !");
				}
				
				
			}
		});

		guest.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				observer.update(false);
//				removeObserver();			//we don't remove the observer because we could login later
				removeItself();

			}
		});

		reg.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				LoginToMainFrame.openRegisteringPanel(observer);
			}
		});

		p.add(log);
		p.add(reg);
		p.add(guest);

		this.add(p, BorderLayout.SOUTH);
	}

	public void addObserver(MainFrame o) {
		observer = o;
	}

	public void removeObserver() {
		observer = null;
	}

	private boolean isUserValid() {

//		if (name.isPresent() && name.getPwd() == this.pwd ) {
//			this.pwd = new char[10];
//			return true;
//		}
//		return false;
		return true;
	}

	public String getName() {
		return name;
	}

}
