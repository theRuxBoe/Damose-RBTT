package main.java.frontend.rightpanel;

import main.java.frontend.main.MainFrame;
import main.java.frontend.rightpanel.panels.StopFocus;

/**
 * The Class FocusController connects the {@link RightPanel}
 * with the {@link FocusPanel} so the focus panel doesn't need to
 * know the right panel, it just needs to remember the previous panel
 * displayed.
 */
public class FocusController {

	/** The {@link RightPanel}. */
 private static RightPanel rxPane;
	
	/**
	 * Opens the {@link FocusPanel} with the given {@link StopFocus}.
	 *
	 * @param stopfocus the stop focus to be displayed
	 */
	public static void openFocus(StopFocus stopfocus) {
		rxPane.openFocusPanel(stopfocus);
		
	}
	
	/**
	 * Registers the current right panel instanced by the
	 * {@link MainFrame}.
	 *
	 * @param r the right panel
	 */
	public static void setRightPanel(RightPanel r) {
		if ( rxPane != null) {
			rxPane = null;
		}
		rxPane = r;
	}
	
	/**
	 * Opens the previous panel from the {@link FocusPanel}.
	 */
	public static void openPrevious() {
		rxPane.setAndShowCurrent(rxPane.getFocus().getPrevious());
	}
}
