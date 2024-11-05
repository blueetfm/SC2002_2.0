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
import java.util.Set;


public class StaffList implements StaffManager {
    private String csvFilePath;
    private static final String CSV_HEADER = "Staff ID,Name,Role,Gender,Age";
    
    public StaffList(String csvFilePath) {
        this.csvFilePath = csvFilePath;
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

    @Override
    public synchronized void addStaff(String StaffID, String password, String name, String role, String gender, int age) {
        List<Staff> staffList = readAllStaff();
        
        boolean exists = staffList.stream()
            .anyMatch(s -> s.getStaffID().equals(StaffID));
        
        if (exists) {
            System.out.println("Staff ID already exists: " + StaffID);
            return;
        }

        staffList.add(new Staff(StaffID, name, role, gender, age));
        writeAllStaff(staffList);
        System.out.println("Staff " + StaffID + " has been added to the system with default password:" + password);
    }

    @Override
    public synchronized void removeStaff(String StaffID) {
        List<Staff> staffList = readAllStaff();
        boolean removed = staffList.removeIf(staff -> staff.getStaffID().equals(StaffID));
        
        if (removed) {
            writeAllStaff(staffList);
            System.out.println("Staff " + StaffID + " has been removed.");
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
