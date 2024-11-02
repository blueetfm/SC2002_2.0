package Models;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class StaffList implements StaffManager {
    public Map<String, Staff> staffList;
    
    public StaffList() {
        this.staffList = new HashMap<>();
    }

    public void loadStaffCSV(String filePath) {
        try (Scanner scanner = new Scanner(new File(filePath))) {
            // Skip header
            if (scanner.hasNextLine()) {
                scanner.nextLine();
            }
            
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] parts = line.split(",");
                
                try {
                    if (parts.length == 5) {
                        String id = parts[0].trim();
                        String name = parts[1].trim();
                        String role = parts[2].trim();
                        String gender = parts[3].trim();
                        int age = Integer.parseInt(parts[4].trim());

                        staffList.put(id, new Staff(id, name, role, gender, age));
                    }
                } catch (NumberFormatException e) {
                    System.err.println("Error parsing number in line: " + line);
                }
            }
        } catch (FileNotFoundException e) {
            System.err.println("CSV file not found: " + e.getMessage());
        }
    }

    @Override
    public void addStaff(String StaffID, String password, String name, String role, String gender, int age) {
        staffList.put(StaffID, new Staff(StaffID, name, role, gender, age));
        System.out.println("Staff " + StaffID + " has been added. ");
    };

    @Override
    public void removeStaff(String StaffID) {
        Staff staff = staffList.get(StaffID);
        if (staff == null) {
            System.out.println("Staff not found: " + StaffID);
            return;
        }
        staffList.remove(StaffID);
        System.out.println("Staff " + StaffID + " has been removed. ");
    };
    
    @Override
    public void viewStaffList() {
        System.out.println("\nStaff List");
        System.out.println("--------------------");
        for (Staff staff : staffList.values()) {
            System.out.println(staff);
        }
    };
}
