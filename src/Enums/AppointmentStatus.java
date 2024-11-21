/**
 * This enum defines the possible statuses of an appointment.
 * @author SCSCGroup4
 * @version 1.0
 * @since 2024-11-21
 */
package Enums;

public enum AppointmentStatus {
    /**
     * The appointment is available for scheduling.
     */
    AVAILABLE,
    /**
     * The appointment has been scheduled but not yet confirmed.
     */
    SCHEDULED,
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