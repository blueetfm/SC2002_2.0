/**
 * The {@code Appointment} class represents an appointment in a medical or service-related system.
 * It encapsulates the details of an appointment, including its ID, associated patient and doctor IDs,
 * date, time slot, service type, and status.
 * 
 * <p>
 * The class provides constructors for initializing appointment objects and
 * getter and setter methods to access and modify the appointment's properties.
 * </p>
 * 
 * <p>
 * Example usage:
 * <pre>
 * {@code
 * Appointment appointment = new Appointment(
 *     "A123", 
 *     "P001", 
 *     "D002", 
 *     LocalDate.of(2024, 12, 15), 
 *     "TS001", 
 *     new Service("General Consultation", 100.0), 
 *     AppointmentStatus.SCHEDULED
 * );
 * }
 * </pre>
 * </p>
 * 
 * Package: {@code Models}
 * Dependencies: {@code Enums}, {@code java.time}
 * @author SCSCGroup4
 * @version 1.0
 * @since 2024-11-21
 */
package Models;

import Enums.*;
import java.time.*;

/**
 * Represents an appointment with details such as IDs, date, time slot, service type, and status.
 */
public class Appointment {

    /** Unique identifier for the appointment. */
    protected String appointmentID;

    /** Unique identifier for the patient associated with the appointment. */
    protected String patientID;

    /** Unique identifier for the doctor associated with the appointment. */
    protected String doctorID;

    /** The date of the appointment. */
    protected LocalDate date;

    /** The specific time of the appointment. */
    protected LocalTime time;

    /** Identifier for the time slot reserved for the appointment. */
    protected String timeSlotID;

    /** The service type associated with the appointment. */
    protected Service service;

    /** The current status of the appointment. */
    protected AppointmentStatus status;

    /**
     * Constructs a new {@code Appointment} object with the specified details.
     * 
     * @param appointmentID The unique identifier for the appointment.
     * @param patientID     The unique identifier for the patient.
     * @param doctorID      The unique identifier for the doctor.
     * @param date          The date of the appointment.
     * @param timeSlotID    The identifier for the reserved time slot.
     * @param service       The service type associated with the appointment.
     * @param status        The current status of the appointment.
     */
    public Appointment(String appointmentID, String patientID, String doctorID, LocalDate date, String timeSlotID, Service service, AppointmentStatus status){
        this.appointmentID = appointmentID;
        this.patientID = patientID;
        this.doctorID = doctorID;
        this.date = date;
        this.timeSlotID = timeSlotID;
        this.service = service;
        this.status = status;
    }

    /**
     * Sets the status of the appointment.
     * 
     * @param status The new status to be assigned to the appointment.
     */
    public void setStatus(Enums.AppointmentStatus status){
        this.status = status;
    }
    
    /**
     * Retrieves the unique identifier for the appointment.
     * 
     * @return The appointment ID.
     */
    public String getAppointmentID(){
        return this.appointmentID;
    }
    /**
     * Retrieves the unique identifier for the patient associated with the appointment.
     * 
     * @return The patient ID.
     */
    public String getPatientID(){
        return this.patientID;
    }
    /**
     * Retrieves the unique identifier for the doctor associated with the appointment.
     * 
     * @return The doctor ID.
     */
    public String getDoctorID(){
        return this.doctorID;
    }
    /**
     * Retrieves the date of the appointment.
     * 
     * @return The date of the appointment.
     */
    public LocalDate getDate(){
        return this.date;
    }
    /**
     * Retrieves the identifier for the reserved time slot.
     * 
     * @return The time slot ID.
     */
    public String getTimeSlotID(){
        return this.timeSlotID;
    }
    /**
     * Retrieves the service type associated with the appointment.
     * 
     * @return The service type.
     */
    public Service getService() {
        return this.service;
    }
    /**
     * Retrieves the current status of the appointment.
     * 
     * @return The appointment status.
     */
    public AppointmentStatus getStatus(){
        return this.status;
    }
}
