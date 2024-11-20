package Models;

public interface StaffManager {
    public boolean addStaff(String name, String role, String gender, int age);
    public void removeStaff(String StaffID);
    public void viewStaffList();
    public boolean updateStaff(String staffID, String name, String role, String gender, int age);

}