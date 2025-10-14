package frontend.focus;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

public class LinePanel extends JPanel implements Focusable {
	
	private List<BusStop> stops;
	private int id;
	private String direction;
	
	
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
}
