package backend.manualtest;

import java.util.List;
import java.util.Optional;

import backend.model.PredizioneArrivo;
import backend.realtime.VehiclePositionInfo;
import backend.service.TransitServiceImpl;

/**
 * The Class RealtimeManualTest.
 */
public class RealtimeManualTest {
	
	/**
	 * The main method.
	 *
	 * @param args the arguments
	 * @throws Exception the exception
	 */
	public static void main(String[] args) throws Exception {
		
		TransitServiceImpl service = TransitServiceImpl.createDefault();
		
		String[] stops = {"70218"};
		
		for (String f : stops) {
			
			List<PredizioneArrivo> predizioni = service.prediciArriviPerFermata(f, 10);
			
			System.out.println("Predizioni (" + predizioni.size() + "):");
            for (PredizioneArrivo p : predizioni) {
                System.out.println("  " + p.toString() + " tripId=" + p.getTripId() + " dir=" + p.getDirectionName());
                if ((p.getTripId()!=null)&&(!p.getTripId().isBlank())) {
                	
                	Optional<VehiclePositionInfo> vpi = service.getVehiclePositionForTripId(p.getTripId());
                	if (vpi.isPresent()) {
                        VehiclePositionInfo v = vpi.get();
                        System.out.println("  Vehicle: id=" + v.getVehicleId() + " label=" + v.getVehicleLabel() + " lat=" + v.getLat() + " lon=" + v.getLon() + " ts=" + v.getTimestamp());
                    } else {
                        System.out.println("  Nessuna posizione disponibile per quel trip (null).");
                    }
                }
            }
		}		
	}
}
