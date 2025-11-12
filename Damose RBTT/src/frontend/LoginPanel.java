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
<<<<<<< Updated upstream
	private JPanel pane;
	
	public LoginPanel() {
		openLoginPanel();
	}
	
	private void openLoginPanel() {
//		opens the login panel where you can type name and password for all-feature access
		JPanel panel = new JPanel();
=======
	
	public LoginPanel() {
		super(new BorderLayout());
		addInnerPanel();
	
	}
	
	private void addInnerPanel() {
//		opens the login panel where you can type name and password for all-feature access
>>>>>>> Stashed changes
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
		
<<<<<<< Updated upstream
		panel.setLayout(new BorderLayout());
//		panel.setLayer(innerPanel, 0);
		panel.add(innerPanel, BorderLayout.CENTER);
		panel.add(new JLabel("Damose - Rome Bus Transit Tracker"), BorderLayout.NORTH);
		panel.add(new JButton("Login"), BorderLayout.SOUTH);
		panel.setVisible(true);
		this.pane = panel;
=======
//		panel.setLayer(innerPanel, 0);
		this.add(innerPanel, BorderLayout.CENTER);
	}
	
	private void addInfos() {
		this.add(new JLabel("Damose - Rome Bus Transit Tracker"), BorderLayout.NORTH);
		this.add(new JButton("Login"), BorderLayout.SOUTH);
//		this.setVisible(true);
>>>>>>> Stashed changes
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
<<<<<<< Updated upstream
		return pane;
=======
		return this;
>>>>>>> Stashed changes
	}
	
	public String getName() {
		return name.getText();
	}
	
	private char[] getPwd() {
		return pwd.getPassword();
	}
	
<<<<<<< Updated upstream
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
=======
//	public static void main(String[] args) {
//		LoginPanel l = new LoginPanel();
//		JFrame f = new JFrame();
//		
//		
//		
//		f.setLayout(new BorderLayout());
//		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//		
//		f.add(l.getLoginPanel(), BorderLayout.CENTER);
//		f.pack();
//		f.setVisible(true);
//		f.requestFocus();
//		
//	}
>>>>>>> Stashed changes
	
}
