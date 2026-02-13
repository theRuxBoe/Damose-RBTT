package main.java.frontend.news;

import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JTextArea;

import main.java.backend.realtime.ServiceAlertInfo;

/**
 * The Class NewsPanel creates a visualization panel of a Service Alert Info
 */
public class AlertPanel extends JPanel{
	
	
	/**
	 * Creates the alert panel from a given ServiceAlertInfo
	 *
	 * @param alert the alert
	 */
	public AlertPanel(ServiceAlertInfo alert) {
		super();
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));	
		addAlertData(alert);
	}
	
	/**
	 * Adds the data inside a J Text Area
	 *
	 * @param alert the service alert containing informations
	 */
	private void addAlertData(ServiceAlertInfo alert) {
		JTextArea t = new JTextArea();
		t.setText(alert.getHeader() + "\n");
		t.append(alert.getDescription() + "\n");
		t.append("Linee affette : " + alert.getRouteIds().toString());
		t.setLineWrap(true);
		t.setWrapStyleWord(true);
		t.setFocusable(false);
		add(t);
		add(new JSeparator());
	}
	
	
}
