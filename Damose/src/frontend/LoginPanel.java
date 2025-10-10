package frontend;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
//import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
//import java.util.Arrays;

import javax.swing.*;


public class LoginPanel extends JPanel{

	private JTextField name;
	private JPasswordField pwd;
	private JPanel pane;
	
	public LoginPanel() {
		openLoginPanel();
	}
	
	private void openLoginPanel() {
//		opens the login panel where you can type name and password for all-feature access
		JPanel panel = new JPanel();
		JPanel innerPanel = new JPanel(new FlowLayout());
//		panel.setLayout();
		JTextField name = new JTextField("Name", 20);
		name.setHorizontalAlignment(JTextField.CENTER);
		
		JPasswordField pwd = new JPasswordField("Password", 20);
		pwd.setHorizontalAlignment(JTextField.CENTER);
		
//		TODO I need to understand if this design is good or could be better
		name.addFocusListener(new FocusListener() {
			
			@Override
			public void focusLost(FocusEvent e) {
				name.setText("Name");
			}
			
			@Override
			public void focusGained(FocusEvent e) {
				name.setText("");
			}
		});
		
		pwd.addFocusListener(new FocusListener() {
			
			@Override
			public void focusLost(FocusEvent e) {
				pwd.setText("password");
			}
			
			@Override
			public void focusGained(FocusEvent e) {
				pwd.setText("");
			}
		});
		
		this.name = name;
		this.pwd = pwd;
		innerPanel.add(name);
		innerPanel.add(pwd);
		
		panel.setLayout(new BorderLayout());
//		panel.setLayer(innerPanel, 0);
		panel.add(innerPanel, BorderLayout.CENTER);
		panel.add(new JLabel("Damose - Rome Bus Transit Tracker"), BorderLayout.NORTH);
		panel.add(new JButton("Login"), BorderLayout.SOUTH);
		panel.setVisible(true);
		this.pane = panel;
	}
	
	public boolean isUserValid() {
		
//		checks the database for user's data
		String n = name.getText();
		char[] p = getPwd();
		
//		retrieves data from user's info file
		
//		code
		
//		code
		
//		checks if exists an user with the same name and then compares the passwords
//		TODO should we use an exception or just a message? I would prefer the second one!
		
		p = new char[10];
		return true;
	}
	
	public JPanel getLoginPanel() {
		return pane;
	}
	
	public String getName() {
		return name.getText();
	}
	
	private char[] getPwd() {
		return pwd.getPassword();
	}
	
	public static void main(String[] args) {
		LoginPanel l = new LoginPanel();
		JFrame f = new JFrame();
		
		
		
		f.setLayout(new BorderLayout());
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		f.add(l.getLoginPanel(), BorderLayout.CENTER);
		f.pack();
		f.setVisible(true);
		f.requestFocus();
		
	}
	
}
