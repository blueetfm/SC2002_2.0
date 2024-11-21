package Models;

import java.util.List;

public class Administrator extends User {
    private String name;
    private String gender;
    // private StaffManager staffManager;
    // private MedicationInventoryManager medicationInventory;

    public Administrator(String hospitalID, String password, String role, 
                     String name, String gender) {
        super(hospitalID, password, role);
        this.name = name;
        this.gender = gender;
        StaffManager.initialize("data/Staff_List.csv"); 
        MedicationInventoryManager.initialize(
            "data/Medicine_List.csv", 
            "data/Medication_Replenish_Requests.csv"
        );
    }

    // Staff Management Methods
    public void viewStaffList() {
        try {
            StaffInterface.viewStaffList();
        } catch (Exception e) {
            System.err.println("Error viewing staff list: " + e.getMessage());
        }
    }

    public boolean addStaff(String name, String role, String gender, int age) {
        return StaffInterface.addStaff(name, role, gender, age);
    }

    public boolean removeStaff(String staffID) {
        try {
            StaffInterface.removeStaff(staffID);
            return true;
        } catch (Exception e) {
            System.err.println("Error removing staff: " + e.getMessage());
            return false;
        }
    }

    public boolean updateStaff(String staffId, String name, String role, String gender, int age) {
        return StaffInterface.updateStaff(staffId, name, role, gender, age);
    }

    public Staff getStaffByID(String staffId) {
        return StaffInterface.getStaffByID(staffId);
    }


    // Medication Management Methods
    public void viewMedicationInventory() {
        MedicationInventoryInterface.viewMedicationInventory();
    }

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
    public void viewReplenishRequests() {
        MedicationInventoryInterface.displayReplenishRequests();
    }

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

    //reset user pw
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

    public String getUserPassword(String hospitalID) {
        return StaffInterface.getCurrentPassword(hospitalID);
    }
    
    
    // Appointment Methods
    public Appointment viewAppointmentDetailsByID(String appointmentID) {
        Appointment appointment = AppointmentManager.getAppointmentByID(appointmentID);
        return appointment;
    }

    public List<Appointment> viewAppointmentDetailsByPatientID(String patientID) {
        List<Appointment> appointments = AppointmentManager.getAppointmentsByPatientID(patientID);
        return appointments;
    }

    public List<Appointment> viewAppointmentDetailsByDoctorID(String doctorID) {
        List<Appointment> appointments = AppointmentManager.getAppointmentsByDoctorID(doctorID);
        return appointments;
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

    // Getters
    public String getName() {
        return name;
    }

    public String getGender() {
        return gender;
    }
}