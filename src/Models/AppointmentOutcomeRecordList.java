package Models;
import java.time.LocalDate;
import java.util.*;

public class AppointmentOutcomeRecordList implements AppointmentOutcomeRecordManager {
    protected ArrayList<AppointmentOutcomeRecord> appointmentOutcomeRecordList;

    public AppointmentOutcomeRecordList(){
        this.appointmentOutcomeRecordList = new ArrayList<>();
    }

    // CRUD
    public void createAppointmentOutcomeRecord(String appointmentID, LocalDate date, Service service, String medication, PrescriptionStatus prescriptionStatus, String notes) {
        AppointmentOutcomeRecord newRecord = new AppointmentOutcomeRecord(appointmentID, date, service, medication, prescriptionStatus, notes);
        appointmentOutcomeRecordList.add(newRecord);
    }

    public void readAppointmentOutcomeRecord(String appointmentID){
        for (AppointmentOutcomeRecord AOR : appointmentOutcomeRecordList) {
            if (AOR.getAppointmentID().equals(appointmentID)){
                System.out.println("Date of Appointment: " + AOR.getDate().toString());
                System.out.println("Service provided: " + AOR.getService());
                System.out.println("Prescribed Medication: " + AOR.getMedication());

            }
        }
    }

    public void updateAppointmentOutcomeRecord(String appointmentID){
        for (AppointmentOutcomeRecord AOR : appointmentOutcomeRecordList) {
            if (AOR.getAppointmentID().equals(appointmentID)){

            }
        }
    }

    

    public void deleteAppointmentOutcomeRecord(String appointmentID){
        for (AppointmentOutcomeRecord AOR : appointmentOutcomeRecordList) {
            if (AOR.getAppointmentID().equals(appointmentID)){

            }
        }
        
    }
}
