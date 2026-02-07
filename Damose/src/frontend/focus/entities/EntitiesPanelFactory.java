package frontend.focus.entities;

import javax.swing.JPanel;

import backend.model.Corsa;
import backend.model.DatoGTF;
import backend.model.Fermata;
//import backendNOTPUSH.Bus;
//import backendNOTPUSH.BusStop;
//import backendNOTPUSH.Entity;
//import backendNOTPUSH.Line;
import backend.model.Linea;
import backend.model.PredizioneArrivo;
import backend.model.Risultato;
import backend.model.RisultatoFermata;
import backend.model.RisultatoLinea;
import backend.service.TransitServiceImpl.WrapperGenerico;

public class EntitiesPanelFactory {

	
	public JPanel createPanel(DatoGTF e) {
		JPanel p = null;
		if (e instanceof Fermata) {
			p = new BusStopPanel((Fermata) e);
		}
		else if (e instanceof Linea) {
			p = new LinePanel((Linea) e);
		}
		else if (e instanceof PredizioneArrivo) {
			p = new BusPanel((PredizioneArrivo) e);
		}
		
		return p;
		
	}
	
	public JPanel createPanel(Risultato e) {
		JPanel p = null;
		if (e instanceof RisultatoFermata) {
			p = new BusStopPanel((RisultatoFermata) e);
		}
		else if (e instanceof RisultatoLinea) {
			p = new LinePanel((RisultatoLinea) e);
		}
		
		
		return p;
		
	}
	
	public JPanel createPanel(WrapperGenerico wg) {
		JPanel p = null;
		if (wg.getType() == "Fermata") {
			p = new BusStopPanel((Fermata) wg.getItem());
		}
		else if (wg.getType() == "Linea") {
			p = new LinePanel((Linea) wg.getItem());
		}
		
		
		return p;
	}
}
