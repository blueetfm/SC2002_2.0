/**
 * This enum defines the possible statuses of an appointment.
 * @author SCSCGroup4
 * @version 1.0
 * @since 2024-11-21
 */
package Enums;

public enum AppointmentStatus {
    /**
     * Waiting for the doctor to accept Appointment
     */
    PENDING,
    /**
     * The appointment has been confirmed by both parties.
     */
    CONFIRMED,
    /**
     * The appointment has been cancelled.
     */
    CANCELLED,
    /**
     * The appointment has been completed.
     */
    COMPLETED;
}