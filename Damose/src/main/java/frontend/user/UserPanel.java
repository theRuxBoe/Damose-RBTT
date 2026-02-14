package main.java.frontend.user;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.io.IOException;

import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import main.java.backend.user.UserDB;
import main.java.frontend.MainFrame;

/**
 * The Class UserPanel is used as a super class for both the login
 * panel and the register one.
 * It contains the shared functionalities between the two classes
 * to avoid code duplication.
 */
public class UserPanel extends JPanel{

	/** The grid bag constraints */
	private GridBagConstraints gbc = new GridBagConstraints();
	
	/** The default color. */
	private Color defaultColor = new Color(0x7851a9);
	
	/** The j text field for the user's name. */
	private JTextField name;
	
	/** The j text field for the user's password. */
	private JPasswordField pwd;
	
	/** The panel allocated for the buttons. */
	private JPanel buttonSpace;
	
	/** The observer, i.e. the frame to notify when the user is logged. */
	private MainFrame observer;
	
	/** The users DB. */
	private static UserDB db;
	
	
	
	/**
	 * Instantiates a new user panel with gridbag layout,
	 * the central label with the project's name and the two fields
	 * for name and password.
	 */
	public UserPanel() {
		super();
		setLayout(new GridBagLayout());
		setBackground(defaultColor);
		
		addLabel();

		addInnerPanel();

		addButtonsPanel();
		
		
	}
	
	/**
	 * Adds the central logo image.
	 */
	public void addLabel() {
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.weightx = 0.25;
		gbc.weighty = 0.25;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		ImageIcon pic = null;
			pic = new ImageIcon(getClass().getResource("/main/res/damose_logo.png"));
			JLabel picLabel = new JLabel(pic);
			add(picLabel, gbc);
	}
	
	/**
	 * Adds the inner panel containing the j text field for the name
	 * and the j password field for the password.
	 */
	public void addInnerPanel() {
		JPanel p = new JPanel();
		p.setLayout(new BoxLayout(p, BoxLayout.PAGE_AXIS));
		gbc.gridx = 0;
		gbc.gridy = 1;
		gbc.gridheight = 3;
		gbc.weightx = 0.3;
		gbc.weighty = 0.3;
		gbc.fill = GridBagConstraints.NONE;
		JLabel pwd = new JLabel("Inserisci password : ", JLabel.RIGHT);
		pwd.setFont(new Font("normal", Font.PLAIN, 25));
		pwd.setPreferredSize(new Dimension(300,40));
		
		JLabel n = new JLabel("Nome : ", JLabel.RIGHT);
		
		n.setFont(new Font("normal", Font.PLAIN, 25));
		n.setForeground(Color.WHITE);
		
		n.setPreferredSize(new Dimension(300,40));
		
		JPanel p1 = new JPanel();
		p1.setBackground(defaultColor);

		
		
		p1.add(n);
				
		JTextField inputname = new JTextField(20);
		this.name = inputname;
		
		p1.add(inputname);
		
		p.add(p1);
		
		JPanel p2 = new JPanel();
		p2.setBackground(defaultColor);
		
		
		
		pwd.setForeground(Color.WHITE);
		
		p2.add(pwd);
		
		
		JPasswordField inpwd = new JPasswordField(20);
		this.pwd= inpwd;
		
		p2.add(inpwd);
		
		p.add(p2);
		
		p.setBackground(defaultColor);
		add(p, gbc);
	}
	
	/**
	 * Adds the buttons panel, a placeholder to be used by the login 
	 * panel and register panel to add their buttons.
	 */
	protected void addButtonsPanel() {
		JPanel p = new JPanel();
		gbc.gridx = 0;
		gbc.gridy = 4;
		gbc.anchor = GridBagConstraints.SOUTH;
		
		p.setLayout(new FlowLayout());
		p.setBackground(defaultColor);
		
		buttonSpace = p;
		this.add(p, gbc);
		
	}

	/**
	 * Opens the users' DB.
	 */
	public void openDB() {
		if (db == null) {
		try {
			UserDB dab = new UserDB();
			db = dab;
		}
		catch (IOException e) {
			JOptionPane.showMessageDialog(null, e.getMessage());
		}
		}
	}
	
	/**
	 * Gets the users' database.
	 *
	 * @return the users' database
	 */
	public UserDB getDB() {
		return db;
	}
	
	/**
	 * Gets the observer.
	 *
	 * @return the observer
	 */
	public MainFrame getObserver() {
		return observer;
	}
	
	/**
	 * Sets the MainFrame observer.
	 *
	 * @param f the new observer
	 */
	public void setObserver(MainFrame f) {
		observer = f;
	}
	
	/**
	 * Gets the panel to add buttons.
	 *
	 * @return the button space
	 */
	public JPanel getButtonSpace() {
		return buttonSpace;
	}
	
	/**
	 * Gets the j text field for the username.
	 *
	 * @return the user name
	 */
	public JTextField getUsername() {
		return name;
	}
	
	/**
	 * Gets the password field.
	 *
	 * @return the password field
	 */
	public JPasswordField getPwdField() {
		return pwd;
	}
	
	
}
