package main.java.frontend.rightpanel.panels;

import javax.swing.JPanel;

import main.java.backend.model.RisultatoLinea;

import main.java.backend.model.DatoGTF;
import main.java.backend.model.Fermata;
import main.java.backend.model.Linea;
import main.java.backend.model.PredizioneArrivo;
import main.java.backend.service.TransitServiceImpl.WrapperGenerico;
import main.java.frontend.BackendController;

/**
 * A factory for creating panel objects for stops, lines and vehicles.
 */
public class EntitiesPanelFactory {

	/**
	 * Creates a new panel object depending
	 * on the input {@link DatoGTF}.
	 *
	 * @param e the GTF data object
	 * @return the panel containing the object's informations
	 */
	public JPanel createPanel(DatoGTF e) {
		JPanel p = null;
		if (e instanceof Fermata) {
			p = new StopPanel((Fermata) e);
		} else if (e instanceof RisultatoLinea) {
			p = new LinePanel((RisultatoLinea) e);
		} else if (e instanceof PredizioneArrivo) {
			p = new ArrivingVehiclePanel((PredizioneArrivo) e);
		}

		return p;

	}


	/**
	 * Creates a new panel object depending
	 * on the input {@link WrapperGenerico}
	 *
	 * @param wg the generic wrapper returned by the database
	 * search
	 * @return the panel created with the generic wrapper
	 */
	public JPanel createPanel(WrapperGenerico wg) {
		JPanel p = null;
		if (wg.getType() == "Fermata") {
			p = new StopPanel((Fermata) wg.getItem());
		} else if (wg.getType() == "Linea") {
			p = new LinePanel((RisultatoLinea) wg.getItem());
		}

		return p;
	}
}
