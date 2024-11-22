/**
 * The {@code TimeSlotManager} class manages the lifecycle of time slots in a scheduling system. 
 * It provides functionalities to create, retrieve, and update time slots, 
 * as well as saving and loading data from a CSV file.
 * 
 * <p>This class works with the {@code TimeSlot} model and interacts with the CSVHandler utility 
 * for data persistence. It implements the {@code TimeSlotInterface} interface.
 * 
 * <p>Key features:
 * <ul>
 *     <li>Initialize time slots from a CSV file</li>
 *     <li>Save time slots to a CSV file</li>
 *     <li>Filter time slots by doctor ID and availability</li>
 *     <li>Create new time slots</li>
 *     <li>Update time slot statuses</li>
 * </ul>
 * 
 * <p>Dependencies:
 * <ul>
 *     <li>{@code Enums.ScheduleStatus}</li>
 *     <li>{@code Services.TimeSlotInterface}</li>
 *     <li>{@code Utils.CSVHandler}</li>
 *     <li>{@code java.time.LocalDateTime}</li>
 *     <li>{@code java.util.List}</li>
 *     <li>{@code java.util.stream.Collectors}</li>
 * </ul>
 * 
 * @see Models.TimeSlot
 * @see Enums.ScheduleStatus
 * @see Utils.CSVHandler
 * @see Services.TimeSlotInterface
 */
package Models;
import Enums.*;
import Services.TimeSlotInterface;
import Utils.CSVHandler;
import java.time.*;
import java.util.*;
import java.util.stream.Collectors;

public class TimeSlotManager implements TimeSlotInterface{
    /** List of all {@code TimeSlot} objects managed by this class. */
    private static List<TimeSlot> timeSlots = new ArrayList<>();

    /** Path to the CSV file storing time slot data. */
    private static final String CSV_PATH = "data/TimeSlots.csv";

    /**
     * Initializes the list of {@code TimeSlot} objects by reading data from the CSV file.
     * Existing time slots in the list will be cleared before loading new data.
     */
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
    /**
     * Saves the current list of {@code TimeSlot} objects to the CSV file.
     * The CSV file will include headers and all time slot data.
     */
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
    /**
     * Retrieves a list of {@code TimeSlot} objects associated with a specific doctor ID.
     * 
     * @param doctorID the ID of the doctor
     * @return a list of {@code TimeSlot} objects for the specified doctor
     */
    public static List<TimeSlot> getTimeSlotsByDoctorID(String doctorID) {
        return timeSlots.stream()
            .filter(slot -> slot.getDoctorID().equals(doctorID))
            .collect(Collectors.toList());
    }
    /**
     * Retrieves a list of available {@code TimeSlot} objects.
     * 
     * @return a list of {@code TimeSlot} objects with status {@code AVAILABLE}
     */
    public static List<TimeSlot> getAvailableTimeSlots() {
        return timeSlots.stream()
            .filter(slot -> slot.getStatus() == ScheduleStatus.AVAILABLE)
            .collect(Collectors.toList());
    }
    /**
     * Retrieves a {@code TimeSlot} object by its unique ID.
     * 
     * @param timeSlotID the unique ID of the time slot
     * @return the {@code TimeSlot} object if found, or {@code null} if not found
     */
    public static TimeSlot getTimeSlotByID(String timeSlotID) {
        return timeSlots.stream()
            .filter(slot -> slot.getTimeSlotID().equals(timeSlotID))
            .findFirst()
            .orElse(null);
    }
    /**
     * Creates a new {@code TimeSlot} object and adds it to the list.
     * The time slot is automatically assigned a unique ID and set to {@code AVAILABLE} status.
     * 
     * @param doctorID the ID of the doctor associated with the time slot
     * @param start the start time of the time slot
     * @param end the end time of the time slot
     */
    public static void createTimeSlot(String doctorID, LocalDateTime start, LocalDateTime end) {
        String timeSlotID = "TS" + (timeSlots.size() + 1);
        TimeSlot newSlot = new TimeSlot(timeSlotID, start, end, doctorID, null, ScheduleStatus.AVAILABLE);
        timeSlots.add(newSlot);
        saveTimeSlots();
    }
    /**
     * Updates the status of an existing {@code TimeSlot} object.
     * 
     * @param timeSlotID the unique ID of the time slot to update
     * @param status the new {@code ScheduleStatus} of the time slot
     */
    public static void updateTimeSlotStatus(String timeSlotID, ScheduleStatus status) {
        TimeSlot slot = getTimeSlotByID(timeSlotID);
        if (slot != null) {
            slot.setStatus(status);
            saveTimeSlots();
        }
    }
}