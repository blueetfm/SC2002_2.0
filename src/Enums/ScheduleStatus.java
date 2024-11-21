/**
 * This enum defines the possible statuses of a doctor's schedule timeslot.
 * @author SCSCGroup4
 * @version 1.0
 * @since 2024-11-21
 */
package Enums;

public enum ScheduleStatus {
    /**
     * The timeslot is available for booking.
     */
    AVAILABLE,
    /**
     * The timeslot has been requested but not yet confirmed.
     */
    PENDING,
    /**
     * The timeslot has been reserved for an appointment.
     */
    RESERVED,
    /**
     * The timeslot has been cancelled.
     */
    CANCELLED,

    COMPLETED;
}