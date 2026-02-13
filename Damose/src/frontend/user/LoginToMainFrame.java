package frontend.user;

import frontend.main.MainFrame;
import frontend.main.RightPanelToMainFrameController;

/**
 * The Class LoginToMainFrame is used to switch to the login panel or to the register panel 
 * through the main frame's card layout. It keeps in memory the login panel and register panel 
 * instances.
 * 
 */
public class LoginToMainFrame {

	/** The login panel. */
	private static LoginPanel logpane;
	
	/** The register panel. */
	private static RegisterPanel regpane;
	
	/** The boolean logged info. */
	private static boolean logged;

	/** The current user (could be null if no user is logged in). */
	private static String user;

	/**
	 * Opens login from a mainframe.
	 *
	 * @param f the mainframe
	 */
	public static void openLogin(MainFrame f) {
		if (logpane == null) {
			LoginPanel l = new LoginPanel();
			logpane = l;
			f.getFrame().getContentPane().add(logpane, "Login Panel");

		}

		logpane.setObserver(f);
		f.getCardLayout().show(f.getFrame().getContentPane(), "Login Panel");

		f.getFrame().repaint();
		f.getFrame().revalidate();
	}

	/**
	 * Opens registering panel from a main frame.
	 *
	 * @param f the mainframe
	 */
	public static void openRegisteringPanel(MainFrame f) {
		if (regpane == null) {
			RegisterPanel r = new RegisterPanel();
			regpane = r;
			f.getFrame().getContentPane().add(regpane, "Register Panel");
		}

		regpane.setObserver(f);
		f.getCardLayout().show(f.getFrame().getContentPane(), "Register Panel");

		f.getFrame().repaint();
		f.getFrame().revalidate();
	}

	
	
	/**
	 * Updates the {@link MainFrame} with the informations
	 * gathered by the {@link LoginPanel}.
	 *
	 * @param log if the user has logged
	 * @param u the potential username
	 */
	public static void updateFromLogin(boolean log, String u) {
		logged = log;
		user = u;
		RightPanelToMainFrameController.createRightPanel();
	}
	
	/**
	 * Checks if the user is logged.
	 *
	 * @return true, if user is logged
	 */
	public static boolean isLogged() {
		return logged;
	}
	
	/**
	 * Gets the current user logged.
	 *
	 * @return the current user
	 */
	public static String getCurrentUser() {
		return user;
	}
}
