package frontend.focus.entities;

import java.awt.BorderLayout;
import java.awt.Color;
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

import backend.model.Linea;
import backend.model.RisultatoLinea;
import backend.model.Fermata;
import frontend.*;
import frontend.focus.SearchFocusPanel;

public class LinePanel extends ScrollablePanel { 
	
//	private ArrayList<Fermata> stops;
	private String id;
	private String direction;
	private Linea line;
	
	
	public LinePanel(Linea l) {
		super();
//		setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
		setLayout(new BorderLayout());
		setBorder(new BevelBorder(BevelBorder.LOWERED));
		this.line = l;
//		this.id = l.getName();
//		this.direction = l.getDescription();
//		this.stops = l.get
		
		addLabelsData();
		addScrollPanel();
	
	}
	
	public LinePanel(RisultatoLinea rl) {
		super();
		
	}
	
//	questo listener dovrà mostrare sulla mappa tutte le fermate della linea come una route di jxmapviewer
//	SE CIAO
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
		JLabel data = new JLabel("  " + line.getName() + " - " + line.getDescription());
		data.setFont(new Font("Serif", Font.BOLD, 30));
		add(data, BorderLayout.NORTH);
	}
	
	private void addScrollPanel() {
		JScrollPane x = this.setContent(convertList(MainFrame.getTTS().trovaFermatePerLinea(line.getRouteId(), line.getDescription())));
//		x.setPreferredSize(new Dimension(400,10));
		add(x, BorderLayout.EAST);
//		JPanel p = new JPanel();
//		p.setBackground(Color.BLACK);
////		p.setPreferredSize(new Dimension(30,30));
//		
//		add(p, BorderLayout.EAST);
		
	}



}
