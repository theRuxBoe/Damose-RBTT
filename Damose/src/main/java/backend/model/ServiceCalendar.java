package main.java.backend.model;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

/**
 * The Class ServiceCalendar -> identifies an object which contains a service id and the dates where the service is active .
 */
public class ServiceCalendar extends DatoGTF {
	
	/** The service id. */
	private String serviceId;
	
	/** The active dates. */
	private Set<LocalDate> activeDates = new HashSet<LocalDate>();
	
	/**
	 * Instantiates a new service calendar.
	 *
	 * @param serviceId the service id
	 */
	public ServiceCalendar(String serviceId) {
        this.serviceId = serviceId;
    }
	
	/**
	 * Adds a date in which the service is active.
	 *
	 * @param date the date
	 */
	public void addDate(LocalDate date) {
        this.activeDates.add(date);
    }
	
	/**
	 * Checks if the service is active on a certain date.
	 *
	 * @param date the date
	 * @return true, if is active on
	 */
	public boolean isActiveOn(LocalDate date) {
        return activeDates.contains(date);
    }

    /**
     * Gets the service id.
     *
     * @return the service id
     */
    public String getServiceId() {
        return serviceId;
    }
	
}
