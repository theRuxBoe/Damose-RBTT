package frontend.rightpanel.panels;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.MouseListener;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.JScrollPane;
import javax.swing.border.BevelBorder;

import org.jxmapviewer.viewer.GeoPosition;
import org.jxmapviewer.viewer.WaypointPainter;

import backend.model.Fermata;
import backend.model.PredizioneArrivo;
import frontend.main.MainFrame;
import frontend.utilities.ListToScrollConverter;
import frontend.waypoints.BusWaypoint;
import frontend.waypoints.WaypointManager;


public class BusStopFocus extends BusStopPanel  {

	private Fermata stop;
	
	public BusStopFocus(Fermata bs) {
		super(bs);
		stop = bs;
		setBorder(new BevelBorder(BevelBorder.LOWERED));
		setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
		showOnMap();
		addScroll();
		removeListener();
		
		
		
	}
	
	
	private void removeListener() {
		MouseListener[] x = this.getMouseListeners();
		for (MouseListener m : x) {
			this.removeMouseListener(m);
		}
	}
	
	
	private void addScroll() {
		List<PredizioneArrivo> arriving = MainFrame.getTTS().prediciArriviPerFermata(stop.getStopId(), 5);
		
		
		JScrollPane x = ListToScrollConverter.setContent(ListToScrollConverter.convertList2(arriving));
		x.setPreferredSize(new Dimension(500,500));
		this.add(x);
	}

	private void showOnMap() {
		GeoPosition gp = new GeoPosition(stop.getLat(), stop.getLon());
		WaypointManager.getMap().setCenterPosition(gp);
		
		
		WaypointManager.paintBusStop(gp, new WaypointPainter<BusWaypoint>());
		
		
	}
	

}