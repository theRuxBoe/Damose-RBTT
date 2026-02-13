package main.java.frontend.rightpanel.panels;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.time.LocalTime;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.Timer;


import main.java.backend.model.PredizioneArrivo;
import main.java.backend.model.RouteType;
import main.java.frontend.main.BackendController;

/**
 * The Class ArrivingVehiclePanel displays the representation
 * of a vehicle incoming to a stop.
 * <p>
 * When a {@link StopFocus} panel is opened, it creates a
 * list of {@link ArrivingVehiclePanel}s and proceeds to show them on the map through 
 * a method inside this one.
 */
public class ArrivingVehiclePanel extends JPanel {

	/** The arriving vehicle object. */
	private PredizioneArrivo vehicle;
	
	/** The grid bag constraints. */
	private GridBagConstraints gbc = new GridBagConstraints();
	
	/** The vehicle type. */
	private RouteType type;
	
	private Timer t;

	/**
	 * Instantiates a new bus panel from a given {@link PredizioneArrivo}.
	 *
	 * @param v the vehicle
	 */
	public ArrivingVehiclePanel(PredizioneArrivo v) {
		super();
		this.vehicle = v;
		this.type = BackendController.getTTS().cercaLinee(v.getRouteId()).getFirst().getRouteType();
		setLayout(new GridBagLayout());

		addLine();
		addTime();
		setMaximumSize(getPreferredSize());
		setAlignmentX(Component.LEFT_ALIGNMENT);
	}

	/**
	 * Adds the line and eventually available seats.
	 */
	private void addLine() {
		JPanel linep = new JPanel();
		linep.setLayout(new BorderLayout());
		JLabel l = new JLabel("" + vehicle.getRouteId(), JLabel.CENTER);
		l.setForeground(Color.WHITE);
		l.setFont(new Font("Normal", Font.BOLD, 40));
		linep.setBackground(Color.RED);
		linep.add(l, BorderLayout.CENTER);

		gbc.insets = new Insets(10, 5, 5, 10);
		gbc.weightx = 0.2;
		gbc.weighty = 0.2;
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.gridwidth = 3;
		gbc.gridheight = 3;
		gbc.anchor = GridBagConstraints.CENTER;

//		Dialog, DialogInput, Monospaced, Serif, or SansSerif

		add(linep, gbc);

		JPanel p2 = new JPanel();
		final JLabel l2 = new JLabel(vehicle.getDirectionName());
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
		String occ = vehicle.isRealTime()
				? BackendController.getTTS().getVehiclePositionForTripId(vehicle.getTripId()).get().getOccupancyLevel().toString()
				: "Non disponibili";
		JLabel l3 = new JLabel(vehicle.getTripId() + " , Posti : " + occ);
		p3.add(l3);

		gbc.gridx = 3;
		gbc.gridy = 1;
		add(p3, gbc);

	}

	/**
	 * Adds the time and automatically checks for vehicle's position every 30 seconds.
	 */
	private final void addTime() {
		JPanel p = new JPanel();
		JLabel l = new JLabel( calculateArrivingTime()); 
		l.setFont(new Font("Normal", Font.BOLD, 30));

		if ( vehicle.isRealTime()) {
			l.setForeground(Color.GREEN);
		}

		p.add(l);
		t = new Timer(40000, e -> {
			l.setText(calculateArrivingTime());
			repaint();
			revalidate();

		});
		t.start();
		gbc.weightx = 0.3;
		gbc.weighty = 0.3;

		gbc.gridx = 8;
		gbc.gridy = 0;
		gbc.fill = GridBagConstraints.VERTICAL;

		this.add(p, gbc);

	}
	
	/**
	 * Calculates the actual arriving time.
	 *
	 * @return the minutes to wait for the arrival
	 */
	private String calculateArrivingTime() {
		int res;
		LocalTime tArrivo = vehicle.getArrivalTime();
		
		LocalTime ora = LocalTime.now();
		int x = tArrivo.getMinute() - ora.getMinute();
		if (x < 1) {
			if (tArrivo.getHour() == ora.getHour()) {
				return "Ora";
			
				
				
			}
			else if (tArrivo.getHour() == ora.getHour()+1) {
				x += 60;
				res = x  ;
			
				
			}
			else {  
				x += 120;
				res = x ;
			
			}
			
		}
		else { 
			if (tArrivo.getHour() == ora.getHour()) {
				res = x ; }
			else if (tArrivo.getHour() == ora.getHour()+1){
				res = x + 60;
			}
			else { res = x + 120; }
		}
		
		if ( res > 60) {return "> 1 ora";}
		else { return res + " min"; }
		
	}
	
	/**
	 * Stops the current timer.
	 */
	
	public void stopTimer() {
		t.stop();
	}
	
}
