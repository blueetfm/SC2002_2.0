/**
 * Represents a record detailing the outcome of a medical appointment.
 * This class encapsulates information about the appointment's outcome, 
 * including the appointment ID, date, service provided, prescribed medicine, 
 * prescription status, and additional notes.
 * @author SCSCGroup4
 * @version 1.0
 * @since 2024-11-21
 */
package Models;
import Enums.*;
import java.time.*;

public class AppointmentOutcomeRecord {
	/** The unique identifier of the appointment. */
    private String appointmentID;

    /** The date of the appointment. */
    private LocalDate date;

    /** The type of service provided during the appointment. */
    private Service service;

    /** The prescribed medicine during the appointment, if any. */
    private String medicine;

    /** The prescription status associated with the appointment. */
    private PrescriptionStatus status;

    /** Additional notes about the appointment outcome. */
    private String notes;

    /**
     * Constructs a new {@code AppointmentOutcomeRecord}.
     *
     * @param appointmentID the unique identifier of the appointment
     * @param date the date of the appointment
     * @param service the service provided during the appointment
     * @param medicine the medicine prescribed during the appointment
     * @param status the prescription status
     * @param notes additional notes about the appointment outcome
     */
	public AppointmentOutcomeRecord(String appointmentID, LocalDate date, Enums.Service service, String medicine, PrescriptionStatus status, String notes) {
		this.appointmentID = appointmentID;
		this.date = date;
		this.service = service;
		this.medicine = medicine;
		this.status = status;
		this.notes = notes;
	}
	/**
     * Retrieves the appointment ID.
     *
     * @return the unique identifier of the appointment
     */
	public String getAppointmentID() {
		return this.appointmentID;
	}
	 /**
     * Retrieves the date of the appointment.
     *
     * @return the date of the appointment
     */
	public LocalDate getDate() {
		return this.date;
	}
	/**
     * Retrieves the service provided during the appointment.
     *
     * @return the service provided
     */
	public Service getService() {
		return this.service;
	}
	/**
     * Retrieves the prescribed medicine.
     *
     * @return the prescribed medicine, or {@code null} if none was prescribed
     */
	public String getMedicine() {
		return this.medicine;
	}
	/**
     * Retrieves the prescription status of the appointment.
     *
     * @return the prescription status
     */
	public PrescriptionStatus getStatus() {
		return this.status;
	}
	/**
     * Retrieves additional notes about the appointment.
     *
     * @return the notes associated with the appointment outcome
     */
	public String getNotes() {
		return this.notes;
	}
	/**
     * Updates the appointment ID.
     *
     * @param appointmentID the new appointment ID
     * @return the updated appointment ID
     */
	public String setAppointmentID(String appointmentID) {
		this.appointmentID = appointmentID;
		return this.appointmentID;
	}
	/**
     * Updates the date of the appointment.
     *
     * @param date the new date
     * @return the updated date
     */
	public LocalDate setDate(LocalDate date) {
		this.date = date;
		return this.date;
	}
	/**
     * Updates the service provided during the appointment.
     *
     * @param service the new service
     * @return the updated service
     */
	public Service setService(Enums.Service service) {
		this.service = service;
		return this.service;
	}
	/**
     * Updates the prescribed medicine.
     *
     * @param medicine the new prescribed medicine
     * @return the updated prescribed medicine
     */
	public String setMedicine(String medicine) {
		this.medicine = medicine;
		return this.medicine;
	}
	/**
     * Updates the prescription status.
     *
     * @param status the new prescription status
     * @return the updated prescription status
     */
	public PrescriptionStatus setStatus(PrescriptionStatus status) {
		this.status = status;
		return this.status;
	}
	/**
     * Updates the notes associated with the appointment.
     *
     * @param notes the new notes
     * @return the updated notes
     */
	public String setNotes(String notes) {
		this.notes = notes;
		return this.notes;
	}
}
