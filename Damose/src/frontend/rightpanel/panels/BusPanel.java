package frontend.rightpanel.panels;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
//import java.awt.FlowLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.NoSuchElementException;

import javax.swing.*;

import org.jxmapviewer.viewer.GeoPosition;

import backend.model.PredizioneArrivo;

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
//		setBorder(new BevelBorder(BevelBorder.LOWERED));
		
		
//		
		
		addLine();
		addTime();
		setMaximumSize(getPreferredSize());
		setAlignmentX(Component.LEFT_ALIGNMENT);
//		showOnMap();
	}
	
	
	private void addLine() {
		JPanel linep = new JPanel();
		linep.setLayout(new BorderLayout());
		JLabel l = new JLabel("" + b.getRouteId(), JLabel.CENTER);
		l.setForeground(Color.WHITE);
		l.setFont(new Font("Normal", Font.BOLD, 40));
		linep.setBackground(Color.RED);
		linep.add(l, BorderLayout.CENTER);
		
		gbc.insets = new Insets(10, 5, 5, 10);		
		gbc.weightx = 0.2;
		gbc.weighty = 0.2;
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.gridwidth =3;
		gbc.gridheight = 3;
		gbc.anchor = GridBagConstraints.CENTER;
//		gbc.fill = GridBagConstraints.BOTH;
		
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
		JLabel l3 = new JLabel(b.getTripId() + " , Seats : " + "N/A");		
		p3.add(l3);
		
		gbc.gridx = 3;
		gbc.gridy = 1;
		add(p3, gbc);
		
		
	}
	

	private void addTime() {
		JPanel p = new JPanel();
		JLabel l = new JLabel(b.getArrivalTime().getMinute() + " min"); //calculateTime(b.getArrivalTime())
		l.setFont(new Font("Normal", Font.BOLD, 30));
		
		if (b.isRealTime()) {
			l.setForeground(Color.GREEN);
		}
		
		
		p.add(l);
		Timer t = new Timer(30000, new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
//					VehiclePositionInfo x = MainFrame.getTTS().getVehiclePositionForTripId(b.getTripId()).get();
//					WaypointManager.paintBus(new BusWaypoint(new GeoPosition(x.getLat(), x.getLon())), new WaypointPainter<BusWaypoint>());			//uno dei punti su cui intervenire per gestire diversi tipi di mezzo
//					MapPanel.getMapViewer().setCenterPosition(b.getPosition());
					l.setText(b.getArrivalTime().getMinute() + " min");
					
				}
				catch (NoSuchElementException exception) {
					
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
	

//	public static void main(String[] args) {
//		JFrame f = new JFrame();
//		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//		f.setLayout(new FlowLayout());
//		f.add(new BusPanel(new PredizioneArrivo("1234", "716", "45645", "domodossola", LocalTime.now())));
//		f.setVisible(true);
//		f.pack();
//	}
	
}
