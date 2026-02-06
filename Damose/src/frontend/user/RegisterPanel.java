package frontend.user;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import frontend.LoginToMainFrame;
import frontend.MainFrame;

public class RegisterPanel extends JPanel{//extends LoginPanel {

	private MainFrame observer;
	private String name;
	private Color defaultcolor = new Color(0x7851a9);
	private GridBagConstraints gbc = new GridBagConstraints();
	
	
	
	public RegisterPanel() {
		super();
//		setLayout(new BorderLayout());
		setLayout(new GridBagLayout());
		
		setPreferredSize(new Dimension(300, 300));
//		setLocation(new Point(800, 300));
		setBackground(defaultcolor);
		
		addLabel();
		addInputField();
		addButtons();
		
	}
	
	private void addLabel() {
		JLabel dam = new JLabel("Damose - registration", JLabel.CENTER);
		dam.setForeground(Color.WHITE);
		dam.setFont(new Font("Serif", Font.BOLD, 20));
		dam.setPreferredSize(new Dimension(100,100));
//		gbc.insets = new Insets(5,5,5,5);
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.weightx = 0.5;
		gbc.weighty = 0.5;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		
		add(dam, gbc);
	}
	
	private void addInputField() {
		JPanel p = new JPanel();
		p.setLayout(new BoxLayout(p, BoxLayout.PAGE_AXIS));
		gbc.gridx = 0;
		gbc.gridy = 1;
		gbc.gridheight = 4;
		gbc.weightx = 0.2;
		gbc.weighty = 0.2;
		gbc.fill = GridBagConstraints.NONE;
		
		JLabel n = new JLabel("Name : ");
		n.setForeground(Color.WHITE);
		JTextField inputname = new JTextField(20);
		this.name = inputname.getText();
		p.add(n);
		p.add(inputname);
		
		JLabel pwd = new JLabel("Enter password : ");
		pwd.setForeground(Color.WHITE);
		JPasswordField inpwd = new JPasswordField(20);
		
		p.add(pwd);
		p.add(inpwd);
		
//		in futuro possiamo aggiungere una verifica della password
//		p.add(x);
//		p.add(inpwd);
		
		p.setBackground(defaultcolor);
		add(p, gbc);
		
	}
	
	private void addButtons() {
		JPanel p = new JPanel();
		p.setBackground(defaultcolor);
//		p.setBackground(Color.BLACK);
		
		JButton back = new JButton("Back");
		back.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				LoginToMainFrame.openLogin(observer);
//				RegisterPanel.clear();
			}
		});
		
		JButton reg = new JButton("Register");
		reg.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
//				aggiunge l'utente inserito dentro il database
				
				JOptionPane p = new JOptionPane();
				int x = p.showConfirmDialog(observer, "Are you sure you want to register?");
				if (x == 0) {
//					procedi con la registrazione
				}
			}
		});
		
		p.add(back);
		p.add(reg);
		gbc.gridx = 0;
		gbc.gridy = 5;
		gbc.anchor = GridBagConstraints.SOUTH;
		
		this.add(p, gbc);
	}
	
	public void addObserver(MainFrame f) {
		observer = f;
	}
}
