package Models;

import java.time.LocalDate;

public interface AppointmentOutcomeRecordManager {
    public void createAppointmentOutcomeRecord(String appointmentID, String hospitalID, LocalDate date, Service service, String medication, PrescriptionStatus prescriptionStatus, String notes);

    public void readAppointmentOutcomeRecord(String appointmentID);

    public void updateAppointmentOutcomeRecord(String appointmentID);

    public void deleteAppointmentOutcomeRecord(String appointmentID);
}
