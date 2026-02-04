package frontend.user;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
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
	
	
	
	public RegisterPanel() {
		super();
		setLayout(new BorderLayout());
		setPreferredSize(new Dimension(300, 300));
		setLocation(new Point(800, 300));
		setBackground(defaultcolor);
		
		addLabel();
		addInputField();
		addButtons();
		
	}
	
	private void addLabel() {
//		JPanel p1 = new JPanel();
//		setLayout(new FlowLayout(FlowLayout.CENTER));
//		p1.setLayout(new BoxLayout(p1, BoxLayout.PAGE_AXIS));
//		p1.setBackground(new Color(defaultcolor));
		JLabel dam = new JLabel("Damose", JLabel.CENTER);
//		dam.setAlignmentX(CENTER_ALIGNMENT);
		dam.setFont(new Font("Serif", Font.BOLD, 20));
		dam.setPreferredSize(new Dimension(100,100));
		
		
//		JLabel rbtt = new JLabel("Rome Bus Transit Tracker", JLabel.CENTER);
//		rbtt.setFont(new Font("Monospaced", Font.BOLD, 15));
//		p1.setPreferredSize(new Dimension(100, 100));
		add(dam,BorderLayout.NORTH);
//		p1.add(rbtt);
//		add(p1, BorderLayout.NORTH);
	}
	
	private void addInputField() {
		JPanel p = new JPanel();
		JLabel n = new JLabel("Name : ");
		JTextField inputname = new JTextField(20);
		this.name = inputname.getText();
		p.add(n);
		p.add(inputname);
		
		JLabel pwd = new JLabel("Enter password : ");
		JPasswordField inpwd = new JPasswordField(20);
		
		p.add(pwd);
		p.add(inpwd);
		
//		in futuro possiamo aggiungere una verifica della password
//		p.add(x);
//		p.add(inpwd);
		
		p.setBackground(defaultcolor);
		add(p, BorderLayout.CENTER);
		
	}
	
	private void addButtons() {
		JPanel p = new JPanel();
		p.setBackground(new Color(0x7851a9));
		
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
		
		this.add(p, BorderLayout.SOUTH);
	}
	
	public void addObserver(MainFrame f) {
		observer = f;
	}
}
