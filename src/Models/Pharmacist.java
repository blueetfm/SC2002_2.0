package Models;

import Enums.*;

public class Pharmacist extends User {
    protected String name;
    protected String gender;

    public Pharmacist(String hospitalID, String password, String role, String name, String gender, String medicineListPath, String requestsPath) {
        super(hospitalID, password, role);
        this.name = name;
        this.gender = gender;
        MedicationInventoryManager.initialize(medicineListPath, requestsPath);
    }

    public boolean viewMedicationInventory() {
        try {
            MedicationInventoryManager.viewMedicationInventory();
            return true;
        } catch (Exception e) {
            System.err.println("Error viewing inventory: " + e.getMessage());
            return false;
        }
    }

    public boolean updatePrescriptionStatus(String appointmentID) {
        try {
            AppointmentOutcomeRecord appointmentOutcomeRecord = AppointmentManager.getAppointmentOutcomeRecordByID(appointmentID);
            if (appointmentOutcomeRecord == null){
                System.out.println("Appointment does not exist.");
                return false;
            } else if (appointmentOutcomeRecord.getPrescriptionStatus().name().equals("DISPENSED")){
                System.out.println("Prescription has already been dispensed.");
                return false;
            }
            appointmentOutcomeRecord.setPrescriptionStatus(PrescriptionStatus.DISPENSED);
            return true;
        } catch (Exception e) {
            System.err.println("Error viewing appointment: " + e.getMessage());
            return false;
        }
    }

    public boolean viewAppointmentOutcome(String appointmentID) {
        try {
            AppointmentOutcomeRecord appointmentOutcomeRecord = AppointmentManager.getAppointmentOutcomeRecordByID(appointmentID);
            if (appointmentOutcomeRecord == null){
                System.out.println("Appointment does not exist.");
                return false;
            }
            AppointmentManager.printAppointmentOutcomeRecord(appointmentOutcomeRecord);
            return true;
        } catch (Exception e) {
            System.err.println("Error viewing appointment: " + e.getMessage());
            return false;
        }
    }

    public boolean requestMedicationReplenishment(String medicationName, int quantity) {
        try {
            if (quantity <= 0) {
                System.out.println("Quantity must be positive");
                return false;
            }
            MedicationInventoryManager.submitReplenishRequest(medicationName, quantity);
            return true;
        } catch (Exception e) {
            System.err.println("Error submitting replenish request: " + e.getMessage());
            return false;
        }
    }

    public boolean logout() {
        try {
            System.out.println("Logging out pharmacist: " + this.name);
            return true;
        } catch (Exception e) {
            System.err.println("Error during logout: " + e.getMessage());
            return false;
        }
    }

    // Getters
    public String getName() {
        return name;
    }

    public String getGender() {
        return gender;
    }

    public String getStaffID() {
        return hospitalID;
    }
}