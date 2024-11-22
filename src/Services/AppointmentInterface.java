package Services;
import Enums.AppointmentStatus;
import Enums.Service;
import Models.Appointment;
import Models.AppointmentManager;
import java.util.*;

public interface AppointmentInterface {
    
    public static void initializeObjects() {
        AppointmentManager.initializeObjects();
    }

    public static void saveAppointments() {
        AppointmentManager.saveAppointments();
    }

    public static void scheduleAppointment(String patientID, String doctorID, String timeSlotID, Service service) {
        AppointmentManager.scheduleAppointment(patientID, doctorID, timeSlotID, service);
    }

    public static void markAppointmentCompleted(String appointmentID) {
        AppointmentManager.markAppointmentCompleted(appointmentID);
    }

    public static void updateAppointmentStatus(String appointmentID, AppointmentStatus status) {
        AppointmentManager.updateAppointmentStatus(appointmentID, status);
    }

    public static List<Appointment> getAllAppointments() {
        return AppointmentManager.getAllAppointments();
    }

    public static List<Appointment> getAppointmentsByDoctorID(String doctorID) {
        return AppointmentManager.getAppointmentsByDoctorID(doctorID);
    }

    public static List<Appointment> getAppointmentsByPatientID(String patientID) {
        return AppointmentManager.getAppointmentsByPatientID(patientID);
    }

    // public abstract void recordAppointmentOutcomeRecord(appointmentID, patientID, date, service, medication, PrescriptionStatus.valueOf("PENDING"), notes);
}