package Models;
import java.util.*;
import java.time.*;

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
    protected String appointmentID;
    protected String hospitalID;
    protected LocalDate date;
    protected Service service;
    protected String medication;
    protected PrescriptionStatus prescriptionStatus;
    protected String notes;

    // don't construct with the medication
    public AppointmentOutcomeRecord(String appointmentID, String hospitalID, LocalDate date, Service service, String medication, PrescriptionStatus prescriptionStatus, String notes){
        this.appointmentID = appointmentID;
        this.hospitalID = hospitalID;
        this.date = date;
        this.service = service;
        this.medication = medication;
        this.prescriptionStatus = prescriptionStatus;
        this.notes = notes;
    }

    // setters
    // For pharmacists to update the status of prescription (e.g., pending to dispensed).
    public void setPrescriptionStatus(PrescriptionStatus prescriptionStatus) {
        this.prescriptionStatus = prescriptionStatus;
    }
    
    public void setNotes(String notes) {
        this.notes = notes;
    }

    // print details of AOR --> Single Responsibility Principle
    public void printDetails() {
        System.out.println("Date of Appointment: " + date.toString());
        System.out.println("Service provided: " + service);
        System.out.println("Prescribed Medication: " + medication);
    }

    // getters
    public String getAppointmentID(){
        return this.appointmentID;
    }

    public String getHospitalID(){
        return this.hospitalID;
    }

    public LocalDate getDate(){
        return this.date;
    }

    public Service getService(){
        return this.service;
    }

    public String getMedication(){
        return this.medication;
    }

    public PrescriptionStatus getPrescriptionStatus(){
        return this.prescriptionStatus;
    }

    public String getNotes(){
        return this.notes;
    }
    


}
