package Models;

import java.time.LocalDate;
import java.time.LocalTime;

import Enums.*;

// bigger manager of AppointmentOutcomeRecordManager?
// acts as the controller that decides when and how to interact with AppointmentOutcomeRecordManager

// BIG QUESTION: DO I NEED A INTERFACE FOR THIS? 
public class AppointmentManager {
    private static AppointmentManager instance;
    private AppointmentList appointmentList;

    private AppointmentManager() {
        this.appointmentList = AppointmentList.getInstance();
    }

    public static AppointmentManager getInstance() {
        if (instance == null) {
            instance = new AppointmentManager();
        }
        return instance;
    }

    public void createAppointment(String appointmentID, String patientID, String doctorID, LocalDate date, LocalTime timeSlot) {
        appointmentList.createAppointment(appointmentID, patientID, doctorID, date, timeSlot);
    }

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

    public void viewAppointment(String appointmentID) {
        appointmentList.readAppointment(appointmentID);
    }

    public void updateAppointment(String appointmentID) {
        appointmentList.updateAppointment(appointmentID);
    }

    public void deleteAppointment(String appointmentID) {
        appointmentList.deleteAppointment(appointmentID);
    }

    public void viewAppointmentOutcome(String appointmentID) {
        // outcomeRecordList.readAppointmentOutcomeRecord(appointmentID);
    }

    public void updateAppointmentOutcome(String appointmentID) {
        // outcomeRecordList.updateAppointmentOutcomeRecord(appointmentID);
    }

    public void deleteAppointmentOutcome(String appointmentID) {
        // outcomeRecordList.deleteAppointmentOutcomeRecord(appointmentID);
    }
}
