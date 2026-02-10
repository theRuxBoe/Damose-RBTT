package frontend.rightpanel.panels;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseListener;
import java.io.IOException;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.BevelBorder;

import org.jxmapviewer.viewer.GeoPosition;
import org.jxmapviewer.viewer.WaypointPainter;

import backend.model.Fermata;
import backend.model.PredizioneArrivo;
import backend.service.TransitServiceImpl;
import frontend.main.MainFrame;
import frontend.user.FavouritesDBManager;
import frontend.utilities.ListToScrollConverter;
import frontend.waypoints.BusWaypoint;
import frontend.waypoints.WaypointManager;


public class BusStopFocus extends JPanel  {

	private Fermata stop;
	
	public BusStopFocus(Fermata bs) {
		super();
		stop = bs;
		showOnMap();
		
		setBorder(new BevelBorder(BevelBorder.LOWERED));
		setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
		addLabelsData();
		addScroll();
		setMaximumSize(getPreferredSize());
		
		
	}
	
	private JButton createFavouriteButton() {
		String s = "☆";
		JButton b = new JButton(s);
		b.setBorderPainted(false);
		b.setFocusPainted(false);
		b.setContentAreaFilled(false);
		b.setSize(new Dimension(10,10));
		b.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				if (b.getText() == "☆") {
					b.setText("★");
					FavouritesDBManager.addToFavourites(stop);
//					this will put the stop into the favourites area
				}
				else {
					b.setText("☆");
//					this will kick the stop from the favourites
				}
			}
		});
		return b; 
	}
	
	private void addLabelsData() {
		JPanel labpane = new JPanel();
		labpane.setLayout(new FlowLayout(FlowLayout.LEFT));
		JLabel data = new JLabel(stop.getName() + " - " + stop.getStopId(), JLabel.LEFT);
		data.setFont(new Font("Serif", Font.PLAIN, 30));
		labpane.add(data);
		if (MainFrame.isLogged()) {
			labpane.add(createFavouriteButton());
		}
		labpane.setMaximumSize(labpane.getPreferredSize());
		labpane.setAlignmentX(LEFT_ALIGNMENT);
		add(labpane);
		
	}
	
	
	private void addScroll() {
		List<PredizioneArrivo> arriving = MainFrame.getTTS().prediciArriviPerFermata(stop.getStopId(), 5);
		
		
		JScrollPane x = ListToScrollConverter.setContent(ListToScrollConverter.convertList(arriving));
		x.setPreferredSize(new Dimension(500,500));
		this.add(x);
	}

	private void showOnMap() {
		GeoPosition gp = new GeoPosition(stop.getLat(), stop.getLon());
		WaypointManager.getMap().setCenterPosition(gp);
		
		
		WaypointManager.paintBusStop(gp, new WaypointPainter<BusWaypoint>());
		
		
	}
	
//	public static void main(String[] args) {
//	
////	try {
////		TransitServiceImpl.createDefault();
////	} catch (IOException e) {
////		// TODO Auto-generated catch block
////		e.printStackTrace();
////	}
//	
//	JFrame f = new JFrame();
//	f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//	f.setLayout(new FlowLayout());
//	f.add(new BusStopFocus(new Fermata("1234", "TarantaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaooooaoaoaoaoOAoaosdos", 41.0, 52.0)));
//	f.setVisible(true);
//	f.pack();
//}
}