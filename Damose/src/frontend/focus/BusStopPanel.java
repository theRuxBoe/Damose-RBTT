package frontend.focus;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;

import org.jxmapviewer.viewer.GeoPosition;

import frontend.MainFrame;

public class BusStopPanel extends JPanel implements Focusable{

//	we need to wait until we know how the information is passed from the back-end
	private List<Bus> arrivingBuses;
	private int id;
	private GeoPosition position;
	
	
	public BusStopPanel(BusStop bs) {
		this.id = bs.getId();
		this.position = bs.getPosition();
		
	}
	
	private JPanel createStopPanel() {
		JPanel panel = new JPanel();
		panel.setLayout(null);
		panel.add(new JLabel("Stop id :" + id));
//		MainFrame.getMap().add(new StopWaypoint(position));		we could show on the map the stop
		JScrollPane scrollpanel = new JScrollPane();
		scrollpanel.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		scrollpanel.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		for (Bus bus : arrivingBuses) {
			scrollpanel.add(new BusPanel(bus));
			scrollpanel.add(new JLabel("Arriving time :" + bus.getTime(position))); //unimplemented method
		}
		panel.add(scrollpanel);
		return panel;
	}
	
//	a bus stop panel will display the buses with their arrival times
//	public void getArrivingBuses() {}

@Override
	public JPanel createPanel() {
		this.createStopPanel();
	
	}
	
	
	
//	
}
