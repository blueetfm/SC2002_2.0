/**
 * Represents a patient in the hospital system. A patient can view and update personal information,
 * manage appointments, and access medical records.
 * This class extends {@code User}.
 * @author SCSCGroup4
 * @version 1.0
 * @since 2024-11-21
 */
package Models;

import Enums.AppointmentStatus;
import Enums.Service;
import Services.AppointmentInterface;
import Services.MedicalRecordInterface;
import Services.TimeSlotInterface;
import java.time.*;
import java.util.*;

/**
 * The {@code Patient} class models a patient in the hospital system.
 * It includes functionality for managing personal information, scheduling
 * appointments, viewing medical records, and interacting with other system
 * components like appointment and medical record management.
 */
public class Patient extends User {

    /**
     * Name of the patient.
     */
    private String name;

    /**
     * Birth date of the patient.
     */
    private LocalDate birthDate;

    /**
     * Gender of the patient.
     */
    private String gender;

    /**
     * Phone number of the patient.
     */
    private String phoneNum;

    /**
     * Email address of the patient.
     */
    private String email;

    /**
     * Unique patient ID.
     */
    private String patientID;

    /**
     * Blood type of the patient.
     */
    private String bloodType;

    /**
     * Medical record manager for handling the patient's medical records.
     */
    private MedicalRecordManager medicalRecordManager = new MedicalRecordManager();

    /**
     * Interface for managing time slots.
     */
    private TimeSlotInterface timeSlotManager;

    /**
     * Manages appointments for the patient.
     */
    // private AppointmentManager appointmentManager;

    /**
     * Constructs a new {@code Patient} object with the specified details.
     *
     * @param hospitalID  The hospital ID of the patient.
     * @param password    The password for the patient's account.
     * @param role        The role of the patient (e.g., "Patient").
     * @param name        The name of the patient.
     * @param birthDate   The birth date of the patient.
     * @param gender      The gender of the patient.
     * @param phoneNum    The phone number of the patient.
     * @param email       The email address of the patient.
     * @param bloodType   The blood type of the patient.
     */
    public Patient(String hospitalID, 
                   String password, String role, String name, 
                   LocalDate birthDate, String gender, String phoneNum, String email,
                   String bloodType){
        super(hospitalID, password, role);
        this.name = name;
        this.birthDate = birthDate;
        this.gender = gender;
        this.phoneNum = phoneNum;
        this.email = email;
        this.patientID = hospitalID;
        this.bloodType = bloodType;
    }

    /**
     * Constructs a new {@code Patient} object with basic details (without password).
     *
     * @param hospitalID  The hospital ID of the patient.
     * @param name        The name of the patient.
     * @param birthDate   The birth date of the patient.
     * @param gender      The gender of the patient.
     * @param phoneNum    The phone number of the patient.
     * @param email       The email address of the patient.
     * @param bloodType   The blood type of the patient.
     */
    public Patient(String hospitalID, String name, 
                   LocalDate birthDate, String gender, String phoneNum, String email,
                   String bloodType){
        super(hospitalID,"", name);
        this.name = name;
        this.birthDate = birthDate;
        this.gender = gender;
        this.phoneNum = phoneNum;
        this.email = email;
        this.patientID = hospitalID;
        this.bloodType = bloodType;
    }

    /**
     * Views the medical record for this patient.
     *
     * @return {@code true} if the medical record is successfully viewed, otherwise {@code false}.
     */
    public boolean viewMedicalRecord(){
		MedicalRecordManager medical = new MedicalRecordManager();
		MedicalRecordInterface.readMedicalRecordsByPatientID(this.patientID);
		return true;
    }

    /**
     * Views available appointment slots for the patient.
     *
     * @return {@code true} if the appointment slots are successfully viewed, otherwise {@code false}.
     */
    public List<TimeSlot> viewAvailableAppointmentSlots() {
        TimeSlotInterface.initializeObjects();
        return TimeSlotInterface.getAvailableTimeSlots();
    }


    /**
     * Schedules an appointment for the patient.
     *
     * @param doctorID    The ID of the doctor.
     * @param timeslotID  The ID of the time slot.
     * @return {@code true} if the appointment is successfully scheduled, otherwise {@code false}.
     */
    public boolean scheduleAppointment(String doctorID, String timeSlotID, Service service) {
        try {
            AppointmentManager.scheduleAppointment(this.patientID, doctorID, timeSlotID, service);
            return true;
        } catch (Exception e) {
            System.err.println("Error scheduling appointment: " + e.getMessage());
            return false;
        }
    }

    /**
     * Reschedules an existing appointment for the patient.
     *
     * @param oldAppointmentID The ID of the current appointment.
     * @param newTimeSlotID    The ID of the new time slot.
     * @return {@code true} if the appointment is successfully rescheduled, otherwise {@code false}.
     */
    public boolean rescheduleAppointment(String oldAppointmentID, String newTimeSlotID) {
        try {
            AppointmentInterface.initializeObjects();
            TimeSlotInterface.initializeObjects();
            
            // Cancel old appointment
            AppointmentInterface.updateAppointmentStatus(oldAppointmentID, AppointmentStatus.CANCELLED);
            
            // Get old appointment details to create new one
            Appointment oldApt = AppointmentInterface.getAppointmentsByPatientID(this.patientID)
                .stream()
                .filter(apt -> apt.getAppointmentID().equals(oldAppointmentID))
                .findFirst()
                .orElse(null);
                
            if (oldApt == null) {
                return false;
            }
            
            // Schedule new appointment
            return scheduleAppointment(oldApt.getDoctorID(), newTimeSlotID, Service.CONSULTATION);
        } catch (Exception e) {
            System.err.println("Error rescheduling appointment: " + e.getMessage());
            return false;
        }
    }

    /**
     * Cancels an existing appointment for the patient.
     *
     * @return {@code true} if the appointment is successfully canceled, otherwise {@code false}.
     */
    public boolean cancelAppointment(String appointmentID) {
        try {
            AppointmentInterface.initializeObjects();
            AppointmentInterface.updateAppointmentStatus(appointmentID, AppointmentStatus.CANCELLED);
            return true;
        } catch (Exception e) {
            System.err.println("Error cancelling appointment: " + e.getMessage());
            return false;
        }
    }

    /**
     * Views the scheduled appointments for this patient.
     *
     * @return A list of {@code Appointment} objects representing the patient's scheduled appointments.
     */
    public List<Appointment> getScheduledAppointments() {
        AppointmentInterface.initializeObjects();
        return AppointmentInterface.getAppointmentsByPatientID(this.patientID);
    }


    /**
     * Views the outcome records of the patient's appointments.
     *
     * @return A list of {@code AppointmentOutcomeRecord} objects representing the outcome of the patient's appointments.
     */
    // public List<AppointmentOutcomeRecord> viewAppointmentOutcomeRecords() {
    //     AppointmentInterface.initializeObjects();
    //     List<AppointmentOutcomeRecord> appointment_outcomes = AppointmentManager.getAppointmentOutcomeRecordsByPatientID(this.patientID);
    //     return appointment_outcomes;
    // }

    /**
     * Logs out the patient from the system.
     *
     * @return {@code true} if logout is successful, otherwise {@code false}.
     */
    public boolean logout() {
        try {
            System.out.println("Logging out patient: " + this.name);
            return true;
        } catch (Exception e) {
            System.err.println("Error during logout: " + e.getMessage());
            return false;
        }
    }

    // Setters and Getters for personal information

    /**
     * Sets the phone number for this patient.
     *
     * @param phoneNum The new phone number.
     */
    public void setPhoneNum(String phoneNum){
        this.phoneNum = phoneNum;
        System.out.println("Phone number changed successfully\n");
    }

    /**
     * Sets the email address for this patient.
     *
     * @param email The new email address.
     */
    public void setEmail(String email){
        this.email = email;
        System.out.println("Email changed successfully\n");
    }

    // Admin setters for modifying patient details

    /**
     * Sets the patient ID for this patient.
     *
     * @param patientID The new patient ID.
     */
    public void setPatientID(String patientID) {
        this.patientID = patientID;
        System.out.println("Patient ID changed successfully\n");
    }

    /**
     * Sets the name for this patient.
     *
     * @param name The new name.
     */
    public void setName(String name) {
        this.name = name;
        System.out.println("Name changed successfully\n");
    }

    /**
     * Sets the birth date for this patient.
     *
     * @param birthDate The new birth date.
     */
    public void setDateOfBirth(LocalDate birthDate) {
        this.birthDate = birthDate;
        System.out.println("Date of birth changed successfully\n");
    }

    /**
     * Sets the gender for this patient.
     *
     * @param gender The new gender.
     */
    public void setGender(String gender) {
        this.gender = gender;
        System.out.println("Gender changed successfully\n");
    }

    /**
     * Sets the blood type for this patient.
     *
     * @param bloodType The new blood type.
     */
    public void setBloodType(String bloodType) {
        this.bloodType = bloodType;
        System.out.println("Blood type changed successfully\n");
    }

    // Getters for retrieving patient details

    /**
     * Gets the patient ID for this patient.
     *
     * @return The patient ID.
     */
    public String getPatientID() {
        return this.patientID;
    }

    /**
     * Gets the phone number for this patient.
     *
     * @return The phone number.
     */
    public String getPhoneNum(){
        return this.phoneNum;
    }

    /**
     * Gets the email address for this patient.
     *
     * @return The email address.
     */
    public String getEmail(){
        return this.email;
    }

    /**
     * Gets the name for this patient.
     *
     * @return The name.
     */
    public String getName() {
        return this.name;
    }

    /**
     * Gets the birth date for this patient.
     *
     * @return The birth date.
     */
    public LocalDate getDateOfBirth() {
        return this.birthDate;
    }

    /**
     * Gets the gender for this patient.
     *
     * @return The gender.
     */
    public String getGender() {
        return this.gender;
    }

    /**
     * Gets the blood type for this patient.
     *
     * @return The blood type.
     */
    public String getBloodType() {
        return this.bloodType;
    }
}