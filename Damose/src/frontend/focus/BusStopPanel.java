package frontend.focus;

import backendDONTPUSH.*;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneLayout;

import org.jxmapviewer.viewer.GeoPosition;

import frontend.MainFrame;

public class BusStopPanel implements Focusable{

//	we need to wait until we know how the information is passed from the back-end
	private List<Bus> arrivingBuses;
	private int id;
	private GeoPosition position;
	
	
	public BusStopPanel(BusStop bs) {
		this.id = bs.getId();
		this.position = bs.getPosition();
		this.arrivingBuses = bs.getArrivingBuses();
		
	}
	
	private JPanel createStopPanel() {
		JPanel panel = new JPanel();
		JPanel supportPanel = new JPanel();
		panel.setLayout(new GridLayout(1, 3));
		panel.setPreferredSize(new Dimension(500,200));
		panel.add(new JLabel("Stop id : " + id), 0);
		
		supportPanel.setLayout(new BoxLayout(supportPanel, BoxLayout.Y_AXIS));
		
//		MainFrame.getMap().add(new StopWaypoint(position));		we could show on the map the stop
		
		
		
		for (Bus bus : arrivingBuses) {
			BusPanel b = new BusPanel(bus);
			supportPanel.add(b.createPanel());
//			scrollpanel.add(new JLabel("Arriving time :" + bus.getTime(position))); //unimplemented method
		}
		
		JScrollPane scrollpanel = new JScrollPane(supportPanel);
		scrollpanel.setLayout(new ScrollPaneLayout());
		scrollpanel.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		scrollpanel.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		
		panel.add(scrollpanel, 1);
		return panel;
	}
	
//	a bus stop panel will display the buses with their arrival times
//	public void getArrivingBuses() {}

@Override
	public JPanel createPanel() {
		JPanel a = this.createStopPanel();
		return a;
	}
	
	
	
//	
}
