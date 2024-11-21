package Models;

import Enums.*;
import java.time.*;

// Appointment details should include:
// - Patient ID
// - Doctor ID
// - Appointment status (e.g., confirmed, canceled, completed).
// - Date and time of appointment


public class Appointment {
    protected String appointmentID;
    protected String patientID; 
    protected String doctorID;
    protected LocalDate date;
    protected LocalTime time;
    protected String timeSlotID;
    protected AppointmentStatus status;

    public Appointment(String appointmentID, String patientID, String doctorID, LocalDate date, String timeSlotID, AppointmentStatus status){
        this.appointmentID = appointmentID;
        this.patientID = patientID;
        this.doctorID = doctorID;
        this.date = date;
        this.timeSlotID = timeSlotID;
        this.status = status;

    }

    // setters
    public void setStatus(Enums.AppointmentStatus status){
        this.status = status;
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

    public String getTimeSlotID(){
        return this.timeSlotID;
    }

    public AppointmentStatus getStatus(){
        return this.status;
    }
}
