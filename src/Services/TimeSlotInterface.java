package Services;
import Enums.*;
import Models.TimeSlot;
import Models.TimeSlotManager;
import java.time.*;
import java.util.*;

public interface TimeSlotInterface {
    public static void initializeObjects() {
		TimeSlotManager.initializeObjects();
    }

    public static void saveTimeSlots() {
        TimeSlotManager.saveTimeSlots();
    }

    public static List<TimeSlot> getTimeSlotsByDoctorID(String doctorID) {
        return TimeSlotManager.getTimeSlotsByDoctorID(doctorID);
    }

    public static List<TimeSlot> getAvailableTimeSlots() {
        return TimeSlotManager.getAvailableTimeSlots();
    }

    public static TimeSlot getTimeSlotByID(String timeSlotID) {
        return TimeSlotManager.getTimeSlotByID(timeSlotID);
    }

    public static void createTimeSlot(String doctorID, LocalDateTime start, LocalDateTime end) {
        TimeSlotManager.createTimeSlot(doctorID, start, end);
    }

    public static void updateTimeSlotStatus(String timeSlotID, ScheduleStatus status) {
        TimeSlotManager.updateTimeSlotStatus(timeSlotID, status);
    }
}