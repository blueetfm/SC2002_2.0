package Models;

import Enums.Service;
import java.time.LocalDate;
import java.util.List;

import Enums.PrescriptionStatus;

public interface AppointmentInterface {
    public static List<Appointment> initializeObjects() {
        return AppointmentManager.initializeObjects();
    }
    public static int updateCSV(List<Appointment> appointments) {
        return AppointmentManager.updateCSV(appointments);
    }
    public static String generateAppointmentID() {
        return AppointmentManager.generateAppointmentID();
    }
    public static List<Appointment> getAppointments() {
        return AppointmentManager.getAppointments();
    }
    public static List<AppointmentOutcomeRecord> getAppointmentOutcomeRecords() {
        return AppointmentManager.getAppointmentOutcomeRecords();
    }
    public static Appointment getAppointmentByID(String appointmentID) {
        return AppointmentManager.getAppointmentByID(appointmentID);
    }
    public static List<Appointment> getAppointmentsByPatientID(String patientID) {
        return AppointmentManager.getAppointmentsByPatientID(patientID);
    }
    public static List<Appointment> getAppointmentsByDoctorID(String doctorID) {
        return AppointmentManager.getAppointmentsByDoctorID(doctorID);
    }
    public static AppointmentOutcomeRecord getAppointmentOutcomeRecordByID(String appointmentID){
        return AppointmentManager.getAppointmentOutcomeRecordByID(appointmentID);
    }

    public static List<AppointmentOutcomeRecord> getAppointmentOutcomeRecordsByPatientID(String patientID){
        return AppointmentManager.getAppointmentOutcomeRecordsByPatientID(patientID);
    }
    public static boolean scheduleAppointment(String patientID, String doctorID, String timeSlotID){
        return AppointmentManager.scheduleAppointment(patientID, doctorID, timeSlotID);
    }

    public static boolean rescheduleAppointment(String oldAppointmentID, String newTimeSlotID){
        return AppointmentManager.rescheduleAppointment(oldAppointmentID, newTimeSlotID);
    }

    // Cancel appointment
    public static int cancelAppointment(String appointmentID){
        return AppointmentManager.cancelAppointment(appointmentID);
    }
    public static int recordAppointmentOutcomeRecord(String appointmentID, String patientID, LocalDate outcomeDate, Enums.Service service, String medication, PrescriptionStatus prescriptionStatus, String notes){
        return AppointmentManager.recordAppointmentOutcomeRecord(appointmentID, patientID, outcomeDate, service, medication, prescriptionStatus, notes);
    }
    
    public static void printAppointment(Appointment appointment) {
    	AppointmentManager.printAppointment(appointment);
    }

    public static void printAppointmentOutcomeRecord(AppointmentOutcomeRecord appointmentOutcomeRecord){
        AppointmentManager.printAppointmentOutcomeRecord(appointmentOutcomeRecord);
    }
}
