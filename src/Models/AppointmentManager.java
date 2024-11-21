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

public class AppointmentManager implements AppointmentInterface{
    private static List<Appointment> appointments = new ArrayList<>();
    private static final String CSV_PATH = "data/Appointments.csv";
    
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
            if (row.size() >= 7) {  // Updated size check
                Appointment appointment = new Appointment(
                    row.get(0), // appointmentID
                    row.get(1), // patientID
                    row.get(2), // doctorID
                    LocalDate.parse(row.get(3)),
                    row.get(4), // timeSlotID
                    Service.valueOf(row.get(6)), //service
                    AppointmentStatus.valueOf(row.get(5))
                );
                appointments.add(appointment);
            }
        }
    }

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

    public static List<Appointment> getAllAppointments() {
        initializeObjects();  // Make sure data is loaded
        return new ArrayList<>(appointments);  // Return a copy of all appointments
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
