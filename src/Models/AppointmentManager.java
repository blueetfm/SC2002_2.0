/**
 * The {@code AppointmentManager} class provides management functionalities for appointments in a scheduling system.
 * It offers methods to initialize, save, schedule, update, and retrieve appointments, as well as handle interactions
 * with a CSV data storage and related services such as time slots.
 * 
 * <p>
 * This class implements the {@code AppointmentInterface} and relies on various supporting classes and enums,
 * including {@code Appointment}, {@code TimeSlot}, {@code Service}, and {@code AppointmentStatus}.
 * </p>
 * 
 * Package: {@code Models}
 * Dependencies: {@code Enums}, {@code Services}, {@code Utils}, {@code java.time}, {@code java.util}
 * @author SCSCGroup4
 * @version 1.0
 * @since 2024-11-21
 */
package Models;

import Enums.AppointmentStatus;
import Enums.ScheduleStatus;
import Enums.Service;
import Services.AppointmentInterface;
import Services.TimeSlotInterface;
import Utils.CSVHandler;
import java.time.*;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Manages the lifecycle and data operations of appointments, including creating, updating, and retrieving them.
 */
public class AppointmentManager implements AppointmentInterface {

    /** A list containing all the appointments in the system. */
    private static List<Appointment> appointments = new ArrayList<>();

    /** The file path for the CSV file used to store appointment data. */
    private static final String CSV_PATH = "data/Appointments.csv";

    /**
     * Initializes the appointment objects by reading them from the CSV file.
     * If no data exists, creates an empty CSV file with headers.
     */
    public static void initializeObjects() {
        List<List<String>> lines = CSVHandler.readCSVLines(CSV_PATH);
        appointments.clear();
        
        if (lines == null || lines.isEmpty()) {
            String[] headers = {"Appointment ID", "Patient ID", "Doctor ID", "Date", 
                              "TimeSlot ID", "Status", "Service"};
            CSVHandler.writeCSVLines(headers, new String[]{}, CSV_PATH);
            return;
        }
        
        for (int i = 1; i < lines.size(); i++) {
            List<String> row = lines.get(i);
            if (row.size() >= 7) {
                Appointment appointment = new Appointment(
                    row.get(0), // appointmentID
                    row.get(1), // patientID
                    row.get(2), // doctorID
                    LocalDate.parse(row.get(3)), //date
                    row.get(4), // timeSlotID
                    Service.valueOf(row.get(6)), //service
                    AppointmentStatus.valueOf(row.get(5)) //status
                );
                appointments.add(appointment);
            }
        }
    }
    /**
     * Saves the current list of appointments to the CSV file.
     */
    public static void saveAppointments() {
        String[] headers = {"Appointment ID", "Patient ID", "Doctor ID", "Date", 
                           "TimeSlot ID", "Status", "Service"};
        List<String> lines = new ArrayList<>();
        
        for (Appointment apt : appointments) {
            String line = String.format("%s,%s,%s,%s,%s,%s,%s",
                apt.getAppointmentID(),
                apt.getPatientID(),
                apt.getDoctorID(),
                apt.getDate(),
                apt.getTimeSlotID(),
                apt.getStatus(),
                apt.getService()
            );
            lines.add(line);
        }
        
        CSVHandler.writeCSVLines(headers, lines.toArray(new String[0]), CSV_PATH);
    }
    /**
     * Schedules a new appointment by creating it and associating it with a specified time slot.
     * 
     * @param patientID  The ID of the patient for the appointment.
     * @param doctorID   The ID of the doctor for the appointment.
     * @param timeSlotID The ID of the time slot reserved for the appointment.
     * @param service    The service type for the appointment.
     */
    public static void scheduleAppointment(String patientID, String doctorID, 
                                         String timeSlotID, Service service) {
        TimeSlot slot = TimeSlotManager.getTimeSlotByID(timeSlotID);
        if (slot != null && slot.getStatus() == ScheduleStatus.AVAILABLE) {
            String appointmentID = "APT" + (appointments.size() + 1);
            Appointment newAppointment = new Appointment(
                appointmentID,
                patientID,
                doctorID,
                slot.getStartTime().toLocalDate(),
                timeSlotID,
                service,
                AppointmentStatus.PENDING
            );
            appointments.add(newAppointment);
            slot.setPatientID(patientID);
            slot.setStatus(ScheduleStatus.PENDING);
            saveAppointments();
            TimeSlotManager.saveTimeSlots();
        }
    }
    /**
     * Updates the status of an existing appointment.
     * 
     * @param appointmentID The ID of the appointment to update.
     * @param status        The new status to set for the appointment.
     */
    public static void updateAppointmentStatus(String appointmentID, AppointmentStatus status) {
        Appointment appointment = appointments.stream()
            .filter(apt -> apt.getAppointmentID().equals(appointmentID))
            .findFirst()
            .orElse(null);
            
        if (appointment != null) {
            appointment.setStatus(status);
            TimeSlot slot = TimeSlotInterface.getTimeSlotByID(appointment.getTimeSlotID());
            if (slot != null) {
                slot.setStatus(status == AppointmentStatus.CONFIRMED ? ScheduleStatus.RESERVED : ScheduleStatus.AVAILABLE);
                TimeSlotInterface.saveTimeSlots();
            }
            saveAppointments();
        }
    }
    /**
     * Marks an appointment as completed and updates the associated time slot status.
     * 
     * @param appointmentID The ID of the appointment to mark as completed.
     */
    public static void markAppointmentCompleted(String appointmentID) {
        Appointment appointment = appointments.stream()
            .filter(apt -> apt.getAppointmentID().equals(appointmentID))
            .findFirst()
            .orElse(null);
                
        if (appointment != null) {
            appointment.setStatus(AppointmentStatus.COMPLETED);
            TimeSlot slot = TimeSlotInterface.getTimeSlotByID(appointment.getTimeSlotID());
            if (slot != null) {
                slot.setStatus(ScheduleStatus.COMPLETED);
                TimeSlotInterface.saveTimeSlots();
            }
            saveAppointments();
        }
    }
    /**
     * Retrieves a list of all appointments.
     * 
     * @return A list of all appointments.
     */
    public static List<Appointment> getAllAppointments() {
        initializeObjects();
        return new ArrayList<>(appointments);
    }
    /**
     * Retrieves appointments associated with a specific doctor.
     * 
     * @param doctorID The ID of the doctor.
     * @return A list of appointments for the specified doctor.
     */
    public static List<Appointment> getAppointmentsByDoctorID(String doctorID) {
        return appointments.stream()
            .filter(apt -> apt.getDoctorID().equals(doctorID))
            .collect(Collectors.toList());
    }
    /**
     * Retrieves appointments associated with a specific patient.
     * 
     * @param patientID The ID of the patient.
     * @return A list of appointments for the specified patient.
     */
    public static List<Appointment> getAppointmentsByPatientID(String patientID) {
        return appointments.stream()
            .filter(apt -> apt.getPatientID().equals(patientID))
            .collect(Collectors.toList());
    }
}
