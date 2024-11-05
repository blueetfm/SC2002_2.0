package Models;

public class StaffListTest {
    public static void main(String[] args) {
        String testFilePath = "data(testCopy)/Staff_List.csv";
        StaffList staffList = new StaffList(testFilePath);

        System.out.println("=== Initial Staff List ===");
        staffList.viewStaffList();

        System.out.println("\n=== Testing Add Staff ===");
        staffList.addStaff("D003", "password", "Jane Doe", "Doctor", "Female", 35);
        staffList.addStaff("P002", "password", "Tom Wilson", "Pharmacist", "Male", 42);
        System.out.println("\nStaff List after additions:");
        staffList.viewStaffList();

        System.out.println("\n=== Testing Remove Staff ===");
        staffList.removeStaff("D001");
        System.out.println("\nStaff List after removing D001:");
        staffList.viewStaffList();

        System.out.println("\n=== Testing Update Staff ===");
        staffList.updateStaff("P001", "Mark Johnson", "Senior Pharmacist", "Male", 30);
        System.out.println("\nStaff List after updating P001:");
        staffList.viewStaffList();

        System.out.println("\n=== Testing Error Cases ===");
        System.out.println("\nTrying to add duplicate staff:");
        staffList.addStaff("D003", "password", "Another Jane", "Doctor", "Female", 40);

        System.out.println("\nTrying to remove non-existent staff:");
        staffList.removeStaff("X999");

        System.out.println("\nTrying to update non-existent staff:");
        staffList.updateStaff("X999", "Not Found", "Role", "Male", 25);
    }
}