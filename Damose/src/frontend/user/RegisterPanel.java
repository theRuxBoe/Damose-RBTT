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

public class RegisterPanel extends UserPanel{//extends LoginPanel {

	
	public RegisterPanel() {
		super();
//		setLayout(new GridBagLayout());
//		
//		setPreferredSize(new Dimension(300, 300));
////		setLocation(new Point(800, 300));
//		setBackground(defaultcolor);
		
//		addLabel();
//		addInputField();
//		openDB();
		
		addButtons();
		
	}
	
//	private void addLabel() {
//		JLabel dam = new JLabel("Damose", JLabel.CENTER);
//		dam.setForeground(Color.WHITE);
//		dam.setFont(new Font("Monospaced", Font.BOLD, 40));
////		dam.setPreferredSize(new Dimension(100,100));
////		gbc.insets = new Insets(5,5,5,5);
//		gbc.gridx = 0;
//		gbc.gridy = 0;
//		gbc.weightx = 0.5;
//		gbc.weighty = 0.5;
//		gbc.fill = GridBagConstraints.HORIZONTAL;
//		
//		add(dam, gbc);
//	}
//	
//	private void addInputField() {
//		JPanel p = new JPanel();
//		p.setLayout(new BoxLayout(p, BoxLayout.PAGE_AXIS));
//		gbc.gridx = 0;
//		gbc.gridy = 1;
//		gbc.gridheight = 4;
//		gbc.weightx = 0.2;
//		gbc.weighty = 0.2;
//		gbc.fill = GridBagConstraints.NONE;
//		
//		JLabel n = new JLabel("Name : ");
//		n.setForeground(Color.WHITE);
//		JTextField inputname = new JTextField(20);
//		this.name = inputname;
//		p.add(n);
//		p.add(inputname);
//		
//		JLabel pwd = new JLabel("Enter password : ");
//		pwd.setForeground(Color.WHITE);
//		JPasswordField inpwd = new JPasswordField(20);
//		this.pwd= inpwd;
//		
//		p.add(pwd);
//		p.add(inpwd);
//		
////		in futuro possiamo aggiungere una verifica della password
////		p.add(x);
////		p.add(inpwd);
//		
//		p.setBackground(defaultcolor);
//		add(p, gbc);
//		
//	}
	
	private void addButtons() {
//		JPanel p = new JPanel();
//		p.setBackground(defaultColor);
//		p.setBackground(Color.BLACK);
		
		JButton back = new JButton("Back");
		back.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				LoginToMainFrame.openLogin(getObserver());
//				RegisterPanel.clear();
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
		
//		p.add(back);
//		p.add(reg);
//		gbc.gridx = 0;
//		gbc.gridy = 5;
//		gbc.anchor = GridBagConstraints.SOUTH;
		
		getButtonSpace().add(back);
		getButtonSpace().add(reg);
		repaint();
		revalidate();
		
//		this.add(p, gbc);
	}
	
//	public void addObserver(MainFrame f) {
//		observer = f;
//	}
}
