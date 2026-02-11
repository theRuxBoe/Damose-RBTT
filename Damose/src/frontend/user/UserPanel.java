package frontend.user;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.io.IOException;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import backend.user.NoAccountExistsYet;
import backend.user.UserDB;
import frontend.main.MainFrame;

public class UserPanel extends JPanel{

	private GridBagConstraints gbc = new GridBagConstraints();
	private Color defaultColor = new Color(0x7851a9);
	
	private JTextField name;
	private JPasswordField pwd;
	private JPanel buttonSpace;
	
	private MainFrame observer;
	private static UserDB db;
	
	
	
	public UserPanel() {
		super();
		setLayout(new GridBagLayout());
//		setMaximumSize(new Dimension(300, 300));
		setBackground(defaultColor);
		
		
		
		addLabel();

		addInnerPanel();

		addButtonsPanel();
		
		
	}
	
	protected void addLabel() {
		JLabel lab = new JLabel("Damose", JLabel.CENTER);
		lab.setForeground(Color.WHITE);
		lab.setFont(new Font("Monospaced", Font.BOLD, 40));
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.weightx = 0.5;
		gbc.weighty = 0.5;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		
		add(lab, gbc);
	}
	
	protected void addInnerPanel() {
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
		this.name = inputname;
		p.add(n);
		p.add(inputname);
		
		JLabel pwd = new JLabel("Enter password : ");
		pwd.setForeground(Color.WHITE);
		JPasswordField inpwd = new JPasswordField(20);
		this.pwd= inpwd;
		
		p.add(pwd);
		p.add(inpwd);
		
		p.setBackground(defaultColor);
		add(p, gbc);
	}
	
	protected void addButtonsPanel() {
		JPanel p = new JPanel();
		gbc.gridx = 0;
		gbc.gridy = 5;
		gbc.anchor = GridBagConstraints.SOUTH;
		
		p.setLayout(new FlowLayout());
		p.setBackground(defaultColor);
		
		buttonSpace = p;
		this.add(p, gbc);
		
	}

	public void openDB() {
		if (db == null) {
		try {
			UserDB dab = new UserDB();
			db = dab;
		}
		catch (IOException e) {
			JOptionPane.showMessageDialog(observer, "There was an error with the DB");
		}
		}
	}
	
	public UserDB getDB() {
		return db;
	}
	
	public MainFrame getObserver() {
		return observer;
	}
	
	public void setObserver(MainFrame f) {
		observer = f;
	}
	
	public JPanel getButtonSpace() {
		return buttonSpace;
	}
	
	public JTextField getUserName() {
		return name;
	}
	
	public JPasswordField getPwdField() {
		return pwd;
	}
	
}
