/**
 * Represents an Administrator in the hospital system.
 * This class provides methods for managing staff, medications, appointment details,
 * and user passwords. It also facilitates replenishment requests and inventory management.
 * The Administrator can also log out and view/modify records related to patients and staff.
 * @author SCSCGroup4
 * @version 1.0
 * @since 2024-11-21
 */

package Models;

import Enums.AppointmentStatus;
import Services.AppointmentInterface;
import Services.MedicationInventoryInterface;
import Services.StaffInterface;
import Services.TimeSlotInterface;
import java.util.List;
import java.util.stream.Collectors;


public class Administrator extends User {
    
    /** Name of the administrator */
    private String name;
    
    /** Gender of the administrator */
    private String gender;

    /**
     * Constructs an Administrator object with specified details.
     * Initializes the staff and medication inventory management systems.
     * 
     * @param hospitalID The unique identifier for the hospital.
     * @param password The password for the administrator account.
     * @param role The role of the user (administrator).
     * @param name The name of the administrator.
     * @param gender The gender of the administrator.
     */
    public Administrator(String hospitalID, String password, String role, 
                         String name, String gender) {
        super(hospitalID, password, role);
        this.name = name;
        this.gender = gender;
        
        // Initialize the staff and medication inventory data
        StaffManager.initialize("data/Staff_List.csv"); 
        MedicationInventoryManager.initialize(
            "data/Medicine_List.csv", 
            "data/Medication_Replenish_Requests.csv"
        );
    }

    // Staff Management Methods

    /**
     * Views the list of all staff in the hospital.
     * 
     * @throws Exception If there is an error viewing the staff list.
     */
    public void viewStaffList() {
        try {
            StaffInterface.viewStaffList();
        } catch (Exception e) {
            System.err.println("Error viewing staff list: " + e.getMessage());
        }
    }

    /**
     * Adds a new staff member to the system.
     * 
     * @param name The name of the new staff member.
     * @param role The role of the new staff member.
     * @param gender The gender of the new staff member.
     * @param age The age of the new staff member.
     * @return true if the staff member is successfully added, false otherwise.
     */
    public boolean addStaff(String name, String role, String gender, int age) {
        return StaffInterface.addStaff(name, role, gender, age);
    }

    /**
     * Removes a staff member from the system based on their staff ID.
     * 
     * @param staffID The unique identifier for the staff member to be removed.
     * @return true if the staff member is successfully removed, false otherwise.
     */
    public boolean removeStaff(String staffID) {
        try {
            StaffInterface.removeStaff(staffID);
            return true;
        } catch (Exception e) {
            System.err.println("Error removing staff: " + e.getMessage());
            return false;
        }
    }

    /**
     * Updates the details of an existing staff member.
     * 
     * @param staffId The unique identifier of the staff member to be updated.
     * @param name The updated name of the staff member.
     * @param role The updated role of the staff member.
     * @param gender The updated gender of the staff member.
     * @param age The updated age of the staff member.
     * @return true if the staff member's details are successfully updated, false otherwise.
     */
    public boolean updateStaff(String staffId, String name, String role, String gender, int age) {
        return StaffInterface.updateStaff(staffId, name, role, gender, age);
    }

    /**
     * Retrieves a staff member by their staff ID.
     * 
     * @param staffId The unique identifier of the staff member.
     * @return The Staff object associated with the given staff ID.
     */
    public Staff getStaffByID(String staffId) {
        return StaffInterface.getStaffByID(staffId);
    }

    // Medication Management Methods

    /**
     * Views the current medication inventory.
     */
    public void viewMedicationInventory() {
        MedicationInventoryInterface.viewMedicationInventory();
    }

    /**
     * Adds a new medication to the inventory.
     * 
     * @param medicineName The name of the medication.
     * @param initialStock The initial stock quantity of the medication.
     * @param lowStockAlert The threshold at which the stock is considered low.
     * @return true if the medication is successfully added, false otherwise.
     */
    public boolean addMedication(String medicineName, int initialStock, int lowStockAlert) {
        try {
            if (medicineName.trim().isEmpty()) {
                System.out.println("Medicine name cannot be empty!");
                return false;
            }
            MedicationInventoryInterface.addMedication(medicineName, initialStock, lowStockAlert);
            return true;
        } catch (Exception e) {
            System.err.println("Error adding medication: " + e.getMessage());
            return false;
        }
    }

    /**
     * Removes a medication from the inventory.
     * 
     * @param medicineName The name of the medication to be removed.
     * @return true if the medication is successfully removed, false otherwise.
     */
    public boolean removeMedication(String medicineName) {
        try {
            if (medicineName.trim().isEmpty()) {
                System.out.println("Medicine name cannot be empty!");
                return false;
            }
            MedicationInventoryInterface.removeMedication(medicineName);
            return true;
        } catch (Exception e) {
            System.err.println("Error removing medication: " + e.getMessage());
            return false;
        }
    }

    /**
     * Updates the stock levels of a medication in the inventory.
     * 
     * @param medicineName The name of the medication.
     * @param newStock The new stock quantity of the medication.
     * @param newLowStockAlert The new low stock threshold.
     * @return true if the medication stock is successfully updated, false otherwise.
     */
    public boolean updateMedicationStock(String medicineName, Integer newStock, Integer newLowStockAlert) {
        try {
            if (medicineName.trim().isEmpty()) {
                System.out.println("Medicine name cannot be empty!");
                return false;
            }
            MedicationInventoryInterface.updateMedication(medicineName, newStock, newLowStockAlert);
            return true;
        } catch (Exception e) {
            System.err.println("Error updating medication: " + e.getMessage());
            return false;
        }
    }

    // Replenishment Methods

    /**
     * Views all current medication replenishment requests.
     */
    public void viewReplenishRequests() {
        MedicationInventoryInterface.displayReplenishRequests();
    }

    /**
     * Approves a replenishment request for a specific medication.
     * 
     * @param medicineName The name of the medication for which the replenishment request is approved.
     * @return true if the request is approved successfully, false otherwise.
     */
    public boolean approveReplenishRequest(String medicineName) {
        try {
            if (medicineName.trim().isEmpty()) {
                System.out.println("Medicine name cannot be empty!");
                return false;
            }
            return MedicationInventoryInterface.approveReplenishRequests(medicineName);
        } catch (Exception e) {
            System.err.println("Error approving replenishment request: " + e.getMessage());
            return false;
        }
    }

    // User Password Management

    /**
     * Updates the password of a user based on their hospital ID.
     * 
     * @param hospitalID The hospital ID of the user whose password needs to be updated.
     * @param newPassword The new password for the user.
     * @return true if the password is updated successfully, false otherwise.
     */
    public boolean updateUserPassword(String hospitalID, String newPassword) {
        try {
            if (hospitalID.trim().isEmpty() || newPassword.trim().isEmpty()) {
                System.out.println("Hospital ID and password cannot be empty!");
                return false;
            }
            return StaffInterface.updatePassword(hospitalID, newPassword);
        } catch (Exception e) {
            System.err.println("Error updating password: " + e.getMessage());
            return false;
        }
    }

    /**
     * Retrieves the current password for a given hospital ID.
     * 
     * @param hospitalID The hospital ID of the user.
     * @return The current password for the user.
     */
    public String getUserPassword(String hospitalID) {
        return StaffInterface.getCurrentPassword(hospitalID);
    }

    // Appointment Management Methods

    public List<Appointment> viewAllAppointments() {
        AppointmentInterface.initializeObjects();
        List<Appointment> appointments = AppointmentInterface.getAllAppointments();
        if (appointments.isEmpty()) {
            System.out.println("No appointments found in the system.");
            return appointments;
        }

        System.out.println("\nAll Appointments in System:");
        System.out.println("----------------------------------------");
        appointments.sort((a1, a2) -> a1.getDate().compareTo(a2.getDate()));
        
        printAppointments(appointments);
        return appointments;
    }

    /**
     * Views the details of an appointment by its ID.
     * 
     * @param appointmentID The ID of the appointment to view.
     * @return The Appointment object associated with the given ID.
     */
    // public Appointment viewAppointmentDetailsByID(String appointmentID) {
    //     Appointment appointment = AppointmentManager.getAppointmentByID(appointmentID);
    //     return appointment;
    // }

    public List<Appointment> viewAppointmentsByStatus(AppointmentStatus status) {
        AppointmentInterface.initializeObjects();
        List<Appointment> allAppointments = AppointmentInterface.getAllAppointments();
        List<Appointment> filteredAppointments = allAppointments.stream()
            .filter(apt -> apt.getStatus() == status)
            .sorted((a1, a2) -> a1.getDate().compareTo(a2.getDate()))
            .collect(Collectors.toList());

        if (filteredAppointments.isEmpty()) {
            System.out.println("No appointments found with status: " + status);
            return filteredAppointments;
        }

        System.out.println("\nAppointments with status " + status + ":");
        System.out.println("----------------------------------------");
        
        printAppointments(filteredAppointments);
        return filteredAppointments;
    }

    /**
     * Views all appointments for a given patient by their ID.
     * 
     * @param patientID The ID of the patient whose appointments are to be viewed.
     * @return A list of appointments for the specified patient.
     */
    public List<Appointment> viewAppointmentsByPatientID(String patientID) {
        AppointmentInterface.initializeObjects();
        List<Appointment> appointments = AppointmentInterface.getAppointmentsByPatientID(patientID);
        if (appointments.isEmpty()) {
            System.out.println("No appointments found for Patient " + patientID);
            return appointments;
        }

        System.out.println("\nAppointments for Patient " + patientID + ":");
        System.out.println("----------------------------------------");
        appointments.sort((a1, a2) -> a1.getDate().compareTo(a2.getDate()));
        
        printAppointments(appointments);
        return appointments;
    }

    /**
     * Views all appointments for a given doctor by their ID.
     * 
     * @param doctorID The ID of the doctor whose appointments are to be viewed.
     * @return A list of appointments for the specified doctor.
     */
    public List<Appointment> viewAppointmentsByDoctorID(String doctorID) {
        AppointmentInterface.initializeObjects();
        List<Appointment> appointments = AppointmentInterface.getAppointmentsByDoctorID(doctorID);
        if (appointments.isEmpty()) {
            System.out.println("No appointments found for Doctor " + doctorID);
            return appointments;
        }

        System.out.println("\nAppointments for Doctor " + doctorID + ":");
        System.out.println("----------------------------------------");
        appointments.sort((a1, a2) -> a1.getDate().compareTo(a2.getDate()));
        
        printAppointments(appointments);
        return appointments;
    }

    //also handles empty appointment class
    private void printAppointments(List<Appointment> appointments) {
        TimeSlotInterface.initializeObjects();
        
        for (Appointment apt : appointments) {
            System.out.printf("Appointment ID: %s\n" +
                            "Patient ID: %s\n" +
                            "Doctor ID: %s\n" +
                            "Date: %s\n" +
                            "Service: %s\n" +
                            "Status: %s\n",
                apt.getAppointmentID(),
                apt.getPatientID(),
                apt.getDoctorID(),
                apt.getDate(),
                apt.getService(),
                apt.getStatus());

            TimeSlot slot = TimeSlotInterface.getTimeSlotByID(apt.getTimeSlotID());
            if (slot != null) {
                System.out.printf("Time: %s - %s\n",
                    slot.getStartTime().toLocalTime(),
                    slot.getEndTime().toLocalTime());
            } else {
                System.out.println("Time: Not available");
            }
            
            System.out.println("----------------------------------------");
        }
    }

    public boolean logout() {
        try {
            System.out.println("Logging out administrator: " + this.name);
            return true;
        } catch (Exception e) {
            System.err.println("Error during logout: " + e.getMessage());
            return false;
        }
    }

    /**
     * Gets the name of the administrator.
     * 
     * @return The name of the administrator.
     */
    public String getName() {
        return name;
    }

    /**
     * Gets the gender of the administrator.
     * 
     * @return the gender of the administrator.
     */
    public String getGender() {
        return gender;
    }
}