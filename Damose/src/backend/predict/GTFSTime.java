package backend.predict;

import java.time.LocalTime;

public class GTFSTime {
	
	private LocalTime time;
	private int dayOffset;
	
	public GTFSTime(LocalTime t, int offset) {
		
		this.time = t;
		this.dayOffset = offset;
	}
	
	public static GTFSTime parseGTFSTime(String orario) {
		
		String[] parts = orario.split(":");
		int hh = Integer.parseInt(parts[0]);
		int mm = Integer.parseInt(parts[1]);
		int ss = Integer.parseInt(parts[2]);
		int dayOff = hh / 24;
		LocalTime time = LocalTime.of(hh%24, mm, ss);
		
		return new GTFSTime(time, dayOff);
	}

	public LocalTime getTime() {
		return time;
	}

	public int getDayOffset() {
		return dayOffset;
	}

}
