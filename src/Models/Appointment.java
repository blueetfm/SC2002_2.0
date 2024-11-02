package Models;

import java.util.*;
import java.time.*;

// Appointment details should include:
// - Patient ID
// - Doctor ID
// - Appointment status (e.g., confirmed, canceled, completed).
// - Date and time of appointment
// - Appointment Outcome Record (for completed appointments) 

enum Status {
    AVAILABLE, CONFIRMED, CANCELED, COMPLETED;
}

public class Appointment {
    protected String patientID; 
    protected String doctorID;
    protected LocalDate date;
    protected LocalTime time;
    protected Status status;

    public Appointment(String patientID, String doctorID, LocalDate date, LocalTime time){
        this.patientID = patientID;
        this.doctorID = doctorID;
        this.date = date;
        this.time = time;
        this.status = Status.AVAILABLE;
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
