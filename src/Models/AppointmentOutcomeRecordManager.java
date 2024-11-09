package Models;

import java.time.LocalDate;
import enums.Service;
import enums.PrescriptionStatus;

public interface AppointmentOutcomeRecordManager {

    void createAppointmentOutcomeRecord(String appointmentID, String hospitalID, LocalDate date, Service service, 
                                        String medication, PrescriptionStatus prescriptionStatus, String notes);

    void viewAppointmentOutcomeRecords();

    void updateAppointmentOutcomeRecord(String appointmentID, LocalDate newDate, Service newService, 
                                        String newMedication, PrescriptionStatus newPrescriptionStatus, String newNotes);

    void deleteAppointmentOutcomeRecord(String appointmentID);

    boolean hasAppointmentOutcomeRecords();
}
