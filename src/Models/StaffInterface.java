package Models;

public interface StaffInterface {
    public static boolean addStaff(String name, String role, String gender, int age) {
        return StaffManager.addStaff(name, role, gender, age);
    }
    
    public static void removeStaff(String StaffID) {
        StaffManager.removeStaff(StaffID);
    }
    
    public static void viewStaffList() {
        StaffManager.viewStaffList();
    }
    
    public static boolean updateStaff(String staffID, String name, String role, String gender, int age) {
        return StaffManager.updateStaff(staffID, name, role, gender, age);
    }
    
    public static Staff getStaffByID(String staffId) {
        return StaffManager.getStaffByID(staffId);
    }
    
    public static boolean updatePassword(String hospitalID, String newPassword) {
        return StaffManager.updatePassword(hospitalID, newPassword);
    }
    
    public static String getCurrentPassword(String hospitalID) {
        return StaffManager.getCurrentPassword(hospitalID);
    }
}