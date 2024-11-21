package Models;

public interface StaffInterface {
    public static boolean addStaff(String name, String role, String gender, int age) {
        return false;
    }
    public static void removeStaff(String StaffID) {}
    public static void viewStaffList() {}
    public static boolean updateStaff(String staffID, String name, String role, String gender, int age) {
        return false;
    }
    public static Staff getStaffByID(String staffId) {
        return null;
    }
    public static boolean updatePassword(String hospitalID, String newPassword) {
        return false;
    }
    public static String getCurrentPassword(String hospitalID) {
        return null;
    }
}