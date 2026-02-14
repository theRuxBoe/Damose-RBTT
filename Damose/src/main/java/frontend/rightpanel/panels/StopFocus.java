package main.java.frontend.rightpanel.panels;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.Timer;
import javax.swing.border.BevelBorder;

import org.jxmapviewer.viewer.GeoPosition;

import main.java.backend.model.Fermata;
import main.java.backend.model.PredizioneArrivo;
import main.java.backend.realtime.VehiclePositionInfo;
import main.java.frontend.BackendController;
import main.java.frontend.MapPanel;
import main.java.frontend.user.FavouritesDBManager;
import main.java.frontend.user.LoginToMainFrame;
import main.java.frontend.utilities.ListToScrollConverter;
import main.java.frontend.waypoints.StopWaypoint;
import main.java.frontend.waypoints.VehicleWaypoint;
import main.java.frontend.waypoints.WaypointManager;

/**
 * The Class StopFocus creates a panel to focus on a particular stop. It shows
 * the next 5 vehicles passing by it.
 */
public class StopFocus extends JPanel {

	/** The stop object. */
	private Fermata stop;
	
	/** The timer to update the vehicles position on the map. */
	private Timer t;
	
	/** The scroll panel containing the {@link ArrivingVehiclePanel} */
	private JScrollPane scroll;

	/**
	 * Instantiates a new stop focus.
	 *
	 * @param f the f
	 */
	public StopFocus(Fermata f) {
		super();
		stop = f;
//		showOnMap();
//		showVehiclesOnMap();

		setBorder(new BevelBorder(BevelBorder.LOWERED));
		setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
		addLabelsData();
		addScroll();
		setMaximumSize(getPreferredSize());

	}

	/**
	 * Creates the "add to favourites" button.
	 *
	 * @return the favourites button
	 */
	private JButton createFavouriteButton() {
		String s = FavouritesDBManager.isPresent(stop) ? "★" : "☆";
		JButton b = new JButton(s);
		b.setBorderPainted(false);
		b.setFocusPainted(false);
		b.setContentAreaFilled(false);
		b.setSize(new Dimension(10, 10));
		b.addActionListener(e -> {
			if (b.getText().equals("☆")) {
				b.setText("★");
				FavouritesDBManager.addToFavourites(stop);
			} else {
				b.setText("☆");
				FavouritesDBManager.remove(stop);
			}

		});
		return b;
	}

	/**
	 * Add the label with the stop's name and its id and, if logged, the add to
	 * favourites button.
	 */
	private void addLabelsData() {
		JPanel labpane = new JPanel();
		labpane.setLayout(new FlowLayout(FlowLayout.LEFT));
		if (LoginToMainFrame.isLogged()) {
			labpane.add(createFavouriteButton());
		}
		JLabel nameId = new JLabel(stop.getName() + " - " + stop.getStopId(), JLabel.LEFT);
		nameId.setFont(new Font("Serif", Font.PLAIN, 30));
		labpane.add(nameId);

		labpane.setMaximumSize(labpane.getPreferredSize());
		labpane.setAlignmentX(LEFT_ALIGNMENT);
		add(labpane);

	}

	/**
	 * Adds the scroll pane with {@link ArrivingVehiclePanel} for the next vehicles arrivals.
	 */
	private void addScroll() {
		List<PredizioneArrivo> arriving = BackendController.getTTS().prediciArriviPerFermata(stop.getStopId(), 5);

		JScrollPane x = ListToScrollConverter.setContent(ListToScrollConverter.convertList(arriving));
		if (x != null) {
			x.setPreferredSize(new Dimension(500, 500));
			scroll = x;
			this.add(x);
		}

	}

	/**
	 * Shows a {@link StopWaypoint} for the stop on the {@link MapPanel} through
	 * the {@link WaypointManager} class.
	 */
	public void showOnMap() {
		GeoPosition gp = new GeoPosition(stop.getLat(), stop.getLon());
		WaypointManager.getMap().setCenterPosition(gp);

		WaypointManager.paintStop(gp);

	}
	
	/**
	 * Periodically shows a {@link VehicleWaypoint} for the arriving vehicles on the {@link MapPanel} 
	 * through the {@link WaypointManager} class.
	 */
	public void showVehiclesOnMap() {
		
		
		addVehicleWaypoints();
		
		t = new Timer(50000, e -> {StopFocus.this.addVehicleWaypoints();});
		t.start();
	}
	
	/**
	 * Adds the vehicle waypoints to a set and paints them.
	 */
	private void addVehicleWaypoints() {
		Set<PredizioneArrivo> waypoints = new HashSet<>();
		List<PredizioneArrivo> arriving = BackendController.getTTS().prediciArriviPerFermata(stop.getStopId(), 5);
		
		for (PredizioneArrivo vehicle : arriving) {
			Optional<VehiclePositionInfo> position = BackendController.getTTS().getVehiclePositionForTripId(vehicle.getTripId());
			if (position.isPresent()) {
				waypoints.add(vehicle); }
			
		}
		WaypointManager.paintVehicles(waypoints);
	}

	/**
	 *  Stops the current timer for waypoints and
	 *  the timer for arriving times of {@link ArrivingVehiclePanel}s.
	 */
	public void stopTimers() {
		t.stop();
		if (scroll != null) {
		for (int i= 0; i < scroll.getComponentCount(); i++) {
			if (scroll.getComponent(i) instanceof ArrivingVehiclePanel) {
				ArrivingVehiclePanel x = (ArrivingVehiclePanel) scroll.getComponent(i);
				x.stopTimer();
			}
		}
		}
	}
}