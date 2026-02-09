package frontend.news;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;

import backend.realtime.ServiceAlertInfo;

public class NewsPanel extends JPanel{
	
	
	public NewsPanel(ServiceAlertInfo n) {
		super();
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		addLabels(n);
	}
	
	private void addLabels(ServiceAlertInfo saf) {
		add(new JLabel(saf.getHeader()));
		add(new JLabel(saf.getDescription()));
		add(new JLabel(saf.getRouteIds().toString()));
		add(new JLabel(saf.getStartTime() + " - " + saf.getEndTime()));
		add(new JLabel(saf.getStopIds().toString()));
	}
	
	
}
