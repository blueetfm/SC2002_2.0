package Models;

import java.util.*;

import Enums.*;

import java.time.*;

// Appointment details should include:
// - Patient ID
// - Doctor ID
// - Appointment status (e.g., confirmed, canceled, completed).
// - Date and time of appointment
// - Appointment Outcome Record (for completed appointments) 


public class Appointment {
    protected String appointmentID;
    protected String patientID; 
    protected String doctorID;
    protected LocalDate date;
    protected LocalTime timeSlot;
    protected Status status;
    protected AppointmentOutcomeRecord outcomeRecord; 

    public Appointment(String appointmentID, String patientID, String doctorID, LocalDate date, LocalTime timeSlot, Status status, AppointmentOutcomeRecord outcomeRecord){
        this.appointmentID = appointmentID;
        this.patientID = patientID;
        this.doctorID = doctorID;
        this.date = date;
        this.timeSlot = timeSlot;
        this.status = Status.AVAILABLE;
        this.outcomeRecord = null;
    }

    // setters
    public void setStatus(Enums.Status status){
        this.status = status;
    }
    public void setAppointmentOutcomeRecord(AppointmentOutcomeRecord outcomeRecord){
        this.outcomeRecord = outcomeRecord;
        this.status = Status.COMPLETED;
    }

    public void printDetails() {
        System.out.println("Date of Appointment: " + date.toString());

    }

    // getters
    public String getAppointmentID(){
        return this.appointmentID;
    }

    public String getPatientID(){
        return this.patientID;
    }

    public String getDoctorID(){
        return this.doctorID;
    }

    public LocalDate getDate(){
        return this.date;
    }

    public LocalTime getTimeSlot(){
        return this.timeSlot;
    }

    public Status getStatus(){
        return this.status;
    }

    public AppointmentOutcomeRecord getOutcomeRecord(){
        return this.outcomeRecord;
    }


    // public boolean isAvailable(){
    //     return availability;
    // }

    // public void scheduleAppointment(){
    //     if (this.isAvailable()) {
    //         this.availability = false;
    //         return;
    //     } else {

    //     }
    // }


    
}
