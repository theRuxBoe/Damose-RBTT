package main.java.backend.predict;

import java.time.LocalTime;

/**
 * The Class GTFSTime -> a utility class for the PredictionEngine class
 */
public class GTFSTime {
	
	/** The time. */
	private LocalTime time;
	
	/** The day offset -> if the day offset is greater than zero, it means the time refers to a day after the current day. 
	 * The day offset indicates how many days from the current day. */
	private int dayOffset;
	
	/**
	 * Instantiates a new GTFS time.
	 *
	 * @param t the t
	 * @param offset the offset
	 */
	public GTFSTime(LocalTime t, int offset) {
		
		this.time = t;
		this.dayOffset = offset;
	}
	
	/**
	 * Parses the GTFS time given input -> this is necessary because the file with the static data on the stop times contains times longer than 24 hours. 
	 * This method takes care of converting those times (for example, 25:00 corresponds to 1:00 am of the next day).
	 *
	 * @param orario the orario
	 * @return the GTFS time
	 */
	public static GTFSTime parseGTFSTime(String orario) {
		
		String[] parts = orario.split(":");
		int hh = Integer.parseInt(parts[0]);
		int mm = Integer.parseInt(parts[1]);
		int ss = Integer.parseInt(parts[2]);
		int dayOff = hh / 24;
		LocalTime time = LocalTime.of(hh%24, mm, ss);
		
		return new GTFSTime(time, dayOff);
	}

	/**
	 * Gets the time.
	 *
	 * @return the time
	 */
	public LocalTime getTime() {
		return time;
	}

	/**
	 * Gets the day offset.
	 *
	 * @return the day offset
	 */
	public int getDayOffset() {
		return dayOffset;
	}

}
