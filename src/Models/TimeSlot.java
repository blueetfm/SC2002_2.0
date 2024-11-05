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
	
	public String getStaffID() {
		return this.staffID;
	}
	
	public String getName() {
		return this.name;
	}
	
	public LocalDate getDate() {
		return this.date;
	}
	
	public LocalTime getTime() {
		return this.time;
	}
	
	public ScheduleStatus getScheduleStatus() {
		return this.scheduleStatus;
	}
	
	public String setStaffID(TimeSlot timeSlot, String staffID) {
		timeSlot.staffID = staffID;
		return timeSlot.staffID;
	}
	
	public String setName(TimeSlot timeSlot, String name) {
		timeSlot.name = name;
		return timeSlot.name;
	}
	
	public LocalDate setDate(TimeSlot timeSlot, LocalDate date) {
		timeSlot.date = date;
		return timeSlot.date;
	}
	
	public LocalTime setTime(TimeSlot timeSlot, LocalTime time) {
		timeSlot.time = time;
		return timeSlot.time;
	}
	
	public ScheduleStatus setScheduleStatus(TimeSlot timeSlot, ScheduleStatus scheduleStatus) {
		timeSlot.scheduleStatus = scheduleStatus;
		return timeSlot.scheduleStatus;
	}

}
