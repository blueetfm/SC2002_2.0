package Services;
import Enums.AppointmentStatus;
import Enums.ScheduleStatus;
import Models.Appointment;
import Models.TimeSlot;
import Utils.CSVHandler;
import java.time.*;
import java.util.*;
import java.util.stream.Collectors;

public class AppointmentInterface {
    private static List<Appointment> appointments = new ArrayList<>();
    private static final String CSV_PATH = "data/Appointments.csv";
    
    public static void initializeObjects() {
        try {
            List<List<String>> lines = CSVHandler.readCSVLines(CSV_PATH);
            appointments.clear();
            
            // If file is empty or only has header, create it
            if (lines == null || lines.isEmpty()) {
                String[] headers = {"Appointment ID", "Patient ID", "Doctor ID", "Date", "TimeSlot ID", "Status"};
                CSVHandler.writeCSVLines(headers, new String[]{}, CSV_PATH);
                return;
            }
            
            // Skip header, process remaining lines
            for (int i = 1; i < lines.size(); i++) {
                List<String> row = lines.get(i);
                if (row.size() >= 6) {  // Ensure row has all required fields
                    Appointment appointment = new Appointment(
                        row.get(0), // appointmentID
                        row.get(1), // patientID
                        row.get(2), // doctorID
                        LocalDate.parse(row.get(3)),
                        row.get(4), // timeSlotID
                        AppointmentStatus.valueOf(row.get(5))
                    );
                    appointments.add(appointment);
                }
            }
        } catch (Exception e) {
            System.err.println("Error initializing appointments: " + e.getMessage());
            // Create empty file with headers if it doesn't exist
            String[] headers = {"Appointment ID", "Patient ID", "Doctor ID", "Date", "TimeSlot ID", "Status"};
            CSVHandler.writeCSVLines(headers, new String[]{}, CSV_PATH);
        }
    }

    public static void saveAppointments() {
        String[] headers = {"Appointment ID", "Patient ID", "Doctor ID", "Date", "TimeSlot ID", "Status"};
        List<String> lines = new ArrayList<>();
        
        for (Appointment apt : appointments) {
            String line = String.format("%s,%s,%s,%s,%s,%s",
                apt.getAppointmentID(),
                apt.getPatientID(),
                apt.getDoctorID(),
                apt.getDate(),
                apt.getTimeSlotID(),
                apt.getStatus()
            );
            lines.add(line);
        }
        
        CSVHandler.writeCSVLines(headers, lines.toArray(new String[0]), CSV_PATH);
    }

    public static void scheduleAppointment(String patientID, String doctorID, String timeSlotID) {
        try {
            TimeSlot slot = TimeSlotInterface.getTimeSlotByID(timeSlotID);
            if (slot != null && slot.getStatus() == ScheduleStatus.AVAILABLE) {
                String appointmentID = "APT" + (appointments.size() + 1);
                Appointment newAppointment = new Appointment(
                    appointmentID,
                    patientID,
                    doctorID,
                    slot.getStartTime().toLocalDate(),
                    timeSlotID,
                    AppointmentStatus.PENDING
                );
                appointments.add(newAppointment);
                slot.setPatientID(patientID);
                slot.setStatus(ScheduleStatus.PENDING);
                TimeSlotInterface.saveTimeSlots();
                saveAppointments();
                System.out.println("Appointment scheduled successfully!");
            }
        } catch (Exception e) {
            System.err.println("Error scheduling appointment: " + e.getMessage());
        }
    }

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

    public static List<Appointment> getAppointmentsByDoctorID(String doctorID) {
        return appointments.stream()
            .filter(apt -> apt.getDoctorID().equals(doctorID))
            .collect(Collectors.toList());
    }

    public static List<Appointment> getAppointmentsByPatientID(String patientID) {
        return appointments.stream()
            .filter(apt -> apt.getPatientID().equals(patientID))
            .collect(Collectors.toList());
    }
}