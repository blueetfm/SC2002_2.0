package Models;

import java.util.*;

// Date of Appointment
// ● Type of service provided (e.g., consultation, X-ray, blood test etc).
// ● Any prescribed medications:
// - medication name
// - status (default is pending)
// ● Consultation notes 
enum PrescriptionStatus {
    DISPENSED, PENDING;
}

enum Service {
    CONSULTATION, XRAY, BLOODTEST;
}


public class AppointmentOutcomeRecord {
    protected Date date;
    protected Service service;
    protected String medication;
    protected PrescriptionStatus prescriptionStatus;
    protected String notes;

    // don't construct with the medication
    public AppointmentOutcomeRecord(Date date, Service service, String medication, PrescriptionStatus prescriptionStatus, String notes){
        this.date = date;
        this.service = service;
        this.medication = medication;
        this.prescriptionStatus = prescriptionStatus;
        this.notes = notes;
    }

    public void createAppointmentOutcomeRecord(){
        
    }

    public void readAppointmentOutcomeRecord(){
        
    }

    public void updateAppointmentOutcomeRecord(){
        
    }

    public void deleteAppointmentOutcomeRecord(){
        
    }
}
