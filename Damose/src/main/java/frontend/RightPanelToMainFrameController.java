package main.java.frontend;

import java.awt.BorderLayout;

import main.java.frontend.rightpanel.FocusPanel;
import main.java.frontend.rightpanel.RightPanel;
import main.java.frontend.rightpanel.SearchPanel;
import main.java.frontend.user.LoginToMainFrame;

/**
 * The Class RightPanelToMainFrameController.
 */
public class RightPanelToMainFrameController {

	/** The {@link RightPanel}. */
	private static RightPanel rp;
	
	/** The {@link MainFrame}. */
	private static MainFrame mframe;
	
	
	/**
	 * Creates the right panel.
	 */
	public static void createRightPanel() {
		if (rp != null) {
			mframe.getMainPanel().remove(rp);
		}

		RightPanel rightPanel = new RightPanel();
		rp = rightPanel;
		mframe.getMainPanel().add(rp, BorderLayout.EAST);

		mframe.getCardLayout().show(mframe.getFrame().getContentPane(), "Main Panel");

		if (LoginToMainFrame.isLogged()) {

			rp.openFavouritePanel();
		} else {

			rp.openSearchPanel();
		}
		
		mframe.getFrame().repaint();
		mframe.getFrame().revalidate();

	}
	
	/**
	 * Gets the frame.
	 *
	 * @return the frame
	 */
	public static MainFrame getFrame() {
		return mframe;
	}
	
	/**
	 * Sets the frame.
	 *
	 * @param f the new frame
	 */
	public static void setFrame(MainFrame f) {
		mframe = f;
	}
	
	/**
	 * Switch the current {@link RightPanel}.
	 */
	public static void switchCurrentRightPanel() {

		if (rp.getCurrent() instanceof SearchPanel || rp.getCurrent() instanceof FocusPanel) {
			rp.openFavouritePanel();
		} else {
			rp.openSearchPanel();
		}
		mframe.getFrame().repaint();
		mframe.getFrame().revalidate();

	}
}
