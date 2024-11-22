/* The `TimeSlot` class represents a specific time slot in a doctor's schedule. 
 * It contains information about the slot's ID, timing, associated doctor, patient, and its status.
 * 
 * <p>Key Features:</p>
 * - Immutable time slot ID and end time.
 * - Mutable start time, patient ID, and schedule status.
 * - Designed to handle scheduling functionalities in a hospital management system.
 * 
 * <p>Usage:</p>
 * Instances of this class are used to manage appointments, availability, and schedule statuses for doctors and patients.
 * 
 * <p>Attributes:</p>
 * - Each time slot is associated with a specific doctor and optionally a patient.
 * - Time slots have statuses to indicate availability or appointment states.
 * 
 * <p>Dependencies:</p>
 * - `Enums.ScheduleStatus`: Represents the current status of the time slot.
 * - `java.time`: Used for date and time representation.
 * 
 * @see Enums.ScheduleStatus
 * @author SCSCGroup4
 * @version 1.0
 * @since 2024-11-21
 */
package Models;

import Enums.ScheduleStatus;
import java.time.*;

public class TimeSlot {
    private final String timeSlotID;
    private LocalDateTime startTime;
    private final LocalDateTime endTime;
    private final String doctorID;
    private String patientID;
    private ScheduleStatus status;

    /**
     * Constructs a `TimeSlot` object with the specified attributes.
     * 
     * @param timeSlotID A unique identifier for the time slot.
     * @param start The start time of the time slot.
     * @param end The end time of the time slot.
     * @param doctorID The unique identifier of the doctor associated with this time slot.
     * @param patientID The unique identifier of the patient (optional, can be null).
     * @param status The current status of the time slot (e.g., AVAILABLE, BOOKED).
     */
    public TimeSlot(String timeSlotID, LocalDateTime start, LocalDateTime end, String doctorID, String patientID, ScheduleStatus status) {
        this.timeSlotID = timeSlotID;
        this.startTime = start;
        this.endTime = end;
        this.doctorID = doctorID;
        this.patientID = patientID;
        this.status = status;
    }

    /**
     * Gets the unique identifier for this time slot.
     * 
     * @return The time slot ID.
     */
    public String getTimeSlotID() {
        return timeSlotID;
    }

    /**
     * Gets the start time of this time slot.
     * 
     * @return The start time as a `LocalDateTime` object.
     */
    public LocalDateTime getStartTime() {
        return startTime;
    }

    /**
     * Gets the end time of this time slot.
     * 
     * @return The end time as a `LocalDateTime` object.
     */
    public LocalDateTime getEndTime() {
        return endTime;
    }

    /**
     * Gets the unique identifier of the doctor associated with this time slot.
     * 
     * @return The doctor's ID.
     */
    public String getDoctorID() {
        return doctorID;
    }

    /**
     * Gets the unique identifier of the patient associated with this time slot, if any.
     * 
     * @return The patient's ID, or null if no patient is associated.
     */
    public String getPatientID() {
        return patientID;
    }

    /**
     * Gets the current status of this time slot.
     * 
     * @return The status of the time slot as a `ScheduleStatus` value.
     */
    public ScheduleStatus getStatus() {
        return status;
    }

    /**
     * Sets the unique identifier of the patient for this time slot.
     * 
     * @param patientID The patient's ID to associate with this time slot.
     */
    public void setPatientID(String patientID) {
        this.patientID = patientID;
    }

    /**
     * Updates the status of this time slot.
     * 
     * @param status The new status to set (e.g., AVAILABLE, BOOKED).
     */
    public void setStatus(ScheduleStatus status) {
        this.status = status;
    }

    /**
     * Updates the start time of this time slot.
     * 
     * @param startTime The new start time as a `LocalDateTime` object.
     */
    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }
}
