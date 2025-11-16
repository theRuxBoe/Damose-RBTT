package frontend.focus.entities;

import frontend.*;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.HashSet;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.BevelBorder;

import org.jxmapviewer.JXMapViewer;
import org.jxmapviewer.viewer.GeoPosition;
import org.jxmapviewer.viewer.Waypoint;
import org.jxmapviewer.viewer.WaypointPainter;

import frontend.MainFrame;
import frontend.Map;
import frontend.focus.SearchFocusPanel;

public class LinePanel extends ScrollablePanel { 
	
	private List<BusStop> stops;
	private int id;
	private String direction;
	private GeoPosition position;
	
	
	public LinePanel(Line l) {
		super(new GridLayout(1, 3));
		setPreferredSize(new Dimension(300,400));
		setBorder(new BevelBorder(BevelBorder.LOWERED));
		this.id = l.getId();
		this.direction = l.getDirection();
		this.stops = l.getStops();
//		this.position = l.getPosition();
		addInfo();
//		addScrollPanel();
//		setContent(Line.getStops());
		
	
	}
	
	private void addListener() {
		addMouseListener(new MouseListener() {
			
			@Override
			public void mouseReleased(MouseEvent e) {
				
			}
			
			@Override
			public void mousePressed(MouseEvent e) {
				
			}
			
			@Override
			public void mouseExited(MouseEvent e) {
				
			}
			
			@Override
			public void mouseEntered(MouseEvent e) {
				
			}
			
			@Override
			public void mouseClicked(MouseEvent e) {
				SearchFocusPanel.getFocus().setLine(LinePanel.this);
			}
		});
	}
	
	private void addInfo() {
		add(new JLabel("Line ID : " + id), 0);
		add(new JLabel("Direction : " + direction), 0);
	}
	

	private void addScrollPanel() {
		JPanel support = new JPanel();
		support.setLayout(new BoxLayout(support, BoxLayout.Y_AXIS));
		for (BusStop stop : this.stops) {
			
			
			support.add(new JLabel(stop.getInfo()));
			
		}
		JScrollPane scrollpanel = new JScrollPane(support);
		scrollpanel.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		scrollpanel.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		
		
		this.add(scrollpanel);
	}



}
