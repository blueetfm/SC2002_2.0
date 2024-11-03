package Models;
import java.time.LocalDate;
import java.util.*;
import enums.*;

import Views.UserMenu;

public class AppointmentOutcomeRecordList implements AppointmentOutcomeRecordManager {
    private static AppointmentOutcomeRecordList instance; // Singleton --> only one class/instance at any time
    // https://www.geeksforgeeks.org/singleton-class-java/
    protected ArrayList<AppointmentOutcomeRecord> appointmentOutcomeRecordList;

    public AppointmentOutcomeRecordList(){
        this.appointmentOutcomeRecordList = new ArrayList<>();
    }

    // Static method to create instance of Singleton class
    public static AppointmentOutcomeRecordList getInstance() {
        if (instance == null) {
            instance = new AppointmentOutcomeRecordList();
        }
        return instance;
    }

    // CRUD
    public void createAppointmentOutcomeRecord(String appointmentID, String hospitalID, LocalDate date, Service service, String medication, PrescriptionStatus prescriptionStatus, String notes) {
        for (AppointmentOutcomeRecord AOR : appointmentOutcomeRecordList){
            if (AOR.getAppointmentID().equals(appointmentID)) {
                System.out.println("Appointment ID is already existing.");
                return;
            }
        }
        AppointmentOutcomeRecord newRecord = new AppointmentOutcomeRecord(appointmentID, hospitalID, date, service, medication, prescriptionStatus, notes);
        appointmentOutcomeRecordList.add(newRecord);
        System.out.println("Appointment successfully recorded!");
        return;
    }

    public void readAppointmentOutcomeRecord(String appointmentID){
        String loggedInID = UserMenu.getLoggedInHospitalID(); // Get the logged-in user's ID
        boolean isPatient = loggedInID.startsWith("P") && (loggedInID.length() == 5);
        boolean resultAOR = false; // to check if there is a valid appointmentID to match or not

        for (AppointmentOutcomeRecord AOR : appointmentOutcomeRecordList) {
            if (AOR.getAppointmentID().equals(appointmentID)){
                resultAOR = true;
                if (!isPatient || AOR.getHospitalID().equals(loggedInID)){
                    AOR.printDetails();
                } else {
                    System.out.println("Access Denied. You can only view your own appointment records.");
                }
            }
        }

        if (!resultAOR) {
            System.out.println("No matching appointment ID.");
        }

        return;
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
