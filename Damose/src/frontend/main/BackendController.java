package frontend.main;

import java.io.IOException;

import javax.swing.JOptionPane;

import backend.service.TransitServiceImpl;

public class BackendController {

	private static TransitServiceImpl tts;
	
	/**
	 * Opens the {@link TransitServiceImpl}.
	 */
	public static void openTransit() {
		if (tts == null) {
		try {
			tts = TransitServiceImpl.createDefault();
		} catch (IOException e) {
			int x = JOptionPane.showConfirmDialog(null, e.getMessage() + "Do you wish to try again?");
			if (x == 0) {
				openTransit();
			}
		}
		}
		checkConnection();
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
	
	public static TransitServiceImpl getTTS() {
		return tts;
	}
}
