package backend.model;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

public class ServiceCalendar extends DatoGTF {
	
	private String serviceId;
	private Set<LocalDate> activeDates = new HashSet<LocalDate>(); //insieme contenente le date attive
	
	public ServiceCalendar(String serviceId) {
        this.serviceId = serviceId;
    }
	
	//aggiunge una data in cui il servizio è attivo
	public void addDate(LocalDate date) {
        this.activeDates.add(date);
    }
	
	public boolean isActiveOn(LocalDate date) {
        return activeDates.contains(date);
    }

    public String getServiceId() {
        return serviceId;
    }
	
}
