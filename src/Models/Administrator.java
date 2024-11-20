package Models;

public class Administrator extends User {
    private final String name;
    private final String gender;
    private final StaffList staffList;
    private final MedicationInventory medicationInventory;

    public Administrator(String hospitalID, String password, String role, 
                     String name, String gender) {
        super(hospitalID, password, role);
        this.name = name;
        this.gender = gender;
        this.staffList = new StaffList("data/Staff_List.csv");
        this.medicationInventory = MedicationInventory.getInstance(
            "data/Medicine_List.csv", 
            "data/Medication_Replenish_Requests.csv"
        );
    }

    // Staff Management Methods
    public void viewStaffList() {
        staffList.viewStaffList();
    }

    public boolean addStaff(String staffID, String name, String role, String gender, int age) {
        try {
            staffList.addStaff(staffID, "password", name, role, gender, age);
            return true;
        } catch (Exception e) {
            System.err.println("Error adding staff: " + e.getMessage());
            return false;
        }
    }

    public boolean removeStaff(String staffID) {
        try {
            staffList.removeStaff(staffID);
            return true;
        } catch (Exception e) {
            System.err.println("Error removing staff: " + e.getMessage());
            return false;
        }
    }

    public boolean updateStaff(String staffId, String name, String role, String gender, int age) {
        return staffList.updateStaff(staffId, name, role, gender, age);
    }

    public Staff getStaffById(String staffId) {
        return staffList.getStaffById(staffId);
    }


    // Medication Management Methods
    public void viewMedicationInventory() {
        medicationInventory.viewMedicationInventory();
    }

    public boolean addMedication(String medicineName, int initialStock, int lowStockAlert) {
        try {
            if (medicineName.trim().isEmpty()) {
                System.out.println("Medicine name cannot be empty!");
                return false;
            }
            medicationInventory.addMedication(medicineName, initialStock, lowStockAlert);
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
            medicationInventory.removeMedication(medicineName);
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
            medicationInventory.updateMedication(medicineName, newStock, newLowStockAlert);
            return true;
        } catch (Exception e) {
            System.err.println("Error updating medication: " + e.getMessage());
            return false;
        }
    }

    // Replenishment Methods
    public void viewReplenishRequests() {
        medicationInventory.displayReplenishRequests();
    }

    public boolean approveReplenishRequest(String medicineName) {
        try {
            if (medicineName.trim().isEmpty()) {
                System.out.println("Medicine name cannot be empty!");
                return false;
            }
            return medicationInventory.approveReplenishRequests(medicineName);
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
            return staffList.updatePassword(hospitalID, newPassword);
        } catch (Exception e) {
            System.err.println("Error updating password: " + e.getMessage());
            return false;
        }
    }

    public String getUserPassword(String hospitalID) {
        return staffList.getCurrentPassword(hospitalID);
    }
    
    
    // Appointment Methods
    public boolean viewAppointmentDetails() {
        // TODO: Implement appointment viewing logic when ready
        System.out.println("Appointment viewing functionality not implemented yet");
        return false;
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