package Models;
import Enums.*;
import Services.TimeSlotInterface;
import Utils.CSVHandler;
import java.time.*;
import java.util.*;
import java.util.stream.Collectors;

public class TimeSlotManager implements TimeSlotInterface{
    private static List<TimeSlot> timeSlots = new ArrayList<>();
    private static final String CSV_PATH = "data/TimeSlots.csv";
    
    public static void initializeObjects() {
        List<List<String>> lines = CSVHandler.readCSVLines(CSV_PATH);
        timeSlots.clear();
        
        for (int i = 1; i < lines.size(); i++) {
            List<String> row = lines.get(i);
            TimeSlot slot = new TimeSlot(
                row.get(0), // timeSlotID
                LocalDateTime.parse(row.get(1)),
                LocalDateTime.parse(row.get(2)),
                row.get(3), // doctorID
                row.get(4), // patientID
                ScheduleStatus.valueOf(row.get(5))
            );
            timeSlots.add(slot);
        }
    }

    public static void saveTimeSlots() {
        String[] headers = {"TimeSlot ID", "Start Time", "End Time", "Doctor ID", "Patient ID", "Status"};
        List<String> lines = new ArrayList<>();
        
        for (TimeSlot slot : timeSlots) {
            String line = String.format("%s,%s,%s,%s,%s,%s",
                slot.getTimeSlotID(),
                slot.getStartTime(),
                slot.getEndTime(),
                slot.getDoctorID(),
                slot.getPatientID(),
                slot.getStatus()
            );
            lines.add(line);
        }
        
        CSVHandler.writeCSVLines(headers, lines.toArray(new String[0]), CSV_PATH);
    }

    public static List<TimeSlot> getTimeSlotsByDoctorID(String doctorID) {
        return timeSlots.stream()
            .filter(slot -> slot.getDoctorID().equals(doctorID))
            .collect(Collectors.toList());
    }

    public static List<TimeSlot> getAvailableTimeSlots() {
        return timeSlots.stream()
            .filter(slot -> slot.getStatus() == ScheduleStatus.AVAILABLE)
            .collect(Collectors.toList());
    }

    public static TimeSlot getTimeSlotByID(String timeSlotID) {
        return timeSlots.stream()
            .filter(slot -> slot.getTimeSlotID().equals(timeSlotID))
            .findFirst()
            .orElse(null);
    }

    public static void createTimeSlot(String doctorID, LocalDateTime start, LocalDateTime end) {
        String timeSlotID = "TS" + (timeSlots.size() + 1);
        TimeSlot newSlot = new TimeSlot(timeSlotID, start, end, doctorID, null, ScheduleStatus.AVAILABLE);
        timeSlots.add(newSlot);
        saveTimeSlots();
    }

    public static void updateTimeSlotStatus(String timeSlotID, ScheduleStatus status) {
        TimeSlot slot = getTimeSlotByID(timeSlotID);
        if (slot != null) {
            slot.setStatus(status);
            saveTimeSlots();
        }
    }
}