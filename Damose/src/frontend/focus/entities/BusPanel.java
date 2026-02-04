package frontend.focus.entities;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
//import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.util.concurrent.Flow;

import javax.swing.*;
import javax.swing.border.BevelBorder;

import org.jxmapviewer.viewer.GeoPosition;

import backendNOTPUSH.Bus;
import frontend.waypoints.BusWaypoint;

public class BusPanel extends JPanel {
	
	private GeoPosition position;
	private int id;
	private int line;
	private String direction;
	private int seats_available;
	private int estimatedTime;
	GridBagConstraints gbc = new GridBagConstraints();
	
//	5, 1, 11, 0
	
	public BusPanel(Bus b) {
		super();
//		setLayout(new BoxLayout(this, BoxLayout.LINE_AXIS));
		setLayout(new GridBagLayout());
		
		setBorder(new BevelBorder(BevelBorder.LOWERED));
		
		this.position = b.getPosition();
		this.id = b.getId();
		this.line = b.getLine();
		this.direction = b.getDirection();
		this.seats_available = b.getSeats_available();
		this.estimatedTime = b.getEstimatedTime();
		
		addLine();
		addTime();
	}
	
	
	private void addLine() {
		JPanel linep = new JPanel();
		JLabel l = new JLabel("" + line, JLabel.CENTER);
		l.setForeground(Color.WHITE);
		l.setFont(new Font("Normal", Font.BOLD, 40));
		linep.setBackground(Color.RED);
		linep.add(l);
		
		gbc.insets = new Insets(10, 5, 5, 10);		
		gbc.weightx = 0.2;
		gbc.weighty = 0.2;
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.gridwidth = 3;
		gbc.gridheight = 3;
		gbc.anchor = GridBagConstraints.CENTER;
		gbc.fill = GridBagConstraints.BOTH;
		
//		Dialog, DialogInput, Monospaced, Serif, or SansSerif
		
		add(linep, gbc);

		JPanel p2 = new JPanel();
		JLabel l2 = new JLabel(direction);
		l2.setFont(new Font("Dialog", Font.BOLD, 25));
		p2.add(l2);
		gbc.weightx = 0.3;
		gbc.weighty = 0.3;
		gbc.anchor = GridBagConstraints.LINE_START;
		gbc.gridx = 3;
		gbc.gridy = 0;
		gbc.gridwidth = 1;
		gbc.gridheight = 1;
		
		add(p2, gbc);
		
		JPanel p3 = new JPanel();
		JLabel l3 = new JLabel(id + " , Seats : " + seats_available);
		p3.add(l3);
		
		gbc.gridx = 3;
		gbc.gridy = 1;
//		gbc.gridwidth = 1;
//		gbc.gridheight = 1;
		add(p3, gbc);
		
		
	}
	

	private void addTime() {
//		con un timer questo deve recuperare regolarmente i dati
		JPanel p = new JPanel();
		JLabel l = new JLabel("" + estimatedTime + " minutes");
		l.setFont(new Font("Normal", Font.BOLD, 30));
//		p.setBackground(Color.CYAN);
		gbc.weightx = 0.3;
		gbc.weighty = 0.3;
		
		gbc.gridx = 8;
		gbc.gridy = 0;
//		gbc.gridheight = 3;
		gbc.fill = GridBagConstraints.VERTICAL;
		
		p.add(l);
		this.add(p, gbc);
		
	}
	

	public static void main(String[] args) {
		JFrame f = new JFrame();
		BusPanel b = new BusPanel(new Bus());
		f.add(b);
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.pack();
		f.setLocationRelativeTo(null);
		f.setVisible(true);
	}
	
}
