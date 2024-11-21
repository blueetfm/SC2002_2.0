package Models;
import java.util.*;

import Enums.*;

import java.time.*;

public class AppointmentOutcomeRecord {
    protected String appointmentID;
    protected String patientID;
    protected LocalDate date;
    protected Service service;
    protected String medication;
    protected PrescriptionStatus prescriptionStatus;
    protected String notes;

    public AppointmentOutcomeRecord(String appointmentID, String patientID, LocalDate date, Service service, String medication, PrescriptionStatus prescriptionStatus, String notes){
        this.appointmentID = appointmentID;
        this.patientID = patientID;
        this.date = date;
        this.service = service;
        this.medication = medication;
        this.prescriptionStatus = prescriptionStatus;
        this.notes = notes;
    }

    // setters
    public void setPrescriptionStatus(PrescriptionStatus prescriptionStatus) {
        this.prescriptionStatus = prescriptionStatus;
    }
    
    public void setNotes(String notes) {
        this.notes = notes;
    }

    // getters
    public String getAppointmentID(){
        return this.appointmentID;
    }

    public String getPatientID(){
        return this.patientID;
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
