package frontend.rightpanel.panels;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.NoSuchElementException;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.Timer;

import org.jxmapviewer.viewer.GeoPosition;
import org.jxmapviewer.viewer.WaypointPainter;

import backend.model.PredizioneArrivo;
import backend.model.RouteType;
import backend.realtime.VehiclePositionInfo;
import frontend.main.BackendController;
import frontend.main.MainFrame;
import frontend.waypoints.GenericWaypoint;
import frontend.waypoints.WaypointManager;

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
	 * Adds the line.
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
		JLabel l2 = new JLabel(vehicle.getDirectionName());
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
	 * Adds the time.
	 */
	private void addTime() {
		JPanel p = new JPanel();
		JLabel l = new JLabel(vehicle.getArrivalTime().getMinute() + " min"); // calculateTime(b.getArrivalTime())
		l.setFont(new Font("Normal", Font.BOLD, 30));

		if (vehicle.isRealTime()) {
			l.setForeground(Color.GREEN);
		}

		p.add(l);
		Timer t = new Timer(40000, e -> {
			try {
				VehiclePositionInfo x = BackendController.getTTS().getVehiclePositionForTripId(vehicle.getTripId()).get();
				GeoPosition geop = new GeoPosition(x.getLat(), x.getLon());

				WaypointManager.paintVehicle(geop, new WaypointPainter<GenericWaypoint>(), type);

			} catch (NoSuchElementException exception) {

			}
			l.setText(vehicle.getArrivalTime().getMinute() + " min");
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

}
