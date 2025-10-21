package frontend.focus;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import backendDONTPUSH.*;

public class LinePanel extends JPanel { // implements Focusable, Favouritable 
	
	private List<BusStop> stops;
	private int id;
	private String direction;
	
	
	public LinePanel(Line l) {
		super(new GridLayout(1, 3));
		setPreferredSize(new Dimension(300,400));
		
		this.id = l.getId();
		this.direction = l.getDirection();
		this.stops = l.getStops();
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
//	public JPanel createPanel() {
//		JPanel a = this.createLinePanel();
//		return a;
//	}

//	@Override
//	public void setFavourite() {
////		Favourites.getPreferiti().add(this);  //TODO same as busstoppanel
//	}
}
