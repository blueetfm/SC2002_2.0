package Models;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;


public class StaffList implements StaffManager {
    //staff list csv 
    private String csvFilePath;
    private static final String CSV_HEADER = "Staff ID,Name,Role,Gender,Age";

    //user list csv
    private String userListPath;
    private static final String USER_CSV_HEADER = "Hospital ID,Password";
    
    public StaffList(String csvFilePath) {
        this.csvFilePath = csvFilePath;
        this.userListPath = csvFilePath.replace("Staff_List.csv", "User_list.csv");
        File file = new File(csvFilePath);
        if (!file.exists()) {
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(csvFilePath))) {
                writer.write(CSV_HEADER);
                writer.newLine();
            } catch (IOException e) {
                System.err.println("Error initializing CSV file: " + e.getMessage());
            }
        }
    }

    //handles staff-list csv operations
    private synchronized List<Staff> readAllStaff() {
        List<Staff> staffList = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(csvFilePath))) {
            String line;
            boolean firstLine = true;
            while ((line = reader.readLine()) != null) {
                if (firstLine) {
                    firstLine = false;
                    continue;
                }
                String[] parts = line.split(",");
                if (parts.length == 5) {
                    staffList.add(new Staff(
                        parts[0].trim(),
                        parts[1].trim(),
                        parts[2].trim(),
                        parts[3].trim(),
                        Integer.parseInt(parts[4].trim())
                    ));
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading CSV file: " + e.getMessage());
        }
        return staffList;
    }

    private synchronized void writeAllStaff(List<Staff> staffList) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(csvFilePath))) {
            writer.write(CSV_HEADER);
            writer.newLine();
            for (Staff staff : staffList) {
                writer.write(staff.toCSVString());
                writer.newLine();
            }
        } catch (IOException e) {
            System.err.println("Error writing to CSV file: " + e.getMessage());
        }
    }

    //handles user list csv operations
    private synchronized List<String[]> readUserList() {
        List<String[]> userList = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(userListPath))) {
            String line;
            boolean firstLine = true;
            while ((line = reader.readLine()) != null) {
                if (firstLine) {
                    firstLine = false;
                    continue;
                }
                String[] parts = line.split(",");
                if (parts.length == 2) {
                    userList.add(new String[]{parts[0].trim(), parts[1].trim()});
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading User list CSV file: " + e.getMessage());
        }
        return userList;
    }

    private synchronized void writeUserList(List<String[]> userList) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(userListPath))) {
            writer.write(USER_CSV_HEADER);
            writer.newLine();
            for (String[] user : userList) {
                writer.write(String.format("%s,%s", user[0], user[1]));
                writer.newLine();
            }
        } catch (IOException e) {
            System.err.println("Error writing to User list CSV file: " + e.getMessage());
        }
    }

    @Override
    public synchronized void addStaff(String StaffID, String password, String name, String role, String gender, int age) {
        // First check if staff exists in Staff_List
        List<Staff> staffList = readAllStaff();
        boolean exists = staffList.stream()
            .anyMatch(s -> s.getStaffID().equals(StaffID));
        
        if (exists) {
            System.out.println("Staff ID already exists: " + StaffID);
            return;
        }

        // Add to Staff_List
        staffList.add(new Staff(StaffID, name, role, gender, age));
        writeAllStaff(staffList);

        // Add to User_list
        List<String[]> userList = readUserList();
        userList.add(new String[]{StaffID, password});
        writeUserList(userList);

        System.out.println("Staff " + StaffID + " has been added to the system with default password: " + password);
    }

    @Override
    public synchronized void removeStaff(String StaffID) {
        // Remove from Staff_List
        List<Staff> staffList = readAllStaff();
        boolean removed = staffList.removeIf(staff -> staff.getStaffID().equals(StaffID));
        
        if (removed) {
            writeAllStaff(staffList);
            
            // Remove from User_list
            List<String[]> userList = readUserList();
            boolean userRemoved = userList.removeIf(user -> user[0].equals(StaffID));
            if (userRemoved) {
                writeUserList(userList);
            }
            
            System.out.println("Staff " + StaffID + " has been removed from both Staff and User lists.");
        } else {
            System.out.println("Staff not found: " + StaffID);
        }
    }

    public synchronized void updateStaff(String staffID, String name, String role, String gender, int age) {
        List<Staff> staffList = readAllStaff();
        boolean updated = false;
        
        for (int i = 0; i < staffList.size(); i++) {
            if (staffList.get(i).getStaffID().equals(staffID)) {
                staffList.set(i, new Staff(staffID, name, role, gender, age));
                updated = true;
                break;
            }
        }
        
        if (updated) {
            writeAllStaff(staffList);
            System.out.println("Staff " + staffID + " has been updated.");
        } else {
            System.out.println("Staff not found: " + staffID);
        }
    }

    public void updateStaffDetails() {
        Scanner scanner = new Scanner(System.in);
        
        System.out.println("Staff List");
        System.out.println("--------------------");
        List<Staff> staffList = readAllStaff();
        Set<String> printed = new HashSet<>();
        for (Staff staff : staffList) {
            if (!printed.contains(staff.getStaffID())) {
                System.out.printf("ID: %s, Name: %s, Role: %s, Gender: %s, Age: %d%n",
                    staff.getStaffID(),
                    staff.getName(),
                    staff.getRole(),
                    staff.getGender(),
                    staff.getAge());
                printed.add(staff.getStaffID());
            }
        }
        
        // Ask for staff ID to update
        System.out.print("\nEnter Staff ID to update: ");
        String staffId = scanner.nextLine();
        
        // Find the current staff details
        Staff currentStaff = null;
        for (Staff staff : staffList) {
            if (staff.getStaffID().equals(staffId)) {
                currentStaff = staff;
                break;
            }
        }
        
        if (currentStaff == null) {
            System.out.println("Staff not found!");
            return;
        }

        // Show update options menu
        System.out.println("\nWhat would you like to update?");
        System.out.println("1. Name");
        System.out.println("2. Role");
        System.out.println("3. Gender");
        System.out.println("4. Age");
        System.out.println("5. Cancel");
        
        System.out.print("Enter your choice (1-5): ");
        String choice = scanner.nextLine();
        
        // Initialize variables with current values
        String newName = currentStaff.getName();
        String newRole = currentStaff.getRole();
        String newGender = currentStaff.getGender();
        int newAge = currentStaff.getAge();
        
        switch (choice) {
            case "1":
                System.out.print("Enter new name: ");
                newName = scanner.nextLine();
                break;
                
            case "2":
                System.out.print("Enter new role: ");
                newRole = scanner.nextLine();
                break;
                
            case "3":
                System.out.print("Enter new gender: ");
                newGender = scanner.nextLine();
                break;
                
            case "4":
                System.out.print("Enter new age: ");
                try {
                    newAge = Integer.parseInt(scanner.nextLine());
                } catch (NumberFormatException e) {
                    System.out.println("Invalid age format. Update cancelled.");
                    return;
                }
                break;
                
            case "5":
                System.out.println("Update cancelled.");
                return;
                
            default:
                System.out.println("Invalid choice! Update cancelled.");
                return;
        }
        
        // Call the updateStaff method with the new values
        updateStaff(staffId, newName, newRole, newGender, newAge);
    }

    @Override
    public void viewStaffList() {
    System.out.println("Staff List");
    System.out.println("--------------------");
    List<Staff> staffList = readAllStaff();
    Set<String> printed = new HashSet<>();
    for (Staff staff : staffList) {
        if (!printed.contains(staff.getStaffID())) {
            System.out.printf("ID: %s, Name: %s, Role: %s, Gender: %s, Age: %d%n",
                staff.getStaffID(),
                staff.getName(),
                staff.getRole(),
                staff.getGender(),
                staff.getAge());
            printed.add(staff.getStaffID());
        }
    }
}
}
