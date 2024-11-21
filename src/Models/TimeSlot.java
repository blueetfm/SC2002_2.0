package Models;
import Enums.ScheduleStatus;
import java.time.*;

public class TimeSlot {
    private String timeSlotID;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String doctorID;
    private String patientID;
    private ScheduleStatus status;

    public TimeSlot(String timeSlotID, LocalDateTime start, LocalDateTime end, String doctorID, String patientID, ScheduleStatus status) {
        this.timeSlotID = timeSlotID;
        this.startTime = start;
        this.endTime = end;
        this.doctorID = doctorID;
        this.patientID = patientID;
        this.status = status;
    }

    // Getters
    public String getTimeSlotID() { return timeSlotID; }
    public LocalDateTime getStartTime() { return startTime; }
    public LocalDateTime getEndTime() { return endTime; }
    public String getDoctorID() { return doctorID; }
    public String getPatientID() { return patientID; }
    public ScheduleStatus getStatus() { return status; }

    // Setters
    public void setPatientID(String patientID) { this.patientID = patientID; }
    public void setStatus(ScheduleStatus status) { this.status = status; }
}