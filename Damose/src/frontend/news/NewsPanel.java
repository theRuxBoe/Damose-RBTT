package frontend.news;

import java.awt.Font;

import javax.swing.BoxLayout;
import javax.swing.JEditorPane;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JTextArea;
import javax.swing.JTextPane;

import backend.realtime.ServiceAlertInfo;

public class NewsPanel extends JPanel{
	
	
	public NewsPanel(ServiceAlertInfo n) {
		super();
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));	
		addLabels(n);
//		setMaximumSize(getPreferredSize());
	}
	
	private void addLabels(ServiceAlertInfo saf) {
		JTextArea t = new JTextArea();
		t.setText(saf.getHeader() + "\n");
		t.append(saf.getDescription() + "\n");
		t.append("Linee affette : " + saf.getRouteIds().toString() + "\n");
		t.append(saf.getStartTime().toString() + " - " + saf.getEndTime().toString() + "\n");
//		t.append(saf.getStopIds().toString());
				
//		t.setFont(new Font("Serif", Font.ITALIC, 16));
		t.setLineWrap(true);
		t.setWrapStyleWord(true);
		
		t.setFocusable(false);
		
//		add(new JLabel(saf.getHeader()));
//		add(new JLabel(saf.getDescription()));
//		add(new JLabel(saf.getRouteIds().toString()));
//		add(new JLabel(saf.getStartTime() + " - " + saf.getEndTime()));
//		add(new JLabel(saf.getStopIds().toString()));
		add(t);
		add(new JSeparator());
	}
	
	
}
