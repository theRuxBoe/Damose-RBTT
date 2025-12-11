package frontend.user;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
//import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.beans.PropertyEditor;
import java.util.ArrayList;
//import java.util.Arrays;
import java.util.List;

import javax.swing.*;

import frontend.MainFrame;


public class LoginPanel extends JPanel {

	
	private String name;
	private char[] pwd;
	private MainFrame observer;
	
	
	public LoginPanel() {
		super(new BorderLayout());
		add(new JLabel("Damose - Rome Bus Transit Tracker"), BorderLayout.NORTH);
		addInnerPanel();
		
		addButtons();
		setPreferredSize(new Dimension(300,300));
	}
	
	private void addInnerPanel() {
		JPanel innerPanel = new JPanel(new FlowLayout());
		JTextField name = new JTextField("Name", 20);
		name.setHorizontalAlignment(JTextField.CENTER);
		
		JPasswordField pwd = new JPasswordField("Password", 20);
		pwd.setHorizontalAlignment(JTextField.CENTER);
		
		name.addFocusListener(new FocusListener() {
			
			@Override
			public void focusLost(FocusEvent e) {
			}
			
			@Override
			public void focusGained(FocusEvent e) {
				name.setText("");
			}
		});
		
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
		
//		panel.setLayer(innerPanel, 0);
		this.add(innerPanel, BorderLayout.CENTER);
	}
	
	private void addButtons() {
		JPanel p = new JPanel();
		p.setLayout(new FlowLayout());
		
		JButton b = new JButton("Login");
		JButton g = new JButton("Enter as Guest");
		p.add(b);
		p.add(g);
		
		b.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				if(isUserValid()) {
					observer.update(true);
					removeObserver();		
				}
				
			}
		});
		
		g.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				observer.update(false);
//				removeObserver();			//we don't remove the observer because we could login later
				
			}
		});
		
		this.add(p, BorderLayout.SOUTH);
	}
	
	public void addObserver(MainFrame o) {
		observer = o;
	}
	
	public void removeObserver() {
		observer = null;
		}
	
	private boolean isUserValid(){
		
		if (name.isPresent() && name.getPwd() == this.pwd ) {
			this.pwd = new char[10];
			return true;
		}
		return false;
	}
	
	
	public String getName() {
		return name;
	}
	
	
	public static void main(String[] args) {
		JFrame f = new JFrame();
		LoginPanel l = new LoginPanel();
		f.add(l);
		f.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		f.pack();
		f.setVisible(true);
	}
	
}
