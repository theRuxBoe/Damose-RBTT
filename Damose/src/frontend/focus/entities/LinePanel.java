package frontend.focus.entities;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;
import javax.swing.border.BevelBorder;

import org.jxmapviewer.JXMapViewer;
import org.jxmapviewer.viewer.GeoPosition;
import org.jxmapviewer.viewer.Waypoint;
import org.jxmapviewer.viewer.WaypointPainter;

import backendNOTPUSH.BusStop;
import backendNOTPUSH.Line;
import frontend.*;
import frontend.focus.SearchFocusPanel;

public class LinePanel extends ScrollablePanel { 
	
	private ArrayList<BusStop> stops;
	private int id;
	private String direction;
	
	
	public LinePanel(Line l) {
		super();
		setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
		setBorder(new BevelBorder(BevelBorder.LOWERED));
		this.id = l.getId();
		this.direction = l.getDirection();
		this.stops = l.getStops();
		
		addLabelsData();
		addScrollPanel();
	
	}
//	questo listener dovrà mostrare sulla mappa tutte le fermate della linea come una route di jxmapviewer
	private void addListener() {
		addMouseListener(new MouseListener() {
			@Override
			public void mouseReleased(MouseEvent e) {}
			@Override
			public void mousePressed(MouseEvent e) {}
			@Override
			public void mouseExited(MouseEvent e) {}
			@Override
			public void mouseEntered(MouseEvent e) {}
			@Override
			public void mouseClicked(MouseEvent e) {}
		});
	}
	
	private void addLabelsData() {
		JLabel data = new JLabel(id + " - " + direction);
		data.setFont(new Font("Serif", Font.BOLD, 30));
		add(data);
	}
	
	private void addScrollPanel() {
		JScrollPane x = this.setContent(convertList(stops));
		this.add(x, BorderLayout.EAST);
		
	}



}
