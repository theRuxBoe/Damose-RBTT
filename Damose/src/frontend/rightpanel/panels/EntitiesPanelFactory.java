package frontend.rightpanel.panels;

import javax.swing.JPanel;

import backend.model.DatoGTF;
import backend.model.Fermata;
import backend.model.Linea;
import backend.model.PredizioneArrivo;
import backend.service.TransitServiceImpl.WrapperGenerico;

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
		} else if (e instanceof Linea) {
			p = new LinePanel((Linea) e);
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
			p = new LinePanel((Linea) wg.getItem());
		}

		return p;
	}
}
