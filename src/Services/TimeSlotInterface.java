package Services;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import Enums.ScheduleStatus;
import Models.TimeSlot;

public interface TimeSlotInterface {
	static List<TimeSlot> initializeObjects(){
		return null;
	}
    static int updateCSV(List<TimeSlot> timeSlots){
		return 0;
	}
	static List<TimeSlot> getTimeSlots(){
		return null;
	}
    static TimeSlot getTimeSlotByID(String timeSlotID){
		return null;
	}
    static List<TimeSlot> getTimeSlotsByDoctorID(String doctorID){
		return null;
	}
    static List<TimeSlot> getTimeSlotsByPatientID(String patientID){
		return null;
	}
    static int addTimeSlot(String doctorID, String patientID, LocalDate date, LocalTime time, ScheduleStatus scheduleStatus){
		return 0;
	}
    static int editTimeSlot(String staffID, String patientID, String name, LocalDate date, LocalTime time, ScheduleStatus scheduleStatus){
		return 0;
	}
	static String generateTimeSlotID(){
		return null;
	}

	public static void printTimeSlot(TimeSlot slot) {
	}
}
