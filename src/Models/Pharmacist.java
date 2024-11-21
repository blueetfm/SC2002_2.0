package Models;

import Enums.*;

/**
 * Represents a pharmacist in the hospital system. 
 * The `Pharmacist` class extends the `User` class and contains methods for interacting 
 * with medication records, prescriptions, and appointment outcomes.
 * @author SCSCGroup4
 * @version 1.0
 * @since 2024-11-21
 */
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
        try {
            AppointmentOutcomeRecord appointmentOutcomeRecord = AppointmentManager.getAppointmentOutcomeRecordByID(appointmentID);
            if (appointmentOutcomeRecord == null) {
                System.out.println("Appointment does not exist.");
                return false;
            } else if (appointmentOutcomeRecord.getPrescriptionStatus().name().equals("DISPENSED")) {
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

    /**
     * Views the outcome of a given appointment.
     * <p>
     * This method retrieves the appointment outcome record by appointment ID and prints 
     * the details. If the appointment does not exist, an error message is displayed.
     * </p>
     * 
     * @param appointmentID The unique ID of the appointment to view the outcome of.
     * @return `true` if the appointment outcome is successfully viewed, `false` otherwise.
     */
    public boolean viewAppointmentOutcome(String appointmentID) {
        try {
            AppointmentOutcomeRecord appointmentOutcomeRecord = AppointmentManager.getAppointmentOutcomeRecordByID(appointmentID);
            if (appointmentOutcomeRecord == null) {
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
