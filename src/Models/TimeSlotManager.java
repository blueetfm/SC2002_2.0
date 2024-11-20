package Models;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;

import enums.ScheduleStatus;

public interface TimeSlotManager {
	public int addTimeSlot(String staffID, String patientID, String name, LocalDate date, LocalTime time, ScheduleStatus scheduleStatus);
	
	public int deleteTimeSlot(String name);
	
	public int editTimeSlot(String staffID, String patientID, String name, LocalDate date, LocalTime time, ScheduleStatus scheduleStatus);
	
	public ArrayList<TimeSlot> getTimeSlotByStaffID(String ID);
	
	public ArrayList<TimeSlot> getTimeSlotByPatientID(String ID);
}
