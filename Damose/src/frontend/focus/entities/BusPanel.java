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
import java.awt.Insets;import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalTime;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.concurrent.Flow;

import javax.swing.*;
import javax.swing.border.BevelBorder;

import org.jxmapviewer.viewer.GeoPosition;

import backend.model.Corsa;
import backend.model.PredizioneArrivo;
import backend.realtime.VehiclePositionInfo;
import backend.service.ProvaTransitService;
import backend.service.TransitService;
import frontend.MainFrame;
import frontend.MapPanel;
import frontend.utilities.WaypointRenderer;
import frontend.waypoints.BusWaypoint;

//	lo chiamiamo bus panel ma in realtà è un'astrazione di quello che arriva alla fermata
public class BusPanel extends JPanel {
	
	private GeoPosition position;
	private PredizioneArrivo b ;
	private int id;
	private int line;
	private String direction;
	private int seats_available;
	private int estimatedTime;
	private GridBagConstraints gbc = new GridBagConstraints();
	
//		forse conviene tenere in memoria il bus piuttosto che copiare ogni campo ???
	
	public BusPanel(PredizioneArrivo b) {
		super();
		this.b = b;
		setLayout(new GridBagLayout());
		setBorder(new BevelBorder(BevelBorder.LOWERED));
		
		
//		
		
		addLine();
		addTime();
	}
	
	
	private void addLine() {
		JPanel linep = new JPanel();
		JLabel l = new JLabel("" + b.getRouteId(), JLabel.CENTER);
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
		JLabel l2 = new JLabel(b.getDirectionName());
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
		JLabel l3 = new JLabel(b.getTripId() + " , Seats : " + "DaTO non disponibile");		//quando daniele aggiungerà i posti potremo metterli qui
		p3.add(l3);
		
		gbc.gridx = 3;
		gbc.gridy = 1;
		add(p3, gbc);
		
		
	}
	

	private void addTime() {
		JPanel p = new JPanel();
		JLabel l = new JLabel("" + LocalTime.from(b.getArrivalTime()) + "minutes (??)");
		l.setFont(new Font("Normal", Font.BOLD, 30));
		
		l.setForeground(Color.GREEN);
		
		p.add(l);
		Timer t = new Timer(30000, new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					VehiclePositionInfo x = MainFrame.getTTS().getVehiclePositionForTripId(b.getTripId()).get();
					WaypointRenderer.paintWaypoints(new BusWaypoint(new GeoPosition(x.getLat(), x.getLon())));			//uno dei punti su cui intervenire per gestire diversi tipi di mezzo
//					MapPanel.getMapViewer().setCenterPosition(b.getPosition());
					l.setText("" + LocalTime.from(b.getArrivalTime()) + " minutes");
				}
				catch (NoSuchElementException exception) {
					exception.printStackTrace();
//					dovremmo passare ai dati statici in caso
				}
				
				repaint();
				revalidate();
				
			}
		});
		t.start();
		gbc.weightx = 0.3;
		gbc.weighty = 0.3;
		
		gbc.gridx = 8;
		gbc.gridy = 0;
		gbc.fill = GridBagConstraints.VERTICAL;
		
		this.add(p, gbc);
		
	}
	
	

	
}
