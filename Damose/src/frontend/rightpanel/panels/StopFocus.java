package frontend.rightpanel.panels;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.BevelBorder;

import org.jxmapviewer.viewer.GeoPosition;
import org.jxmapviewer.viewer.WaypointPainter;

import backend.model.Fermata;
import backend.model.PredizioneArrivo;
import frontend.main.BackendController;
import frontend.main.MainFrame;
import frontend.main.MapPanel;
import frontend.user.FavouritesDBManager;
import frontend.user.LoginToMainFrame;
import frontend.utilities.ListToScrollConverter;
import frontend.waypoints.GenericWaypoint;
import frontend.waypoints.WaypointManager;

/**
 * The Class StopFocus creates a panel to focus on a particular stop. It shows
 * the next 5 vehicles passing by it.
 */
public class StopFocus extends JPanel {

	/** The stop object. */
	private Fermata stop;

	/**
	 * Instantiates a new stop focus.
	 *
	 * @param bs the stop to focus
	 */
	public StopFocus(Fermata f) {
		super();
		stop = f;
		showOnMap();

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
			if (b.getText() == "☆") {
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
			this.add(x);
		}

	}

	/**
	 * Shows a {@link GenericWaypoint} for the stop on the {@link MapPanel} through
	 * the {@link WaypointManager} class.
	 */
	private void showOnMap() {
		GeoPosition gp = new GeoPosition(stop.getLat(), stop.getLon());
		WaypointManager.getMap().setCenterPosition(gp);

		WaypointManager.paintStop(gp, new WaypointPainter<GenericWaypoint>());

	}

}