package Models;

import java.time.LocalDate;
import java.time.LocalTime;

import Enums.*;

public class AppointmentManager {
    private static AppointmentManager instance;
    private AppointmentList appointmentList;

<<<<<<< HEAD
    private AppointmentManager(String appointmentFilePath, String outcomeFilePath) {
        this.appointmentList = AppointmentList.getInstance(appointmentFilePath);
        this.outcomeRecordList = AppointmentOutcomeRecordList.getInstance(outcomeFilePath);
=======
    private AppointmentManager() {
        this.appointmentList = AppointmentList.getInstance();
>>>>>>> 5154b130b112b524550b10d15cd356fe540613bf
    }

    public static synchronized AppointmentManager getInstance(String appointmentFilePath, String outcomeFilePath) {
        if (instance == null) {
            instance = new AppointmentManager(appointmentFilePath, outcomeFilePath);
        }
        return instance;
    }

    // CRUD
    public void createAppointment(String appointmentID, String patientID, String doctorID, LocalDate date, LocalTime timeSlot) {
        appointmentList.createAppointment(appointmentID, patientID, doctorID, date, timeSlot);
    }

<<<<<<< HEAD
=======
    public void createAppointmentOutcome(String appointmentID, String hospitalID, LocalDate date, Service service, String medication, PrescriptionStatus prescriptionStatus, String notes) {
        Appointment appointment = getAppointmentById(appointmentID);
        if (appointment != null && appointment.getStatus() == Status.COMPLETED) {
            // outcomeRecordList.createAppointmentOutcomeRecord(appointmentID, hospitalID, date, service, medication, prescriptionStatus, notes);
        } else {
            System.out.println("Appointment not found or not completed. Cannot create outcome record.");
        }
    }

    public Appointment getAppointmentById(String appointmentID) {
        for (Appointment appointment : appointmentList.appointmentList) {
            if (appointment.getAppointmentID().equals(appointmentID)) {
                return appointment;
            }
        }
        return null;
    }

>>>>>>> 5154b130b112b524550b10d15cd356fe540613bf
    public void viewAppointment(String appointmentID) {
        appointmentList.readAppointment(appointmentID);
    }

    public void updateAppointment(String appointmentID) {
        appointmentList.updateAppointment(appointmentID);
    }

    public void deleteAppointment(String appointmentID) {
        appointmentList.deleteAppointment(appointmentID);
    }

    public void createAppointmentOutcome(String appointmentID, String hospitalID, LocalDate date, Service service, String medication, PrescriptionStatus prescriptionStatus, String notes) {
        Appointment appointment = appointmentList.getAppointmentById(appointmentID);
        if (appointment != null && appointment.getStatus() == Status.COMPLETED) {
            outcomeRecordList.createAppointmentOutcomeRecord(appointmentID, hospitalID, date, service, medication, prescriptionStatus, notes);
        } else {
            System.out.println("Appointment not found or not completed. Cannot create outcome record.");
        }
    }

    public void viewAppointmentOutcome(String appointmentID) {
<<<<<<< HEAD
        outcomeRecordList.viewAppointmentOutcomeRecords();
=======
        // outcomeRecordList.readAppointmentOutcomeRecord(appointmentID);
>>>>>>> 5154b130b112b524550b10d15cd356fe540613bf
    }

    public void updateAppointmentOutcome(String appointmentID) {
        // outcomeRecordList.updateAppointmentOutcomeRecord(appointmentID);
    }

    public void deleteAppointmentOutcome(String appointmentID) {
        // outcomeRecordList.deleteAppointmentOutcomeRecord(appointmentID);
    }
}
