package Models;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import Enums.ScheduleStatus;

public interface TimeSlotManager {
	public int addTimeSlot(String staffID, String patientID, String name, LocalDate date, LocalTime time, ScheduleStatus scheduleStatus);
	
	public int deleteTimeSlot(String name);
	
	public int editTimeSlot(String staffID, String patientID, String name, LocalDate date, LocalTime time, ScheduleStatus scheduleStatus);
	
	public List<TimeSlot> getTimeSlotByStaffID(String ID);
	
	public List<TimeSlot> getTimeSlotByPatientID(String ID);
}
