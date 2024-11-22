/**
 * Represents a pharmacist in the hospital system. 
 * The `Pharmacist` class extends the `User` class and contains methods for interacting 
 * with medication records, prescriptions, and appointment outcomes.
 * @author SCSCGroup4
 * @version 1.0
 * @since 2024-11-21
 */
package Models;

import Enums.*;
import java.util.List;


public class Pharmacist extends User {
    protected String name;
    protected String gender;

    /**
     * Constructs a new Pharmacist object.
     * 
     * @param hospitalID The unique hospital ID assigned to the pharmacist.
     * @param password The password for the pharmacist's account.
     * @param role The role of the user (in this case, "Pharmacist").
     * @param name The full name of the pharmacist.
     * @param gender The gender of the pharmacist.
     * @param medicineListPath The path to the list of available medicines.
     * @param requestsPath The path to the medication replenishment requests.
     */
    public Pharmacist(String hospitalID, String password, String role, String name, String gender, String medicineListPath, String requestsPath) {
        super(hospitalID, password, role);
        this.name = name;
        this.gender = gender;
        MedicationInventoryManager.initialize(medicineListPath, requestsPath);
    }

    /**
     * Views the medication inventory in the system.
     * <p>
     * This method attempts to retrieve and display the current medication inventory.
     * If there is an error, it will print an error message and return false.
     * </p>
     * 
     * @return `true` if the inventory is successfully viewed, `false` otherwise.
     */
    public boolean viewMedicationInventory() {
        try {
            MedicationInventoryManager.viewMedicationInventory();
            return true;
        } catch (Exception e) {
            System.err.println("Error viewing inventory: " + e.getMessage());
            return false;
        }
    }

    /**
     * Updates the prescription status for a given appointment.
     * <p>
     * This method updates the prescription status of an appointment to "DISPENSED" 
     * if the prescription has not been dispensed yet. If the appointment does not exist 
     * or if the prescription has already been dispensed, an appropriate message is displayed.
     * </p>
     * 
     * @param appointmentID The unique ID of the appointment for which the prescription status will be updated.
     * @return `true` if the prescription status is successfully updated, `false` otherwise.
     */
    public boolean updatePrescriptionStatus(String appointmentID) {
        List<AppointmentOutcomeRecord> AORList = AppointmentOutcomeRecordManager.showAllAppointmentOutcomeRecords();
        if (AORList == null) {
            System.out.println("No appointment records found.");
            return false;
        } 
    
        for (AppointmentOutcomeRecord AOR : AORList) {
            if (AOR.getAppointmentID().equals(appointmentID)) {
                if (AOR.getStatus() == PrescriptionStatus.PENDING) {
                    // Check if medicine is in stock
                    if (!AOR.getMedicine().equalsIgnoreCase("NONE")) {
                        boolean prescribed = MedicationInventoryManager.prescribeMedication(
                            AOR.getMedicine(), 1); // Assuming 1 unit per prescription
                        
                        if (!prescribed) {
                            System.out.println("Cannot dispense: Medicine out of stock!");
                            return false;
                        }
                    }
                    
                    AOR.setStatus(PrescriptionStatus.DISPENSED);
                    System.out.println("Prescription status updated to DISPENSED.");
                    return true;
                } else {
                    System.out.println("Prescription has already been dispensed.");
                    return false;
                }
            }
        }
        System.out.println("Appointment ID not found.");
        return false;
    }

    
    /** 
     * @return boolean
     */
    public boolean viewAppointmentOutcomes() {
        List<AppointmentOutcomeRecord> AORList = AppointmentOutcomeRecordManager.showAllAppointmentOutcomeRecords();
        if (AORList == null || AORList.isEmpty()) {
            System.out.println("No appointment outcome records found.");
            return false;
        } 
    
        System.out.println("\nAppointment Outcome Records:");
        System.out.println("----------------------------------------");
        boolean hasPending = false;
        for (AppointmentOutcomeRecord AOR : AORList) {
            if (AOR.getStatus() == PrescriptionStatus.PENDING) {
                hasPending = true;
                System.out.printf("Appointment ID: %s\n" +
                                "Date: %s\n" +
                                "Service: %s\n" +
                                "Prescribed Medicine: %s\n" +
                                "Status: %s\n" +
                                "Notes: %s\n",
                    AOR.getAppointmentID(),
                    AOR.getDate(),
                    AOR.getService(),
                    AOR.getMedicine(),
                    AOR.getStatus(),
                    AOR.getNotes());
                System.out.println("----------------------------------------");
            }
        }
        
        if (!hasPending) {
            System.out.println("No pending prescriptions found.");
        }
        return true;
    }

    /**
     * Requests the replenishment of a medication in the inventory.
     * <p>
     * This method checks the requested quantity to ensure it's positive, then submits 
     * a replenishment request for the specified medication. If the quantity is invalid 
     * or an error occurs, an appropriate message is displayed.
     * </p>
     * 
     * @param medicationName The name of the medication to replenish.
     * @param quantity The quantity of the medication to replenish.
     * @return `true` if the replenishment request is successfully submitted, `false` otherwise.
     */
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

    /**
     * Logs out the pharmacist from the system.
     * <p>
     * This method logs the pharmacist out of the system and prints a logout message.
     * </p>
     * 
     * @return `true` if the logout is successful, `false` otherwise.
     */
    public boolean logout() {
        try {
            System.out.println("Logging out pharmacist: " + this.name);
            return true;
        } catch (Exception e) {
            System.err.println("Error during logout: " + e.getMessage());
            return false;
        }
    }

    /**
     * Gets the full name of the pharmacist.
     * 
     * @return The full name of the pharmacist.
     */
    public String getName() {
        return name;
    }

    /**
     * Gets the gender of the pharmacist.
     * 
     * @return The gender of the pharmacist.
     */
    public String getGender() {
        return gender;
    }

    /**
     * Gets the hospital ID of the pharmacist.
     * 
     * @return The hospital ID of the pharmacist.
     */
    public String getStaffID() {
        return hospitalID;
    }
}
