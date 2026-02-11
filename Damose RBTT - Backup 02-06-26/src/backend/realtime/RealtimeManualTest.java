package backend.realtime;

import java.util.List;
import java.util.Optional;

import backend.model.PredizioneArrivo;
import backend.service.TransitServiceImpl;

public class RealtimeManualTest {
	
	public static void main(String[] args) throws Exception {
		
		TransitServiceImpl service = TransitServiceImpl.createDefault();
		
		String[] stops = {"80610"};
		
		for (String f : stops) {
			
			List<PredizioneArrivo> predizioni = service.prediciArriviPerFermata(f, 5);
			
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
