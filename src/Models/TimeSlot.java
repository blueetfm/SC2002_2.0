package Models;

import java.time.*;

import Enums.*;

public class TimeSlot {
	protected String timeSlotID;
	protected String doctorID;
	protected String patientID;
	protected LocalDate date;
	protected LocalTime time;
	protected ScheduleStatus scheduleStatus;
	
	public TimeSlot(String timeSlotID, String doctorID, String patientID, LocalDate date, LocalTime time, ScheduleStatus scheduleStatus) {
		this.timeSlotID = timeSlotID;
		this.doctorID = doctorID;
		this.patientID = patientID;
		this.date = date;
		this.time = time;
		this.scheduleStatus = scheduleStatus;
	}

	public String getTimeSlotID() {
		return this.timeSlotID;
	}
	
	public String getDoctorID() {
		return this.doctorID;
	}
	
	public String getPatientID() {
		return this.patientID;
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

	public void setTimeSlotID(String timeSlotID) {
		this.timeSlotID = timeSlotID;
		return;
	}
	
	public void setDoctorID(String doctorID) {
		this.doctorID = doctorID;
		return;
	}
	
	public void setPatientID(String doctorID) {
		this.patientID = patientID;
		return;
	}
	
	public void setDate(LocalDate date) {
		this.date = date;
		return;
	}
	
	public void setTime(LocalTime time) {
		this.time = time;
		return;
	}
	
	public void setScheduleStatus(ScheduleStatus scheduleStatus) {
		this.scheduleStatus = scheduleStatus;
		return;
	}

}
