package Models;

import Enums.Service;
import java.time.LocalDate;
import java.util.List;

import Enums.PrescriptionStatus;

public interface AppointmentInterface {
    public static List<Appointment> initializeObjects() {
        return null;
    }
    public static int updateCSV(List<Appointment> appointments) {
        return 0;
    }
    public static String generateAppointmentID() {
        return null;
    }
    public static List<Appointment> getAppointments() {
        return null;
    }
    public static List<AppointmentOutcomeRecord> getAppointmentOutcomeRecords() {
        return null;
    }
    public static Appointment getAppointmentByID(String appointmentID) {
        return null;
    }
    public static List<Appointment> getAppointmentsByPatientID(String patientID) {
        return null;
    }
    public static List<Appointment> getAppointmentsByDoctorID(String doctorID) {
        return null;
    }
    public static AppointmentOutcomeRecord getAppointmentOutcomeRecordByID(String appointmentID){
        return null;
    }

    public static List<AppointmentOutcomeRecord> getAppointmentOutcomeRecordsByPatientID(String patientID){
        return null;
    }
    public static boolean scheduleAppointment(String patientID, String doctorID, String timeSlotID){
        return false;
    }

    public static boolean rescheduleAppointment(String oldAppointmentID, String newTimeSlotID){
        return false;
    }

    // Cancel appointment
    public static int cancelAppointment(String appointmentID){
        return 0;
    }
    public static int recordAppointmentOutcomeRecord(String appointmentID, String patientID, LocalDate outcomeDate, Enums.Service service, String medication, PrescriptionStatus prescriptionStatus, String notes){
        return 0;
    }
}
