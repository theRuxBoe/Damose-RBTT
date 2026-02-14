package main.java.frontend;

import java.io.IOException;

import javax.swing.JOptionPane;

import main.java.backend.service.TransitServiceImpl;

/**
 * 
 * The Class BackendController controls the communication 
 * with the back-end by providing a static reference
 * to the {@link TransitServiceImpl}.
 */
public class BackendController {

	/** The {@link TransitServiceImpl}. */
	private static TransitServiceImpl tts;
	
	/**
	 * Opens the {@link TransitServiceImpl}.
	 */
	public static void openTransit() {
		if (tts == null) {
		try {
			tts = TransitServiceImpl.createDefault();
			checkConnection();
		} catch (IOException e) {
			int x = JOptionPane.showConfirmDialog(null, e.getMessage() + "Do you wish to try again?");
			if (x == 0) {
				openTransit();
			}
			
		}
		}
		
	}
	
	/**
	 * Checks the real time connection and notifies the user whether 
	 * they are connected or not.
	 */
	private static void checkConnection() {
		if (!tts.isOnline()) {
			JOptionPane.showMessageDialog(null, "Sei offline!");
		}
	}
	
	/**
	 * Gets the {@link TransitServiceImpl}.
	 *
	 * @return the {@link TransitServiceImpl}
	 */
	public static TransitServiceImpl getTTS() {
		return tts;
	}
}
