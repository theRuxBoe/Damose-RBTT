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

import javax.swing.*;

import frontend.LoginToMainFrame;
import frontend.MainFrame;

public class LoginPanel extends JPanel {

	private String name;
	private char[] pwd;
	private MainFrame observer;
	private GridBagConstraints gbc = new GridBagConstraints();
	private Color defaultColor = new Color(0x7851a9);

	public LoginPanel() {
		super(new BorderLayout());
		setLayout(new GridBagLayout());
		
		setPreferredSize(new Dimension(300, 300));
//		setLocation(new Point(800, 300));
		setBackground(defaultColor);
		addLabel();

		addInnerPanel();

		addButtons();
//		this.requestFocus();

	}

	private void addLabel() {
		JLabel lab = new JLabel("Damose", JLabel.CENTER);
		lab.setForeground(Color.WHITE);
		lab.setFont(new Font("Monospaced", Font.BOLD, 40));
//		lab.setPreferredSize(new Dimension(100, 100));
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.weightx = 0.5;
		gbc.weighty = 0.5;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		
		add(lab, gbc);
	}

	private void addInnerPanel() {
		JPanel innerPanel = new JPanel();
		innerPanel.setLayout(new BoxLayout(innerPanel, BoxLayout.PAGE_AXIS));
		innerPanel.setBackground(defaultColor);
		
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
		gbc.gridx = 0;
		gbc.gridy = 1;
		gbc.gridheight = 4;
		gbc.weightx = 0.2;
		gbc.weighty = 0.2;
		gbc.fill = GridBagConstraints.NONE;
		
		innerPanel.add(name);
		innerPanel.add(pwd);
		
		add(innerPanel, gbc);
	}

	

	private void addButtons() {
		JPanel p = new JPanel();
		gbc.gridx = 0;
		gbc.gridy = 5;
		gbc.anchor = GridBagConstraints.SOUTH;
		
		p.setLayout(new FlowLayout());
		p.setBackground(defaultColor);
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

		this.add(p, gbc);
	}
	
	private void removeItself() {
		observer.remove(this);
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
