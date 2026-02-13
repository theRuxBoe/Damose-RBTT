package frontend.rightpanel;

import java.awt.BorderLayout;

import javax.swing.JButton;
import javax.swing.JPanel;

import frontend.rightpanel.panels.StopFocus;

/**
 * The Class FocusPanel shows the selected stop from
 * the search panel.
 * It also keeps a link to the panel that
 * called it, so when the back button is
 * pressed it doesn't trash the search result.
 * 
 */
public class FocusPanel extends JPanel {
	
	/** The previous opened panel. */
	private JPanel previous;
	
	
	/** The current {@link StopFocus} displayed. */
	private StopFocus current;
	
	/**
	 * Instantiates a new focus panel.
	 */
	public FocusPanel() {
		super();
		setLayout(new BorderLayout());
		addBackButton();
	}
	
	/**
	 * Sets the current displayed {@link StopFocus} panel.
	 *
	 * @param p the {@link StopFocus} panel
	 */
	public void setFocus(StopFocus p) {
		if (current != null) {
			remove(current);
		}
		current = p;
		add(p, BorderLayout.CENTER);
		
	}
	
	/**
	 * Adds the back button to return to the previous panel.
	 */
	private void addBackButton() {
		JPanel p = new JPanel();
		JButton b = new JButton("↩");
		b.setSize(getPreferredSize());
		b.addActionListener(e -> { FocusController.openPrevious(); });
				
		
		p.add(b);
		
		add(p,BorderLayout.NORTH);
	}

	
	
	/**
	 * Sets the previous {@link JPanel}.
	 *
	 * @param p the new previous panel
	 */
	public void setPrevious(JPanel p) {
		previous = p;
	}
	
	/**
	 * Gets the previous panel
	 *
	 * @return the previous panel
	 */
	public JPanel getPrevious() {
		return previous;
	}
	
}
