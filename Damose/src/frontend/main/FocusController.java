package frontend.main;

import javax.swing.JPanel;

import frontend.focus.FocusPanel;

public class FocusController { // probabilmente posso rimuovere questa classe

	private static RightPanel rxPane;
	
	public static void openFocus(JPanel p) {
		rxPane.openFocusPanel(p);
		
	}
	
	public static void setRightPanel(RightPanel r) {
		if ( rxPane != null) {
			rxPane = null;
		}
		rxPane = r;
	}
	
	public static void openPrevious() {
		rxPane.setShowCurrent(rxPane.getFocus().getPrevious());
	}
}
