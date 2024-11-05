package Models;

import java.time.*;
import enums.*;

public class TimeSlot {
	String staffID;
	String name;
	LocalDate date;
	LocalTime time;
	ScheduleStatus scheduleStatus;
	

	public TimeSlot(String staffID, String name, LocalDate date, LocalTime time, ScheduleStatus scheduleStatus) {
		this.staffID = staffID;
		this.name = name;
		this.date = date;
		this.time = time;
		this.scheduleStatus = scheduleStatus;
	}

}
