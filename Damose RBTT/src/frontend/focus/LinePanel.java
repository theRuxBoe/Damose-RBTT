package frontend.focus;

<<<<<<< Updated upstream
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

public class LinePanel extends JPanel implements Focusable {
=======
import frontend.*;
import java.awt.Dimension;
import java.awt.GridLayout;
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

import backendDONTPUSH.*;
import frontend.MainFrame;
import frontend.Map;

public class LinePanel extends JPanel { // implements Focusable,
>>>>>>> Stashed changes
	
	private List<BusStop> stops;
	private int id;
	private String direction;
<<<<<<< Updated upstream
	
	
	public LinePanel(Line l) {
		
		this.id = l.getId();
		this.direction = l.getDirection();
		this.stops = l.getStops();
	
	}
	
	private JPanel createLinePanel() {
		JPanel panel = new JPanel();
		panel.setLayout(null);
		panel.add(new JLabel("Line ID :" + id));
		panel.add(new JLabel("Direction :" + direction));
		JScrollPane scrollpanel = new JScrollPane();
		scrollpanel.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		scrollpanel.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		for (BusStop stop : this.stops) {
			scrollpanel.add(new JLabel(stop.getInfo()));
			
		}
		panel.add(scrollpanel);
		return panel;
		
	}

	@Override
	public JPanel createPanel() {
		this.createLinePanel();
		
	}
=======
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
		addScrollPanel();
		
	
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

	
//	@Override
//	public static JPanel createPanel() {
//		JPanel a = this.createLinePanel();
//		return a;
//	}


>>>>>>> Stashed changes
}
